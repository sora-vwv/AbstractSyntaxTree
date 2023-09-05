package ast.subject;

import ast.AstException;
import ast.JVM;
import ast.Modifier;
import ast.Position;
import ast.struct.Body;
import ast.util.Overload;
import ast.var.Variable;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.*;

public class Method {
    private final static JVM necessary = new JVM("java.lang.Throwable");

    private final Body body;

    final String name;
    final Modifier modifier;
    final JVM type;
    final ArrayList<Variable> arguments;
    final ArrayList<String> throwable;

    public Method(String name, JVM type, Modifier modifier) {
        this.name = name;
        this.type = type;

        body = new Body();

        this.modifier = modifier;
        arguments = new ArrayList<>();
        throwable = new ArrayList<>();
    }

    void codegen(Class clazz) throws AstException {
        MethodVisitor mv = clazz.cw.visitMethod(modifier.codegen(), name, Overload.getDescriptor(arguments, new JVM(JVM.Type.VOID)), null, throwable.toArray(new String[0]));
        body.codegen(mv);
        if (type.isVoid() && !body.isLastReturn())
            mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void addThrowable(JVM type, Position position) throws AstException {
        if (!necessary.equals(type))
            throw new AstException("Тип значения для throws не подходит.", position);
        throwable.add(type.getReference());
    }

    public void addArgument(Variable variable) throws AstException {
        arguments.add(variable);
        body.getCounter().add(variable);
    }

    public Body getBody() {
        return body;
    }

}