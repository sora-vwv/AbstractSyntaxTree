package ast.struct;

import ast.AstException;
import ast.AstNode;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

public class CLabel extends Position implements AstNode {

    public CLabel(Position position) {
        super(position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {

    }

}
