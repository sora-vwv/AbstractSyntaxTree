package ast.logic;

import ast.AstException;
import ast.Position;
import ast.GetDataAst;
import ast.JVM;
import org.objectweb.asm.MethodVisitor;

/*

@author Sora

 */

public class LogicUnary extends Position implements GetDataAst {

    private final GetDataAst value;

    protected LogicUnary(GetDataAst value, Position position) {
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
