package ast.math;

import ast.AstException;
import ast.Position;
import ast.GetData;
import ast.JVM;
import org.objectweb.asm.MethodVisitor;

/*
Бинарные математические операции.
 */

public class MathBinary extends Position implements GetData {

    // значения, которые будет обрабатывать операция
    private final GetData left;
    private final GetData right;

    protected MathBinary(GetData left, GetData right, Position position) throws AstException {
        super(position);

        this.left = left;
        if (left.getType().isNotNumber())
            throw new AstException("Бинарные математические операторы работают только с числами.", this);

        this.right = right;
        if (right.getType().isNotNumber())
            throw new AstException("Бинарные математические операторы работают только с числами.", this);
    }

    // преобразование типов данных по приоритету    D > F > J > I|B|S|C
    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        if (getType().isDouble()) {
            left.codegen(mv);
            left.toDouble(mv);
            right.codegen(mv);
            right.toDouble(mv);
        } else if (getType().isFloat()) {
            left.codegen(mv);
            left.toFloat(mv);
            right.codegen(mv);
            right.toFloat(mv);
        } else if (getType().isLong()) {
            left.codegen(mv);
            left.toLong(mv);
            right.codegen(mv);
            right.toLong(mv);
        } else {
            left.codegen(mv);
            left.toInt(mv);
            right.codegen(mv);
            right.toInt(mv);
        }
    }

    // схема приоритета типов данных при оперциях    D > F > J > I|B|S|C
    @Override
    public JVM getType() throws AstException {
        if (left.getType().isDouble() || right.getType().isDouble())
            return new JVM(JVM.Type.DOUBLE);

        if (left.getType().isFloat() || right.getType().isFloat())
            return new JVM(JVM.Type.FLOAT);

        if (left.getType().isLong() || right.getType().isLong())
            return new JVM(JVM.Type.LONG);

        return new JVM(JVM.Type.INT);
    }

}

