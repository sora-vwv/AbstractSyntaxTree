package ast.bitwise;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

public abstract class BitwiseBinary extends Position implements Expression {

    protected final Expression left;
    protected final Expression right;

    protected BitwiseBinary(Expression left, Expression right, Position position) throws AstException {
        super(position);

        this.left = left;
        if (left.getType().isLong() || left.getType().isIntJVM())
            throw new AstException("Побитовые операторы работают только с int и long.", this);

        this.right = right;
        if (right.getType().isLong() || right.getType().isIntJVM())
            throw new AstException("Побитовые операторы работают только с int и long.", this);
    }

    @Override
    public abstract void codegen(MethodVisitor mv) throws AstException;

    @Override
    public abstract JVM getType() throws AstException;

}
