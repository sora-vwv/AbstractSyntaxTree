package ast.struct.condition;

import ast.AstException;
import ast.Statement;
import ast.Expression;
import ast.Position;
import ast.struct.Body;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class IfElse extends Position implements Statement {

    private final Expression condition;
    private final Body body_if;
    private final Body body_else;

    public IfElse(Expression condition, Body body_if, Body body_else, Position position) throws AstException {
        super(position);

        if (!condition.getType().isBoolean())
            throw new AstException("Условием if может быть только boolean значение", this);
        this.condition = condition;
        this.body_if = body_if;
        this.body_else = body_else;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        Label L1 = new Label();
        Label L2 = new Label();

        condition.codegen(mv);
        mv.visitJumpInsn(IFEQ, L1);
        body_if.codegen(mv);

        if (body_else != null) {
            mv.visitJumpInsn(GOTO, L2);

            mv.visitLabel(L1);
            body_else.codegen(mv);

            mv.visitLabel(L2);
        } else
            mv.visitLabel(L1);
    }

}
