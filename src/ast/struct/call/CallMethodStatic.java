package ast.struct.call;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import ast.compatibility.IClass;
import ast.compatibility.IList;
import ast.compatibility.IMethod;
import ast.util.Overload;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

public class CallMethodStatic extends Position implements Expression {

    private final IMethod method;

    public CallMethodStatic(JVM type, String name, ArrayList<Expression> arguments, Position position) throws AstException {
        super(position);

        if (!type.isReference())
            throw new AstException("Объектом для вызова метода может быть только reference", this);

        IClass clazz = IList.get(type.getReference());
        if (clazz == null)
            throw new AstException("Класс \""+type.getValue()+"\" не найден!", this.getPosition());
        this.method = Overload.getOverloadMethod(clazz, Overload.toArray(arguments), name);
        if (this.method == null)
            throw new AstException("Метод\""+name+"\" в классе \""+type.getValue()+"\" не найдено", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitMethodInsn(INVOKESTATIC, method.getParent().getType().getReference(), method.getName(), method.getDescriptor());
    }

    @Override
    public JVM getType() throws AstException {
        return method.getType();
    }

}
