package ast;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public interface Statement {

    // метод, который генерирует байт-код JVM
    void codegen(MethodVisitor mv) throws AstException;

    Label label_start = new Label();
    Label label_end = new Label();

    default void codegenStart(MethodVisitor mv) {
        mv.visitLabel(label_start);
    }

    default void codegenEnd(MethodVisitor mv) {
        mv.visitLabel(label_end);
    }

    default void codegenLine(MethodVisitor mv) {
        if (this instanceof Position)
            mv.visitLineNumber(((Position)this).getRow(), label_start);
    }

}
