package ast.logic.binary;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import ast.logic.LogicBinary;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Equal extends LogicBinary {

    public Equal(GetData left, GetData right, Position position) throws AstException {
        super(left, right, position);
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
            mv.visitJumpInsn(IFNE, L1);
        } else if(priority.isFloat()) {
            mv.visitInsn(FCMPG);
            mv.visitJumpInsn(IFNE, L1);
        } else if (priority.isLong()) {
            mv.visitInsn(LCMP);
            mv.visitJumpInsn(IFNE, L1);
        } else if (priority.isIntJVM()) {
            mv.visitJumpInsn(IF_ICMPNE, L1);
        } else if (priority.isBoolean()) {
            mv.visitJumpInsn(IF_ICMPNE, L1);
        }

        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, L2);
        mv.visitLabel(L1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(L2);
    }

}
