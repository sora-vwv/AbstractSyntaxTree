package ast.var;

import ast.AstException;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

public class LocalVariableCounter {

    private int current_id;
    private final LocalVariableCounter parent;
    private final ArrayList<Variable> vars = new ArrayList<>();

    public LocalVariableCounter(LocalVariableCounter parent) {
        this.parent = parent;
        current_id = parent.current_id;
    }

    public LocalVariableCounter() {
        parent = null;
        current_id = 0;
    }

    public boolean contains(String name) {
        for(Variable var: vars)
            if(var.getName().equals(name)) return true;

        return parent != null && parent.contains(name);
    }

    public Variable get(String name) {
        for(Variable var: vars)
            if(var.getName().equals(name)) return var;

        if(parent == null) return null;
        return parent.get(name);
    }

    public void add(Variable var) throws AstException {
        if (contains(var.getName()))
            throw new AstException("Переменная не может быть объявлена дважды.", var);

        var.id = current_id++;
        vars.add(var);
    }

    public void codegenVariables(MethodVisitor mv, Label label_start, Label label_end) {
        for (Variable var: vars)
            mv.visitLocalVariable(var.getName(), var.getType().getInternalType(), null, label_start, label_end, var.id);
    }

}
