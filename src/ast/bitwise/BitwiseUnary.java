package ast.bitwise;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

/*

@author Sora

*/

public class BitwiseUnary extends Position implements GetData {

    private final GetData value;

    protected BitwiseUnary(GetData value, Position position) throws AstException {
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