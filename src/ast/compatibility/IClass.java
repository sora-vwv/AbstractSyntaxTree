package ast.compatibility;

import ast.AstException;
import ast.JVM;
import ast.Modifier;

import java.util.ArrayList;
import java.util.Objects;

public class IClass {

    private final JVM classname;
    private final JVM classname_super;
    private final Modifier modifier;

    private final ArrayList<IConstructor> constructors = new ArrayList<>();
    private final ArrayList<IMethod>      methods      = new ArrayList<>();
    private final ArrayList<IField>       fields       = new ArrayList<>();

    public IClass(JVM classname, JVM classname_super, Modifier modifier) {
        this.classname = classname;
        this.classname_super = classname_super;
        this.modifier = modifier;
    }

    public JVM getType() {
        return classname;
    }

    public JVM getTypeSuper() {
        return classname_super;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void addConstructor(IConstructor constructor) {
        constructor.parent = this;
        constructors.add(constructor);
    }

    public void addMethod(IMethod method) {
        method.parent = this;
        methods.add(method);
    }

    public void addField(IField field) {
        field.parent = this;
        fields.add(field);
    }

    public ArrayList<IConstructor> getConstructors() {
        return constructors;
    }

    public ArrayList<IMethod> getMethods() {
        return methods;
    }

    public ArrayList<IField> getFields() {
        return fields;
    }

    public IField getField(String name) throws AstException {
        IClass clazz = this;
        while (clazz != null) {
            for (IField field: fields)
                if (Objects.equals(field.getName(), name) && !field.getModifier().isStatic())
                    return field;
            clazz = IList.get(clazz.getTypeSuper().getReference());
        }
        return null;
    }

    public IField getFieldStatic(String name) throws AstException {
        IClass clazz = this;
        while (clazz != null) {
            for (IField field: fields)
                if (Objects.equals(field.getName(), name) && field.getModifier().isStatic())
                    return field;
            clazz = IList.get(clazz.getTypeSuper().getReference());
        }
        return null;
    }

}
