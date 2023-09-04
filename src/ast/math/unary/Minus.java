package ast.math.unary;

import ast.AstException;
import ast.Expression;
import ast.Position;
import ast.math.MathUnary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Унарный минус
 */

public class Minus extends MathUnary {

    // конструктор должен быть публичным
    public Minus(Expression value, Position position) throws AstException {
        super(value, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv); // там же происходить генерация значения value

        if (getType().isIntJVM())
            mv.visitInsn(INEG);
        else if (getType().isLong())
            mv.visitInsn(LNEG);
        else if (getType().isFloat())
            mv.visitInsn(FNEG);
        else if (getType().isDouble())
            mv.visitInsn(DNEG);
    }

}
