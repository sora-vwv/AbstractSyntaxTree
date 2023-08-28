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

public class Less extends LogicBinary {

    public Less(GetDataAst left, GetDataAst right, Position position) throws AstException {
        super(left, right, position);

        if (left.getType().isBoolean() || right.getType().isBoolean())
            throw new AstException("Бинарный логический оператор less не обрабатывает boolean.");
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {

        JVM priority = getPriorityType();

        Label L1 = new Label();
        Label L2 = new Label();

        codegenLeft(mv);
        codegenRight(mv);

        if (priority.isDouble()) {
            mv.visitInsn(DCMPG);
            mv.visitJumpInsn(IFGE, L1);
        } else if(priority.isFloat()) {
            mv.visitInsn(FCMPG);
            mv.visitJumpInsn(IFGE, L1);
        } else if (priority.isLong()) {
            mv.visitInsn(LCMP);
            mv.visitJumpInsn(IFGE, L1);
        } else if (priority.isIntJVM()) {
            mv.visitJumpInsn(IF_ICMPGE, L1);
        }

        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, L2);
        mv.visitLabel(L1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(L2);
    }

}
