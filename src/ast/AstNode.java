package ast;

import org.objectweb.asm.MethodVisitor;

public interface AstNode {

    // метод, который генерирует байт-код JVM
    void codegen(MethodVisitor mv) throws AstException;

}
