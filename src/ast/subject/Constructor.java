package ast.subject;

import ast.*;
import ast.compatibility.IClass;
import ast.compatibility.IConstructor;
import ast.compatibility.IList;
import ast.struct.Body;
import ast.util.Overload;
import ast.var.Variable;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static ast.util.Overload.getOverloadConstructor;
import static ast.util.Overload.toArray;
import static org.objectweb.asm.Opcodes.*;

public class Constructor extends Position {
    private final static JVM necessary = new JVM("java.lang.Throwable");

    private ArrayList<Expression> values_parent = null;
    private boolean is_this_parent = false;

    private final Body body;
    private final Body body_header;
    private final Body body_custom;

    final Modifier modifier;
    final ArrayList<Variable> arguments;
    final ArrayList<String> throwable;

    private final Class clazz;
    private final Variable this_variable;

    public Constructor(Modifier modifier, Class clazz, Position position) {
        super(position);
        this.clazz = clazz;
        this_variable = new Variable("this", clazz.classname, new Position(-1, -1, null, null));

        body = new Body();
        body_header = body.createChildBody();
        body_custom = body.createChildBody();

        this.modifier = modifier;
        arguments = new ArrayList<>();
        throwable = new ArrayList<>();
    }

    void codegen(ArrayList<Field> fields) throws AstException {
        body.getCounter().add(this_variable);
        for (Variable variable: arguments)
            body_custom.getCounter().add(variable);

        MethodVisitor mv = clazz.cw.visitMethod(modifier.codegen(), "<init>", Overload.getDescriptor(arguments, new JVM(JVM.Type.VOID)), null, throwable.toArray(new String[0]));

        if (values_parent == null) {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        } else {
            JVM clazz_type = is_this_parent ? this.clazz.classname : this.clazz.classname_super;

            IClass clazz = IList.get(clazz_type.getReference());
            if (clazz == null)
                throw new AstException("Класс \"" + clazz_type.getReference() + "\" не найден", this);

            IConstructor constructor = getOverloadConstructor(clazz, toArray(values_parent));
            if (constructor == null)
                throw new AstException("Конструктор класса \"" + clazz_type.getReference() + "\" не найден", this);

            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, constructor.getParent().getType().getReference(), "<init>", constructor.getDescriptor());
        }

        for (Field field: fields)
            field.value.codegen(mv);

        body.codegen(mv);
        if (!body.isLastReturn())
            mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void addThrowable(JVM type, Position position) throws AstException {
        if (!necessary.equals(type))
            throw new AstException("Тип значения для throws не подходит.", position);
        throwable.add(type.getReference());
    }

    public void addArgument(Variable variable) {
        arguments.add(variable);
    }

    public Variable getThis() {
        return this_variable;
    }

    public void setParentConstructor(ArrayList<Expression> values, boolean is_this) {
        //
    }

    public Body getBody() {
        return body_custom;
    }

    Body getBodyHeader() {
        return body_header;
    }

}
