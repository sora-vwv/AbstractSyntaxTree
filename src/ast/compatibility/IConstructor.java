package ast.compatibility;

import ast.Modifier;

import java.util.ArrayList;

public class IConstructor {

    private final ArrayList<IArgument> arguments = new ArrayList<>();
    private final Modifier modifier;

    public IConstructor(Modifier modifier) {
        this.modifier = modifier;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void addArgument(IArgument argument) {
        arguments.add(argument);
    }

}
