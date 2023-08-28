package ast.bitwise.binary;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import ast.bitwise.BitwiseBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class And extends BitwiseBinary {

    public And(GetData left, GetData right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        if (getType().isLong()) {
            left.codegen(mv);
            left.toLong(mv);
            right.codegen(mv);
            right.toLong(mv);
            mv.visitInsn(LAND);
        } else {
            left.codegen(mv);
            right.codegen(mv);
            mv.visitInsn(IAND);
        }
    }

    @Override
    public JVM getType() throws AstException {
        if (left.getType().isLong() || right.getType().isLong())
            return new JVM(JVM.Type.LONG);
        return new JVM(JVM.Type.INT);
    }

}
