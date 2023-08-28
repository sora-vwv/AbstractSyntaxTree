package ast.var;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class GetVariable extends Position implements GetData {

    private final Variable variable;

    public GetVariable(Variable variable, Position position) {
        super(position);
        this.variable = variable;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        JVM type = variable.getType();

        if (type.isIntJVM() || type.isBoolean()) mv.visitVarInsn(ILOAD, variable.id);
        else if (type.isLong()) mv.visitVarInsn(LLOAD, variable.id);
        else if (type.isFloat()) mv.visitVarInsn(FLOAD, variable.id);
        else if (type.isDouble()) mv.visitVarInsn(DLOAD, variable.id);
        else if (type.isReference() || type.isArray()) mv.visitVarInsn(DLOAD, variable.id); // это тот случай, когда array воспринимается как reference
    }

    @Override
    public JVM getType() throws AstException {
        return variable.getType();
    }

}
