package ast.compatibility;

import ast.JVM;
import ast.Modifier;

import java.util.ArrayList;

public class IConstructor extends IOverload {

    private final ArrayList<JVM> arguments = new ArrayList<>();
    private final Modifier modifier;

    IClass parent;

    public IConstructor(Modifier modifier) {
        this.modifier = modifier;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void addArgument(JVM argument) {
        arguments.add(argument);
    }

    @Override
    public JVM[] getArguments() {
        return arguments.toArray(new JVM[0]);
    }

    @Override
    public int getSizeArguments() {
        return arguments.size();
    }

    @Override
    public IClass getParent() {
        return parent;
    }
}
