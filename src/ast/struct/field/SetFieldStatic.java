package ast.struct.field;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import ast.compatibility.IClass;
import ast.compatibility.IField;
import ast.compatibility.IList;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.PUTSTATIC;

public class SetFieldStatic extends Position implements Expression {

    private final IField field;
    private final Expression value;

    public SetFieldStatic(JVM type, String name, Expression value, Position position) throws AstException {
        super(position);

        if (!type.isReference())
            throw new AstException("Объектом для получения поля может быть только reference", this);

        IClass clazz = IList.get(type.getReference());
        if (clazz == null)
            throw new AstException("Класс \""+type.getValue()+"\" не найден!", this.getPosition());
        this.field = clazz.getField(name);
        if (this.field == null)
            throw new AstException("Статическое поле \""+name+"\" в классе \""+type.getValue()+"\" не найдено", this);

        if (!this.field.getType().equals(value.getType()))
            throw new AstException("Значение не соответсвует типу статического поля", this);
        this.value = value;
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        value.codegen(mv);
        mv.visitFieldInsn(PUTSTATIC, field.getParent().getType().getReference(), field.getName(), field.getType().getInternalType());
    }

    @Override
    public JVM getType() throws AstException {
        return field.getType();
    }

}