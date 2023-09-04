package ast.struct;

import ast.AstException;
import ast.Statement;
import ast.Position;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;

public class Continue extends Position implements Statement {

    // меняется при добавлении в Body
    Label value;

    public Continue(Position position) {
        super(position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitJumpInsn(GOTO, value);
    }
}
