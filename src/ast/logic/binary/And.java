package ast.logic.binary;

import ast.AstException;
import ast.GetDataAst;
import ast.JVM;
import ast.Position;
import ast.logic.LogicBinary;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*

@author Sora

 */

public class And extends LogicBinary {

    public And(GetDataAst left, GetDataAst right, Position position) throws AstException {
        super(left, right, position);

        if (!left.getType().isBoolean() || !right.getType().isBoolean())
            throw new AstException("Бинарный логический оператор and принимает только boolean.");
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        Label L1 = new Label();
        Label L2 = new Label();

        codegenLeft(mv);
        mv.visitJumpInsn(IFEQ, L1);
        codegenRight(mv);
        mv.visitJumpInsn(IFEQ, L1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, L2);
        mv.visitLabel(L1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(L2);
    }

}
