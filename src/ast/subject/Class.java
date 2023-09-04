package ast.subject;

import ast.AstException;
import ast.JVM;
import ast.Modifier;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;

public class Class {

    private final JVM classname;
    private final JVM classname_super;
    private final Modifier modifier;

    private final ArrayList<Constructor> constructors = new ArrayList<>();
    private final ArrayList<Method> methods = new ArrayList<>();
    private final ArrayList<Field> fields = new ArrayList<>();

    ClassWriter cw;
    Constructor init;

    public Class(String classname, String classname_super, Modifier modifier) {
        this.classname = new JVM(classname);
        this.classname_super = new JVM(classname_super);
        this.modifier = modifier;
    }

    public byte[] codegen() throws AstException {
        this.cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(49, modifier.codegen(), classname.getReference(), null, classname_super.getReference(), null);

        for(Method method: methods)
            method.codegen(this);
        for(Constructor constructor: constructors)
            constructor.codegen(this);

        for(Field field: fields)
            field.codegen(this);

        cw.visitEnd();
        return cw.toByteArray();
    }

    public void addConstructor(Constructor constructor) {
        constructors.add(constructor);
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public void addField(Field field) {
        fields.add(field);
    }

}
