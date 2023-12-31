package ast.math.binary;

import ast.AstException;
import ast.Expression;
import ast.Position;
import ast.math.MathBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Mul extends MathBinary {

    public Mul(Expression left, Expression right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv);

        if (getType().isDouble())
            mv.visitInsn(DMUL);
        else if (getType().isFloat())
            mv.visitInsn(FMUL);
        else if (getType().isLong())
            mv.visitInsn(LMUL);
        else
            mv.visitInsn(IMUL);
    }

}
