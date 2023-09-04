package ast.bitwise.binary;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import ast.bitwise.BitwiseBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Or extends BitwiseBinary {

    public Or(Expression left, Expression right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        if (getType().isLong()) {
            left.codegen(mv);
            left.toLong(mv);
            right.codegen(mv);
            right.toLong(mv);
            mv.visitInsn(LOR);
        } else {
            left.codegen(mv);
            right.codegen(mv);
            mv.visitInsn(IOR);
        }
    }

    @Override
    public JVM getType() throws AstException {
        if (left.getType().isLong() || right.getType().isLong())
            return new JVM(JVM.Type.LONG);
        return new JVM(JVM.Type.INT);
    }

}
