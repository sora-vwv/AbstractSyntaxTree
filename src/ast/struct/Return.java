package ast.struct;

import ast.*;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Return extends Position implements AstNode {

    private final Expression data;

    public Return(Expression data, JVM necessary, Position pos) throws AstException {
        super(pos);

        if (!necessary.equals(data.getType()))
            throw new AstException("Тип значения для return не подходит.", this);
        this.data = data;
    }

    public Return(JVM necessary, Position pos) throws AstException {
        super(pos);

        if (!necessary.isVoid())
            throw new AstException("Тип значения для return не подходит.", this);
        data = null;
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
