package ast;

import ast.compatibility.IClass;
import ast.compatibility.IList;

import java.util.Objects;

public class JVM {

    private final String value;

    private final int depth;

    private final Type type;

    public enum Type {
        VOID,       // V
        BOOLEAN,    // Z
        CHAR,       // C
        BYTE,       // B
        SHORT,      // S
        INT,        // I
        LONG,       // J
        FLOAT,      // F
        DOUBLE,     // D
        REFERENCE   // L...;
    }

    public JVM(Type type) throws AstException {
        this.value = switch (type) {
            case VOID -> "V";
            case BOOLEAN -> "Z";
            case CHAR -> "C";
            case BYTE -> "B";
            case SHORT -> "S";
            case INT -> "I";
            case LONG -> "J";
            case FLOAT -> "F";
            case DOUBLE -> "D";
            case REFERENCE -> throw new AstException("Тип данных reference не может быть пустым.");
        };
        this.type = type;
        this.depth = 0;
    }

    // classname format:      java.lang.String  ||  java/lang/String
    public JVM(String classname) {
        this.value = classname.replace('.', '/');
        this.type = Type.REFERENCE;
        this.depth = 0;
    }

    // classname format:      java.lang.String  ||  java/lang/String
    public JVM(String classname, int depth) {
        this.value = classname.replace('.', '/');
        this.type = Type.REFERENCE;
        this.depth = depth;
    }

    public JVM(Type type, int depth) throws AstException {
        this.value = switch (type) {
            case VOID -> throw new AstException("Массив не может иметь тип void");
            case BOOLEAN -> "Z";
            case CHAR -> "C";
            case BYTE -> "B";
            case SHORT -> "S";
            case INT -> "I";
            case LONG -> "J";
            case FLOAT -> "F";
            case DOUBLE -> "D";
            case REFERENCE -> throw new AstException("Тип данных reference не может быть пустым.");
        };
        this.type = type;
        this.depth = depth;
    }

    public JVM(JVM parent, int depth) {
        this.value = parent.value;
        this.type = parent.type;
        this.depth = depth;
    }

    public boolean isIntJVM() {
        return type == Type.CHAR || type == Type.BYTE || type == Type.SHORT || type == Type.INT;
    }

    public boolean isVoid() {
        return type == Type.VOID && !isArray();
    }

    public boolean isBoolean() {
        return type == Type.BOOLEAN && !isArray();
    }

    public boolean isChar() {
        return type == Type.CHAR && !isArray();
    }

    public boolean isByte() {
        return type == Type.BYTE && !isArray();
    }

    public boolean isShort() {
        return type == Type.SHORT && !isArray();
    }

    public boolean isInt() {
        return type == Type.INT && !isArray();
    }

    public boolean isLong() {
        return type == Type.LONG && !isArray();
    }

    public boolean isFloat() {
        return type == Type.FLOAT && !isArray();
    }

    public boolean isDouble() {
        return type == Type.DOUBLE && !isArray();
    }

    public boolean isNotNumber() {
        return !isIntJVM() && !isLong() && !isFloat() && !isDouble();
    }

    public boolean isReference() {
        return type == (Type.REFERENCE) && !isArray();
    }

    public boolean isArray() {
        return depth != 0;
    }

    public boolean isArrayIntJVM() {
        return isArray() && (type == Type.CHAR || type == Type.BYTE || type == Type.SHORT || type == Type.INT);
    }

    public boolean isArrayBoolean() {
        return type == Type.BOOLEAN && isArray();
    }

    public boolean isArrayChar() {
        return type == Type.CHAR && isArray();
    }

    public boolean isArrayByte() {
        return type == Type.BYTE && isArray();
    }

    public boolean isArrayShort() {
        return type == Type.SHORT && isArray();
    }

    public boolean isArrayInt() {
        return type == Type.INT && isArray();
    }

    public boolean isArrayLong() {
        return type == Type.LONG && isArray();
    }

    public boolean isArrayFloat() {
        return type == Type.FLOAT && isArray();
    }

    public boolean isArrayDouble() {
        return type == Type.DOUBLE && isArray();
    }

    public boolean isArrayReference() {
        return type == Type.REFERENCE && isArray();
    }

    public int getDepth() {
        return depth;
    }

    public String getInternalType() {
        if (isReference())
            return 'L' + value + ';';

        if (isArray()) {
            if (type == Type.REFERENCE)
                return "[".repeat(depth) + 'L' + value + ';';
            else
                return "[".repeat(depth) + value;
        }

        return value;
    }

    public String getReference() throws AstException {
        if (!isReference() && !isArrayReference())
            throw new AstException("getReference() не может быть вызван: нет reference");

        return value;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(JVM o) throws AstException {
        if (depth != o.depth)
            return false;

        if ((isReference() && o.isReference()) || (isArrayReference() && o.isArrayReference())) {
            IClass clazz = IList.get(o.getReference());

            while (clazz != null) {
                if (Objects.equals(getReference(), clazz.getType().getReference()))
                    return true;

                clazz = IList.get(clazz.getTypeSuper().getReference());
            }

            return false;
        }

        if (isIntJVM() && o.isIntJVM())
            return true;

        if (isArrayIntJVM() && o.isArrayIntJVM())
            return true;

        return Objects.equals(value, o.value);
    }

    public int getScale(JVM o) throws AstException {

        if (Objects.equals(value, o.value))
            return 0;

        int scale = 0;

        if ((isReference() && o.isReference()) || (isArrayReference() && o.isArrayReference())) {
            IClass clazz = IList.get(o.getReference());

            while (clazz != null) {
                if (Objects.equals(getReference(), clazz.getType().getReference()))
                    return scale;

                clazz = IList.get(clazz.getTypeSuper().getReference());
                scale++;
            }
        }

        return 1;
    }
}
