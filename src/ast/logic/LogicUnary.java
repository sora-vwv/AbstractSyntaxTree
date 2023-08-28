package ast.logic;

import ast.AstException;
import ast.Position;
import ast.GetData;
import ast.JVM;
import org.objectweb.asm.MethodVisitor;

public class LogicUnary extends Position implements GetData {

    private final GetData value;

    protected LogicUnary(GetData value, Position position) {
        super(position);
        this.value = value;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        value.codegen(mv);
    }

    @Override
    public JVM getType() throws AstException {
        return new JVM(JVM.Type.BOOLEAN);
    }

}
