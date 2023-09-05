package ast.subject;

import ast.*;

public class Field {

    private final Modifier modifier;
    private final String name;
    private final JVM type;

    final Expression value;

    public Field(String name, Modifier modifier, JVM type, Expression value, Position position) throws AstException {
        this.modifier = modifier;
        this.name = name;

        if (!type.equals(value.getType()))
            throw new AstException("Несовпадение типов.", position);
        this.type = type;
        this.value = value;
    }

    void codegen(Class clazz) {
        clazz.cw.visitField(modifier.codegen(), name, type.getInternalType(), null, null);
    }

}
