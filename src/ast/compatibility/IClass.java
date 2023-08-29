package ast.compatibility;

import ast.JVM;
import ast.Modifier;

import java.util.ArrayList;

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
        constructors.add(constructor);
    }

    public void addMethod(IMethod method) {
        methods.add(method);
    }

    public void addField(IField field) {
        fields.add(field);
    }

}
