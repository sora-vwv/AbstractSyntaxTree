package ast.struct;

import ast.*;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ATHROW;

public class Throw extends Position implements AstNode {
    private final static JVM necessary = new JVM("java.lang.Throwable");

    private final GetData data;

    public Throw(GetData data, Position pos) throws AstException {
        super(pos);

        if (!necessary.equals(data.getType()))
            throw new AstException("Тип значения для throw не подходит.", this);
        this.data = data;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        data.codegen(mv);
        mv.visitInsn(ATHROW);
    }

}