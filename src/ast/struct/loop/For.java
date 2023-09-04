package ast.struct.loop;

import ast.AstException;
import ast.AstNode;
import ast.Expression;
import ast.struct.Body;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class For implements AstNode {

    private final Body body;

    private final AstNode struct_first;
    private final Expression condition;
    private final AstNode struct_second;

    private final Label point_start = new Label();
    private final Label point_end = new Label();

    public For(AstNode struct_first, Expression condition, AstNode struct_second, Body body) throws AstException {
        this.struct_first = struct_first;
        if (!condition.getType().isBoolean())
            throw new AstException("Условием for может быть только boolean значение", condition.getPosition());
        this.condition = condition;
        this.struct_second = struct_second;

        this.body = body;
        body.pushContinue(point_start);
        body.pushBreak(point_end);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {

        if (struct_first != null)
            struct_first.codegen(mv);

        mv.visitLabel(point_start);
        if (condition != null) {
            condition.codegen(mv);
            mv.visitJumpInsn(IFEQ, point_end);
        }

        body.codegen(mv);

        if (struct_second != null)
            struct_second.codegen(mv);
        mv.visitJumpInsn(GOTO, point_start);

        mv.visitLabel(point_end);

        body.popContinue();
        body.popBreak();
    }

}