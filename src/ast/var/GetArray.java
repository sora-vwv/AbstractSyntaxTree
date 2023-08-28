package ast.var;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.AALOAD;

public class GetArray extends Position implements GetData {

    private final Variable variable;
    private final List<GetData> indexes;

    public GetArray(Variable variable, List<GetData> indexes, Position position) throws AstException {
        super(position);

        if(indexes.size() > variable.getType().getDepth())
            throw new RuntimeException("Выход за пределы уровня масива");

        for (GetData index: indexes)
            if(!index.getType().isIntJVM())
                throw new AstException("Индексом массива может быть только число", this);

        this.variable = variable;
        this.indexes = indexes;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitVarInsn(ALOAD, variable.id);

        JVM type = variable.getType();
        int temp = type.getDepth();

        for (GetData index: indexes) {
            index.codegen(mv);

            if(temp == 1) {
                if (type.isArrayBoolean() || type.isArrayByte())  mv.visitInsn(BALOAD);
                else if(type.isArrayChar())                       mv.visitInsn(CALOAD);
                else if(type.isArrayShort())                      mv.visitInsn(SALOAD);
                else if(type.isArrayInt())                        mv.visitInsn(IALOAD);
                else if(type.isArrayLong())                       mv.visitInsn(LALOAD);
                else if(type.isArrayFloat())                      mv.visitInsn(FALOAD);
                else if(type.isArrayDouble())                     mv.visitInsn(DALOAD);
                else if(type.isArrayReference())                  mv.visitInsn(AALOAD);
            } else
                mv.visitInsn(AALOAD);

            temp--;
        }
    }

    @Override
    public JVM getType() throws AstException {
        return new JVM(variable.getType(), variable.getType().getDepth() - indexes.size());
    }

}
