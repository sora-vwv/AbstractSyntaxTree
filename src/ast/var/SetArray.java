package ast.var;

import ast.*;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.AALOAD;

public class SetArray extends Position implements AstNode {

    private final Variable variable;
    private final List<GetData> indexes;
    private final GetData value;

    public SetArray(Variable variable, List<GetData> indexes, GetData value, Position position) throws AstException {
        super(position);

        this.value = value;
        this.variable = variable;
        this.indexes = indexes;

        JVM type = variable.getType();

        if(indexes.size() > type.getDepth())
            throw new AstException("Выход за пределы уровня масива.", this);

        if (type.getDepth() == 0)
            throw new AstException("Размер массива некорректный.", this);

        if(!getType().equals(value.getType()))
            throw new AstException("Несовпадение типов.", this);

        for (GetData index: indexes)
            if(!index.getType().isIntJVM())
                throw new AstException("Индексом массива может быть только число.", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitVarInsn(ALOAD, variable.id);

        int temp = indexes.size();
        JVM type = getType();

        for (GetData index: indexes) {
            index.codegen(mv);

            if(temp == 1) {
                value.codegen(mv);
                if      (type.isBoolean() || type.isByte())     mv.visitInsn(BASTORE);
                else if (type.isChar())                         mv.visitInsn(CASTORE);
                else if (type.isShort())                        mv.visitInsn(SASTORE);
                else if (type.isInt())                          mv.visitInsn(IASTORE);
                else if (type.isLong())                         mv.visitInsn(LASTORE);
                else if (type.isFloat())                        mv.visitInsn(FASTORE);
                else if (type.isDouble())                       mv.visitInsn(DASTORE);
                else if (type.isReference() || type.isArray())  mv.visitInsn(AASTORE);
            } else
                mv.visitInsn(AALOAD);

            temp--;
        }
    }

    private JVM getType() {
        return new JVM(variable.getType(), variable.getType().getDepth() - indexes.size());
    }

}
