package ast.cast;

import ast.AstException;
import ast.GetDataAst;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.CHECKCAST;

/*

@author Sora

 */

public class CastTo extends Position implements GetDataAst {

    private final GetDataAst value;

    private final JVM to;

    public CastTo(GetDataAst value, JVM to, Position position) throws AstException {
        super(position);

        this.value = value;
        if (value.getType().isReference() && to.isReference())
            if (!value.getType().isReference() || !to.isReference())
                throw new AstException("reference кастится только в reference.", this);
        if (value.getType().isArray() && to.isArray())
            if (!value.getType().isArray() || !to.isArray())
                throw new AstException("array кастится только в array.", this);
        if (value.getType().isBoolean() || to.isBoolean())
            throw new AstException("boolean не кастится.", this);
        this.to = to;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        value.codegen(mv);
        if (value.getType().isArray())
            mv.visitTypeInsn(CHECKCAST, to.getInternalType());
        else if (value.getType().isReference())
            mv.visitTypeInsn(CHECKCAST, to.getReference());
        else if (to.isDouble())
            value.toDouble(mv);
        else if (to.isFloat())
            value.toFloat(mv);
        else if (to.isLong())
            value.toLong(mv);
        else if (to.isInt())
            value.toInt(mv);
        else if (to.isShort())
            value.toShort(mv);
        else if (to.isChar())
            value.toChar(mv);
        else if (to.isByte())
            value.toByte(mv);
    }

    @Override
    public JVM getType() throws AstException {
        return to;
    }

}
