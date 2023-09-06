package ast;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

// need extends Position
public interface Expression extends Statement {

    void codegen(MethodVisitor mv) throws AstException;

    JVM getType() throws AstException;

    default void toChar(MethodVisitor mv) throws AstException {
        toInt(mv);
        if (!getType().isChar())
            mv.visitInsn(I2C);
    }

    default void toByte(MethodVisitor mv) throws AstException {
        toInt(mv);
        if (!getType().isByte())
            mv.visitInsn(I2B);
    }

    default void toShort(MethodVisitor mv) throws AstException {
        toInt(mv);
        if (!getType().isShort())
            mv.visitInsn(I2S);
    }

    default void toInt(MethodVisitor mv) throws AstException {
        if (!getType().isIntJVM()) {
            if (getType().isLong())
                mv.visitInsn(L2I);
            else if (getType().isFloat())
                mv.visitInsn(F2I);
            else if (getType().isDouble())
                mv.visitInsn(D2I);
        }
    }

    default void toLong(MethodVisitor mv) throws AstException {
        if (!getType().isLong()) {
            if (getType().isIntJVM())
                mv.visitInsn(I2L);
            else if (getType().isFloat())
                mv.visitInsn(F2L);
            else if (getType().isDouble())
                mv.visitInsn(D2L);
        }
    }

    default void toFloat(MethodVisitor mv) throws AstException {
        if (!getType().isFloat()) {
            if (getType().isLong())
                mv.visitInsn(L2F);
            else if (getType().isIntJVM())
                mv.visitInsn(I2F);
            else if (getType().isDouble())
                mv.visitInsn(D2F);
        }
    }

    default void toDouble(MethodVisitor mv) throws AstException {
        if (!getType().isDouble()) {
            if (getType().isLong())
                mv.visitInsn(L2D);
            else if (getType().isFloat())
                mv.visitInsn(F2D);
            else if (getType().isIntJVM())
                mv.visitInsn(I2D);
        }
    }

    default Position getPosition() {
        if (this instanceof Position)
            return (Position) this;
        return new Position(-1, -1, null, null);
    }

}
