package ast.math.binary;

import ast.AstException;
import ast.Expression;
import ast.Position;
import ast.math.MathBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Add extends MathBinary {

    // конструктор должен быть публичным
    public Add(Expression left, Expression right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv); // там же происходить генерация значений left right

        if (getType().isDouble())
            mv.visitInsn(DADD);
        else if (getType().isFloat())
            mv.visitInsn(FADD);
        else if (getType().isLong())
            mv.visitInsn(LADD);
        else
            mv.visitInsn(IADD);
    }

}
