package ast.compatibility;

import ast.AstException;
import ast.JVM;
import ast.Modifier;

import java.util.ArrayList;

public class IMethod extends IOverload {

    private final String name;
    private final JVM    type;

    private final Modifier modifier;

    private final ArrayList<JVM> arguments = new ArrayList<>();

    IClass parent;

    public IMethod(String name, JVM type, Modifier modifier) {
        this.name = name;
        this.type = type;
        this.modifier = modifier;
    }

    public void addArgument(JVM argument) {
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

    public boolean equals(ArrayList<JVM> arguments) throws AstException {
        if (this.arguments.size() != arguments.size())
            return false;

        for (int i = 0; i < arguments.size(); i++) {
            if (!this.arguments.get(i).equals(arguments.get(i)))
                return false;
        }
        return true;
    }

    public String getDescriptor() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (JVM argument: arguments)
            builder.append(argument.getInternalType());
        builder.append(')');
        builder.append(type.getInternalType());
        return builder.toString();
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
