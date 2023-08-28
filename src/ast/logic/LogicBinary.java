package ast.logic;

import ast.AstException;
import ast.Position;
import ast.GetData;
import ast.JVM;
import org.objectweb.asm.MethodVisitor;

public abstract class LogicBinary extends Position implements GetData {

    private final GetData left;
    private final GetData right;

    protected LogicBinary(GetData left, GetData right, Position position) throws AstException {
        super(position);

        this.left = left;
        if (left.getType().isNotNumber() && !left.getType().isBoolean())
            throw new AstException("Бинарные логические операторы обрабатывают только числа или boolean.", this);

        this.right = right;
        if (right.getType().isNotNumber() && !right.getType().isBoolean())
            throw new AstException("Бинарные логические операторы обрабатывают только числа или boolean.", this);
    }

    @Override
    public abstract void codegen(MethodVisitor mv) throws AstException;

    @Override
    public JVM getType() throws AstException {
        return new JVM(JVM.Type.BOOLEAN);
    }

    protected JVM getPriorityType() throws AstException {
        if (left.getType().isBoolean() && right.getType().isBoolean())
            return new JVM(JVM.Type.BOOLEAN);

        if (left.getType().isBoolean() || right.getType().isBoolean())
            throw new AstException("Бинарный логический оператор не может обрабатывать только один boolean", this);

        if (left.getType().isDouble() || right.getType().isDouble())
            return new JVM(JVM.Type.DOUBLE);

        if (left.getType().isFloat() || right.getType().isFloat())
            return new JVM(JVM.Type.FLOAT);

        if (left.getType().isLong() || right.getType().isLong())
            return new JVM(JVM.Type.LONG);

        return new JVM(JVM.Type.INT);
    }

    protected void codegenLeft(MethodVisitor mv) throws AstException {
        this.codegen(mv, left);
    }

    protected void codegenRight(MethodVisitor mv) throws AstException {
        this.codegen(mv, right);
    }

    private void codegen(MethodVisitor mv, GetData value) throws AstException {
        JVM priority = getPriorityType();

        value.codegen(mv);

        if (priority.isDouble())
            value.toDouble(mv);
        else if (priority.isFloat())
            value.toFloat(mv);
        else if (priority.isLong())
            value.toLong(mv);
        else if (priority.isIntJVM())
            value.toInt(mv);
        // если boolean, то кастить в другой тип не надо
    }

}
