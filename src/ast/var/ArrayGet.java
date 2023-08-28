package ast.var;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.AALOAD;

public class ArrayGet extends Position implements GetData {

    private final Variable variable;
    private final GetData[] indexes;

    public ArrayGet(Variable variable, GetData[] indexes, Position position) throws AstException {
        super(position);

        if(indexes.length > variable.getType().getDepth())
            throw new RuntimeException("Выход за пределы уровня масива");

        for (GetData data: indexes)
            if(!data.getType().isIntJVM())
                throw new AstException("Индексом массива может быть только число", this);

        this.variable = variable;
        this.indexes = indexes;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitVarInsn(ALOAD, variable.id);

        JVM type = variable.getType();
        int temp = type.getDepth();

        for (GetData data: indexes) {
            data.codegen(mv);

            if(temp == 1) {
                if (type.isBoolean() || type.isByte())  mv.visitInsn(BALOAD);
                else if(type.isChar())                  mv.visitInsn(CALOAD);
                else if(type.isShort())                 mv.visitInsn(SALOAD);
                else if(type.isInt())                   mv.visitInsn(IALOAD);
                else if(type.isLong())                  mv.visitInsn(LALOAD);
                else if(type.isFloat())                 mv.visitInsn(FALOAD);
                else if(type.isDouble())                mv.visitInsn(DALOAD);
                else if(type.isReference())             mv.visitInsn(AALOAD);
            } else
                mv.visitInsn(AALOAD);
            temp--;
        }
    }

    @Override
    public JVM getType() throws AstException {
        return new JVM(variable.getType(), variable.getType().getDepth() - indexes.length);
    }

}
