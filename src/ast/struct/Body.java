package ast.struct;

import ast.AstException;
import ast.AstNode;
import ast.GetData;
import ast.var.LocalVariableCounter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.*;

public class Body implements AstNode {

    private final LocalVariableCounter counter;
    final BodyData data;

    ArrayList<AstNode> structs = new ArrayList<>();

    Body() {
        this.counter = new LocalVariableCounter();
        this.data = new BodyData();
    }

    private Body(Body parent) {
        this.counter = new LocalVariableCounter(parent.counter);
        this.data = parent.data;
    }

    public Body createChildBody() {
        return new Body(this);
    }

    public void add(AstNode struct) {
        structs.add(struct);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        codegenStart(mv);
        for(AstNode struct: structs) {
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
            if (struct instanceof GetData)
                if (!((GetData) struct).getType().isVoid())
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

}
