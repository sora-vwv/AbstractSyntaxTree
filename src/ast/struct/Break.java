package ast.struct;

import ast.AstException;
import ast.AstNode;
import ast.Position;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;

public class Break extends Position implements AstNode {

    // меняется при добавлении в Body
    Label value;

    public Break(Position position) {
        super(position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitJumpInsn(GOTO, value);
    }
}
