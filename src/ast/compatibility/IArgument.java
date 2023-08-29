package ast.compatibility;

import ast.JVM;

public class IArgument {

    private final JVM type;

    public IArgument(JVM type) {
        this.type = type;
    }

    public JVM getType() {
        return type;
    }

}
