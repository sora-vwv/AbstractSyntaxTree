package ast.struct;

import ast.*;
import ast.var.Variable;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GOTO;

public class TryCatch extends Position implements Statement {
    private final static JVM necessary = new JVM("java.lang.Throwable");

    private final Body body_try;
    private final Body body_catch;
    private final Variable variable;

    public TryCatch(Body body_try, Body body_catch, Variable variable, Position pos) throws AstException {
        super(pos);

        if (!necessary.equals(variable.getType()))
            throw new AstException("Тип значения для try-catch не подходит.", variable);
        this.body_try = body_try;
        this.body_catch = body_catch;
        this.variable = variable;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        Label L0 = new Label();
        Label L1 = new Label();
        Label L2 = new Label();
        Label L3 = new Label();

        mv.visitTryCatchBlock(L0, L1, L2, variable.getType().getReference());

        mv.visitLabel(L0);
        body_try.codegen(mv);

        mv.visitLabel(L1);
        mv.visitJumpInsn(GOTO, L3);

        mv.visitLabel(L2);
        mv.visitVarInsn(ASTORE, variable.getIndex());
        body_catch.codegen(mv);

        mv.visitLabel(L3);
    }

}
