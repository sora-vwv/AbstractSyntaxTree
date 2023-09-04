package ast.bitwise;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

public class BitwiseUnary extends Position implements Expression {

    private final Expression value;

    protected BitwiseUnary(Expression value, Position position) throws AstException {
        super(position);

        this.value = value;
        if (value.getType().isLong() || value.getType().isIntJVM())
            throw new AstException("Побитовые операторы работают только с int и long.", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        value.codegen(mv);
    }

    @Override
    public JVM getType() throws AstException {
        return value.getType();
    }

}