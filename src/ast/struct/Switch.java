package ast.struct;

import ast.AstException;
import ast.Expression;
import ast.Statement;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

// позже переделать

public class Switch implements Statement {

    private final ArrayList<Body> bodies;
    private final Body default_body;
    private final int[] values;
    private final Expression condition;

    private final Label point_end = new Label();

    public Switch(Expression condition, ArrayList<Body> bodies, Body default_body, int[] values) {
        this.condition = condition;
        this.default_body = default_body;
        if (default_body != null)
            default_body.pushBreak(point_end);
        this.values = values;
        this.bodies = bodies;

        for (Body body: bodies)
            body.pushBreak(point_end);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        condition.codegen(mv);

        Label[] labels = new Label[bodies.size()];
        for (int i = 0, bodiesSize = bodies.size(); i < bodiesSize; i++) {
            Body body = bodies.get(i);
            labels[i] = body.getLabelStart();
        }
        mv.visitLookupSwitchInsn(default_body.getLabelStart(), values, labels);

        mv.visitLabel(point_end);
        for (Body body: bodies)
            body.popBreak();
        if (default_body != null)
            default_body.popBreak();
    }

}
