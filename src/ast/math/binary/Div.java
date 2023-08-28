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

public class Div extends MathBinary {

    public Div(GetDataAst left, GetDataAst right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv);

        if (getType().isDouble())
            mv.visitInsn(DDIV);
        else if (getType().isFloat())
            mv.visitInsn(FDIV);
        else if (getType().isLong())
            mv.visitInsn(LDIV);
        else
            mv.visitInsn(IDIV);
    }

}
