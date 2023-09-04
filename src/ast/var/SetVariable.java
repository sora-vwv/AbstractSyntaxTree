package ast.var;

import ast.*;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class SetVariable extends Position implements Statement {

    private final Variable variable;
    private final Expression value;

    public SetVariable(Variable variable, Expression value, Position position) throws AstException {
        super(position);
        this.variable = variable;
        this.value = value;

        if (!variable.getType().equals(value.getType()))
            throw new AstException("Несовпадение типов.", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        JVM type = variable.getType();

        value.codegen(mv);

        if      (type.isIntJVM() || type.isBoolean())   mv.visitVarInsn(ISTORE, variable.id);
        else if (type.isLong())                         mv.visitVarInsn(LSTORE, variable.id);
        else if (type.isFloat())                        mv.visitVarInsn(FSTORE, variable.id);
        else if (type.isDouble())                       mv.visitVarInsn(DSTORE, variable.id);
        else if (type.isReference() || type.isArray())  mv.visitVarInsn(ASTORE, variable.id);
    }

}
