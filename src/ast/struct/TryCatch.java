package ast.struct;

import ast.AstException;
import ast.AstNode;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

public class TryCatch extends Position implements AstNode {

    public TryCatch(Position pos) {
        super(pos);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {

    }

}
