package ast.compatibility;

import ast.JVM;
import ast.Modifier;

import java.util.ArrayList;

public class IMethod {

    private final String name;
    private final JVM    type;

    private final Modifier modifier;

    private final ArrayList<IArgument> arguments = new ArrayList<>();

    public IMethod(String name, JVM type, Modifier modifier) {
        this.name = name;
        this.type = type;
        this.modifier = modifier;
    }

    public void addArgument(IArgument argument) {
        arguments.add(argument);
    }

    public String getName() {
        return name;
    }

    public JVM getType() {
        return type;
    }

    public Modifier getModifier() {
        return modifier;
    }

}
