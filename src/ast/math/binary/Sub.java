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

public class Sub extends MathBinary {

    // конструктор должен быть публичным
    public Sub(GetDataAst left, GetDataAst right, Position position) throws AstException {
        super(left, right, position);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        super.codegen(mv); // там же происходить генерация значений left right

        if (getType().isDouble())
            mv.visitInsn(DSUB);
        else if (getType().isFloat())
            mv.visitInsn(FSUB);
        else if (getType().isLong())
            mv.visitInsn(LSUB);
        else
            mv.visitInsn(ISUB);
    }

}
