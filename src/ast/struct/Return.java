package ast.struct;

import ast.*;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Return extends Position implements AstNode {

    private final GetData data;

    public Return(GetData data, Position pos) {
        super(pos);
        this.data = data;
    }

    public Return(Position pos) {
        super(pos);
        this.data = null;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        if (data == null) {
            mv.visitInsn(RETURN);
            return;
        }

        data.codegen(mv);
        JVM type = data.getType();

        if      (type.isIntJVM() || type.isBoolean())    mv.visitInsn(IRETURN);
        else if (type.isLong())                          mv.visitInsn(LRETURN);
        else if (type.isFloat())                         mv.visitInsn(FRETURN);
        else if (type.isDouble())                        mv.visitInsn(DRETURN);
        else if (type.isReference() || type.isArray())   mv.visitInsn(ARETURN);
    }

}
