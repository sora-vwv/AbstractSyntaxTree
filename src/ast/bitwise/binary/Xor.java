package ast.bitwise.binary;

import ast.AstException;
import ast.GetDataAst;
import ast.JVM;
import ast.Position;
import ast.bitwise.BitwiseBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*

@author Sora

 */

public class Xor extends BitwiseBinary {

    public Xor(GetDataAst left, GetDataAst right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        if (getType().isLong()) {
            left.codegen(mv);
            left.toLong(mv);
            right.codegen(mv);
            right.toLong(mv);
            mv.visitInsn(LXOR);
        } else {
            left.codegen(mv);
            right.codegen(mv);
            mv.visitInsn(IXOR);
        }
    }

    @Override
    public JVM getType() throws AstException {
        if (left.getType().isLong() || right.getType().isLong())
            return new JVM(JVM.Type.LONG);
        return new JVM(JVM.Type.INT);
    }

}
