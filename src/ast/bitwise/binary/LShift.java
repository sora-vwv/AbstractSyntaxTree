package ast.bitwise.binary;

import ast.AstException;
import ast.GetDataAst;
import ast.JVM;
import ast.Position;
import ast.bitwise.BitwiseBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ISHL;
import static org.objectweb.asm.Opcodes.LSHL;

/*

@author Sora

 */

public class LShift extends BitwiseBinary {

    public LShift(GetDataAst left, GetDataAst right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        if (getType().isLong()) {
            left.codegen(mv);
            left.toLong(mv);
            right.codegen(mv);
            right.toInt(mv);
            mv.visitInsn(LSHL);
        } else {
            left.codegen(mv);
            right.codegen(mv);
            right.toInt(mv);
            mv.visitInsn(ISHL);
        }
    }

    @Override
    public JVM getType() throws AstException {
        if (left.getType().isLong())
            return new JVM(JVM.Type.LONG);
        return new JVM(JVM.Type.INT);
    }

}
