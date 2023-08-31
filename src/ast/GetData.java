package ast;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Интерфейс для узлов дерева, которые оставляют в стеке значений JVM какое-либо
значение: от математически операций до вызова методов и создания новых reference.

Преобразование типов данных:
Opcodes.I2D из библиотеки можно расшифровать как "int to double"
Последнее на стеке значений число int преобразуется в double
 */

// need extends Position
public interface GetData extends AstNode {

    // метод, который генерирует байт-код JVM
    void codegen(MethodVisitor mv) throws AstException;

    // нужно иметь возможность получения типа значения, которое мы оставляем в стеке
    JVM getType() throws AstException;

    // преобразование в разные типы данных

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
