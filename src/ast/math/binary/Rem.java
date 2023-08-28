package ast.math.binary;

import ast.AstException;
import ast.GetDataAst;
import ast.Position;
import ast.math.MathBinary;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*

@author Sora

*/

public class Rem extends MathBinary {

    public Rem(GetDataAst left, GetDataAst right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv);

        if (getType().isDouble())
            mv.visitInsn(DREM);
        else if (getType().isFloat())
            mv.visitInsn(FREM);
        else if (getType().isLong())
            mv.visitInsn(LREM);
        else
            mv.visitInsn(IREM);
    }

}
