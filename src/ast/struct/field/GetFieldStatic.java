package ast.struct.field;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.Position;
import ast.compatibility.IClass;
import ast.compatibility.IField;
import ast.compatibility.IList;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GETSTATIC;

public class GetFieldStatic extends Position implements Expression {

    private final IField field;

    public GetFieldStatic(JVM type, String name, Position position) throws AstException {
        super(position);

        if (!type.isReference())
            throw new AstException("Объектом для получения статического поля может быть только reference", this);

        IClass clazz = IList.get(type.getReference());
        if (clazz == null)
            throw new AstException("Класс \""+type.getValue()+"\" не найден!", this);
        this.field = clazz.getField(name);
        if (this.field == null)
            throw new AstException("Статическое поле \""+name+"\" в классе \""+type.getValue()+"\" не найдено", this);
    }

    @Override
    public void codegen(MethodVisitor mv) throws AstException {
        mv.visitFieldInsn(GETSTATIC, field.getParent().getType().getReference(), field.getName(), field.getType().getInternalType());
    }

    @Override
    public JVM getType() throws AstException {
        return field.getType();
    }

}
