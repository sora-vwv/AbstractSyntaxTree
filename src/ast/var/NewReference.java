package ast.var;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import ast.compatibility.IClass;
import ast.compatibility.IConstructor;
import ast.compatibility.IList;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static ast.util.Overload.*;
import static org.objectweb.asm.Opcodes.*;

public class NewReference extends Position implements Expression {

    private final JVM classname;
    private final IConstructor constructor;
    private final ArrayList<Expression> values;

    public NewReference(JVM classname, ArrayList<Expression> values, Position position) throws AstException {
        super(position);

        if (!classname.isReference())
            throw new AstException("Новым объектом может быть только reference", this);
        this.classname = classname;

        IClass clazz = IList.get(classname.getReference());
        if (clazz == null)
            throw new AstException("Класс \""+classname.getReference()+"\" не найден", this);

        this.constructor = getOverloadConstructor(clazz, toArray(values));
        if (this.constructor == null)
            throw new AstException("Конструктор класса \""+classname.getReference()+"\" не найден", this);
        this.values = values;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitTypeInsn(NEW, classname.getReference());
        mv.visitInsn(DUP);
        for (Expression data: values)
            data.codegen(mv);
        mv.visitMethodInsn(INVOKESPECIAL, classname.getReference(), "<init>", constructor.getDescriptor());
    }

    @Override
    public JVM getType() throws AstException {
        return classname;
    }

}
