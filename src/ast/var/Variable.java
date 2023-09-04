package ast.var;

import ast.JVM;
import ast.Position;

public class Variable extends Position {

    private final String name;
    private final JVM type;
    int id;

    public Variable(String name, JVM type, Position position) {
        super(position);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public JVM getType() {
        return type;
    }

    public int getIndex() {
        return id;
    }

}

