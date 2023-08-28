package ast.cast;

import ast.AstException;
import ast.GetData;
import ast.JVM;
import ast.Position;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.INSTANCEOF;

public class InstanceOf extends Position implements GetData {

    private final GetData left;
    private final JVM right;

    public InstanceOf(GetData left, JVM right, Position position) throws AstException {
        super(position);

        this.left = left;
        if (!left.getType().isReference())
            throw new AstException("instanceof обрабатывает объект в качестве первого параметра.", this);

        this.right = right;
        if (!right.isReference())
            throw new AstException("instanceof обрабатывает reference в качестве второго параметра.", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        left.codegen(mv);
        mv.visitTypeInsn(INSTANCEOF, right.getReference());
    }

    @Override
    public JVM getType() throws AstException {
        return new JVM(JVM.Type.BOOLEAN);
    }

}
