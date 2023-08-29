package ast.logic.binary;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import ast.logic.LogicBinary;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class MoreEqual extends LogicBinary {

    public MoreEqual(GetData left, GetData right, Position position) throws AstException {
        super(left, right, position);

        if (left.getType().isBoolean() || right.getType().isBoolean())
            throw new AstException("Бинарный логический оператор more equal не обрабатывает boolean.", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {

        JVM priority = getPriorityType();

        Label L1 = new Label();
        Label L2 = new Label();

        codegenLeft(mv);
        codegenRight(mv);

        if (priority.isDouble()) {
            mv.visitInsn(DCMPL);
            mv.visitJumpInsn(IFLT, L1);
        } else if(priority.isFloat()) {
            mv.visitInsn(FCMPL);
            mv.visitJumpInsn(IFLT, L1);
        } else if (priority.isLong()) {
            mv.visitInsn(LCMP);
            mv.visitJumpInsn(IFLT, L1);
        } else if (priority.isIntJVM()) {
            mv.visitJumpInsn(IF_ICMPLT, L1);
        }

        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, L2);
        mv.visitLabel(L1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(L2);
    }

}
