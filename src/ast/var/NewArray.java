package ast.var;

import ast.*;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class NewArray extends Position implements Expression {

    private final List<Expression> sizes;
    private final JVM type;

    public NewArray(JVM type, List<Expression> sizes, Position position) throws AstException {
        super(position);
        this.sizes = sizes;
        this.type = type;


        if (type.getDepth() == 0 || type.getDepth() != sizes.size())
            throw new AstException("Размер массива некорректный.", this);

        for (Expression size: sizes)
            if(!size.getType().isIntJVM())
                throw new AstException("Индексом массива может быть только число.", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {

        for (Expression size: sizes)
            size.codegen(mv);

        if (type.getDepth() > 1 || type.isArrayReference()) {
            mv.visitMultiANewArrayInsn(type.getInternalType(), type.getDepth());
        } else if(type.getDepth() == 1) {
            if      (type.isArrayBoolean())  mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BOOLEAN);
            else if (type.isArrayByte())     mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BYTE);
            else if (type.isArrayChar())     mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_CHAR);
            else if (type.isArrayShort())    mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_SHORT);
            else if (type.isArrayInt())      mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
            else if (type.isArrayLong())     mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_LONG);
            else if (type.isArrayFloat())    mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_FLOAT);
            else if (type.isArrayDouble())   mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_DOUBLE);
        }
    }

    @Override
    public JVM getType() throws AstException {
        return type;
    }

}
