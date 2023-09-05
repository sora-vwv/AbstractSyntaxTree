package ast.struct;

import ast.AstException;
import ast.Statement;
import ast.Expression;
import ast.var.LocalVariableCounter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.*;

public class Body implements Statement {

    private final LocalVariableCounter counter;
    final BodyData data;

    ArrayList<Statement> structs = new ArrayList<>();

    public Body() {
        this.counter = new LocalVariableCounter();
        this.data = new BodyData();
    }

    private Body(Body parent) {
        this.counter = new LocalVariableCounter(parent.counter);
        this.data = parent.data;
    }

    public Body createChildBody() {
        Body body = new Body(this);
        this.add(body);
        return body;
    }

    public void add(Statement struct) {
        structs.add(struct);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        codegenStart(mv);
        for(Statement struct: structs) {
            struct.codegenLine(mv);
            struct.codegenStart(mv);

            if (struct instanceof Break) {
                if (data.points_break.empty())
                    throw new AstException("break без метки", (Break)struct);
                ((Break)struct).value = data.points_break.peek();
            }

            if (struct instanceof Continue) {
                if (data.points_continue.empty())
                    throw new AstException("continue без метки", (Continue)struct);
                ((Continue)struct).value = data.points_continue.peek();
            }

            struct.codegen(mv);
            if (struct instanceof Expression)
                if (!((Expression) struct).getType().isVoid())
                    mv.visitInsn(POP);
            struct.codegenEnd(mv);
        }
        codegenEnd(mv);
        counter.codegenVariables(mv, label_start, label_end);
    }

    public LocalVariableCounter getCounter() {
        return counter;
    }

    public void popBreak() {
        data.points_break.pop();
    }

    public void popContinue() {
        data.points_continue.pop();
    }

    public void pushBreak(Label value) {
        data.points_break.push(value);
    }

    public void pushContinue(Label value) {
        data.points_continue.push(value);
    }

    public boolean isLastReturn() {
        if (structs.size() == 0)
            return false;

        Statement statement = structs.get(structs.size()-1);
        if (statement instanceof Body)
            return ((Body) statement).isLastReturn();
        return statement instanceof Return;
    }

}
