package ast.struct;

import ast.AstException;
import ast.Statement;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;

public class CGoto implements Statement {

    private final Label label;

    public CGoto(CLabel label) {
        this.label = label.value;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitJumpInsn(GOTO, label);
    }

}
