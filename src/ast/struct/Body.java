package ast.struct;

import ast.AstException;
import ast.AstNode;
import ast.GetData;
import ast.var.LocalVariableCounter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.*;

public class Body implements AstNode {

    private final LocalVariableCounter counter;

    ArrayList<AstNode> structs = new ArrayList<>();

    public Body() {
        this.counter = new LocalVariableCounter();
    }

    public Body(LocalVariableCounter parent) {
        this.counter = new LocalVariableCounter(parent);
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

}
