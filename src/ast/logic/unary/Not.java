package ast.logic.unary;

import ast.AstException;
import ast.GetData;
import ast.Position;
import ast.logic.LogicUnary;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Not extends LogicUnary {

    public Not(GetData value, Position position) throws AstException {
        super(value, position);
        if (!value.getType().isBoolean())
            throw new AstException("Оператор not работает только с boolean", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        Label L1 = new Label();
        Label L2 = new Label();

        super.codegen(mv);
        mv.visitJumpInsn(IFNE, L1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, L2);
        mv.visitLabel(L1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(L2);
    }

}
