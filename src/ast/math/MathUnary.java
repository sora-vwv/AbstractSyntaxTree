package ast.math;

import ast.AstException;
import ast.Position;
import ast.JVM;
import ast.Expression;
import org.objectweb.asm.MethodVisitor;

/*
Унарные математические операции.
Здесь нет никаких преобразований в иные типы данных.
*/

public class MathUnary extends Position implements Expression {

    // значение, которое будет обрабатывать операция
    private final Expression value;

    protected MathUnary(Expression value, Position position) throws AstException {
        super(position);

        this.value = value;
        if (value.getType().isNotNumber())
            throw new AstException("Унарные математические операторы работают только с числами", this);
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
