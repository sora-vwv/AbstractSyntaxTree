package ast.struct.field;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import ast.compatibility.IClass;
import ast.compatibility.IField;
import ast.compatibility.IList;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GETFIELD;

public class GetField extends Position implements Expression {

    private final Expression root;
    private final IField field;

    public GetField(Expression root, String name, Position position) throws AstException {
        super(position);

        JVM type = root.getType();
        if (!type.isReference())
            throw new AstException("Объектом для получения поля может быть только reference", root.getPosition());
        this.root = root;

        IClass clazz = IList.get(type.getReference());
        if (clazz == null)
            throw new AstException("Класс \""+type.getValue()+"\" не найден!", this.getPosition());
        this.field = clazz.getField(name);
        if (this.field == null)
            throw new AstException("Поле \""+name+"\" в классе \""+type.getValue()+"\" не найдено", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        root.codegen(mv);
        mv.visitFieldInsn(GETFIELD, field.getParent().getType().getReference(), field.getName(), field.getType().getInternalType());
    }

    @Override
    public JVM getType() throws AstException {
        return field.getType();
    }

}
