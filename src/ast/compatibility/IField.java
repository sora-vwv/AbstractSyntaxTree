package ast.compatibility;

import ast.JVM;
import ast.Modifier;

public class IField {

    private final String name;
    private final JVM    type;

    private final Modifier modifier;

    public IField(String name, JVM type, Modifier modifier) {
        this.name = name;
        this.type = type;
        this.modifier = modifier;
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
