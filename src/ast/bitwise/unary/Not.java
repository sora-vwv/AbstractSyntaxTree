package ast.bitwise.unary;

import ast.AstException;
import ast.GetDataAst;
import ast.Position;
import ast.bitwise.BitwiseUnary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*

@author Sora

 */

public class Not extends BitwiseUnary {

    public Not(GetDataAst value, Position position) throws AstException {
        super(value, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv);
        if (getType().isLong()) {
            mv.visitLdcInsn((long)-1);
            mv.visitInsn(LXOR);
        } else {
            mv.visitInsn(ICONST_M1);
            mv.visitInsn(IXOR);
        }
    }

}
