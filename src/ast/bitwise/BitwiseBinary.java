package ast.bitwise;

import ast.AstException;
import ast.GetDataAst;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

/*

@author Sora

*/

public abstract class BitwiseBinary extends Position implements GetDataAst {

    protected final GetDataAst left;
    protected final GetDataAst right;

    protected BitwiseBinary(GetDataAst left, GetDataAst right, Position position) throws AstException {
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
