package ast.math.binary;

import ast.AstException;
import ast.GetData;
import ast.Position;
import ast.math.MathBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*

@author Sora

*/

public class Mul extends MathBinary {

    public Mul(GetData left, GetData right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv);

        if (getType().isDouble())
            mv.visitInsn(DMUL);
        else if (getType().isFloat())
            mv.visitInsn(FMUL);
        else if (getType().isLong())
            mv.visitInsn(LMUL);
        else
            mv.visitInsn(IMUL);
    }

}
