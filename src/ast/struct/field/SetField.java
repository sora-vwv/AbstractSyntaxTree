package ast.struct.field;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import ast.compatibility.IClass;
import ast.compatibility.IField;
import ast.compatibility.IList;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.PUTFIELD;

public class SetField extends Position implements Expression {

    private final Expression root;
    private final IField field;
    private final Expression value;

    public SetField(Expression root, String name, Expression value, Position position) throws AstException {
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

        if (!this.field.getType().equals(value.getType()))
            throw new AstException("Значение не соответсвует типу поля", this);
        this.value = value;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        root.codegen(mv);
        value.codegen(mv);
        mv.visitFieldInsn(PUTFIELD, field.getParent().getType().getReference(), field.getName(), field.getType().getInternalType());
    }

    @Override
    public JVM getType() throws AstException {
        return field.getType();
    }

}