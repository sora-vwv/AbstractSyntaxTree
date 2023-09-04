package ast.struct.loop;

import ast.AstException;
import ast.Statement;
import ast.Expression;
import ast.struct.Body;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class DoWhile implements Statement {

    private final Body body;
    private final Expression condition;

    private final Label point_start = new Label();
    private final Label point_end = new Label();

    public DoWhile(Expression condition, Body body) throws AstException {
        this.condition = condition;
        if (!condition.getType().isBoolean())
            throw new AstException("Условием do-while может быть только boolean значение", condition.getPosition());

        this.body = body;
        body.pushContinue(point_start);
        body.pushBreak(point_end);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        Label skip = new Label();
        mv.visitJumpInsn(GOTO, skip);

        mv.visitLabel(point_start);
        condition.codegen(mv);
        mv.visitJumpInsn(IFEQ, point_end);

        mv.visitLabel(skip);
        body.codegen(mv);
        mv.visitJumpInsn(GOTO, point_start);

        mv.visitLabel(point_end);
    }
}
