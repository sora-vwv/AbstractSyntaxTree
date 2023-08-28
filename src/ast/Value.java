package ast;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

public class Value implements GetData {

    private boolean value_boolean;
    private int     value_int;
    private long    value_long;
    private float   value_float;
    private double  value_double;
    private String  value_string;

    private final JVM type;

    public Value(boolean value) throws AstException {
        value_boolean = value;
        type = new JVM(JVM.Type.BOOLEAN);
    }

    public Value(char value) throws AstException {
        value_int = value;
        type = new JVM(JVM.Type.CHAR);
    }

    public Value(int value) throws AstException {
        value_int = value;
        type = new JVM(JVM.Type.INT);
    }

    public Value(long value) throws AstException {
        value_long = value;
        type = new JVM(JVM.Type.LONG);
    }

    public Value(float value) throws AstException {
        value_float = value;
        type = new JVM(JVM.Type.FLOAT);
    }

    public Value(double value) throws AstException {
        value_double = value;
        type = new JVM(JVM.Type.DOUBLE);
    }

    public Value(String value) {
        value_string = value;
        type = new JVM("java/lang/String");
    }

    public Value() throws AstException {
        type = new JVM(JVM.Type.VOID);
    }

    @Override
    public void codegen(MethodVisitor mv) {
        if (type.isVoid())
            mv.visitInsn(Opcodes.ACONST_NULL);
        else if (type.isBoolean()) {
            if (value_boolean)
                mv.visitInsn(ICONST_1);
            else
                mv.visitInsn(ICONST_0);
        } else if (type.isIntJVM())
            codegen(mv, value_int);
        else if (type.isLong()) {
            if (value_long == 0) mv.visitInsn(LCONST_0);
            else if (value_long == 1) mv.visitInsn(LCONST_1);
            else mv.visitLdcInsn(value_long);
        } else if (type.isFloat()) {
            if (value_float == 0) mv.visitInsn(FCONST_0);
            else if (value_float == 1) mv.visitInsn(FCONST_1);
            else if (value_float == 2) mv.visitInsn(FCONST_2);
            else mv.visitLdcInsn(value_float);
        } else if (type.isDouble()) {
            if (value_double == 0) mv.visitInsn(DCONST_0);
            else if (value_double == 1) mv.visitInsn(DCONST_1);
            else mv.visitLdcInsn(value_double);
        } else if (type.isReference())
            mv.visitLdcInsn(value_string);
    }

    private void codegen(MethodVisitor mv, int value) {
        if      (value == -1)     mv.visitInsn(ICONST_M1);
        else if (value == 0)      mv.visitInsn(ICONST_0);
        else if (value == 1)      mv.visitInsn(ICONST_1);
        else if (value == 2)      mv.visitInsn(ICONST_2);
        else if (value == 3)      mv.visitInsn(ICONST_3);
        else if (value == 4)      mv.visitInsn(ICONST_4);
        else if (value == 5)      mv.visitInsn(ICONST_5);
        else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE)   mv.visitIntInsn(BIPUSH, value);
        else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) mv.visitIntInsn(SIPUSH, value);
        else mv.visitIntInsn(LDC, value);
    }

    @Override
    public JVM getType() throws AstException {
        return type;
    }

}
