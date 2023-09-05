package ast.util;

import ast.AstException;
import ast.Expression;
import ast.JVM;
import ast.compatibility.*;
import ast.var.Variable;

import java.util.ArrayList;
import java.util.Objects;

public class Overload {

    private Overload() {}

    private static IOverload getOverload(ArrayList<IOverload> overloads, JVM[] comparable) throws AstException {
        ArrayList<IOverload> indexes = new ArrayList<>();
        ArrayList<Integer> scaled = new ArrayList<>();

        for (IOverload overload : overloads) {
            JVM[] overload_arguments = overload.getArguments();
            skip:
            {
                int scale = 0;
                for (int j = 0; j < comparable.length; j++) {
                    if (!overload_arguments[j].equals(comparable[j]))
                        break skip;
                    scale += overload_arguments[j].getScale(comparable[j]);
                }
                indexes.add(overload);
                scaled.add(scale);
            }
        }

        if (indexes.size() == 0)
            return null;

        IOverload current_index = indexes.get(0);
        int current_scale = scaled.get(0);

        for (int i = 1; i < indexes.size(); i++) {
            if (current_scale > scaled.get(i)) {
                current_index = indexes.get(i);
                current_scale = scaled.get(i);
            }
        }

        return current_index;
    }

    public static IMethod getOverloadMethod(IClass clazz, JVM[] comparable, String name) throws AstException {
        ArrayList<IOverload> methods = new ArrayList<>();

        while (clazz != null) {
            for (IMethod method: clazz.getMethods()) {
                if (Objects.equals(method.getName(), name))
                    if (method.getSizeArguments() == comparable.length)
                        methods.add(method);
            }
            clazz = IList.get(clazz.getTypeSuper().getReference());
        }

        if (methods.size() == 0)
            return null;

        return (IMethod) getOverload(methods, comparable);
    }

    public static IConstructor getOverloadConstructor(IClass clazz, JVM[] comparable) throws AstException {
        ArrayList<IOverload> constructors = new ArrayList<>();

        for (IConstructor constructor: clazz.getConstructors())
            if (constructor.getSizeArguments() == comparable.length)
                constructors.add(constructor);

        if (constructors.size() == 0)
            return null;

        return (IConstructor) getOverload(constructors, comparable);
    }

    public static JVM[] toArray(ArrayList<Expression> values) throws AstException {
        JVM[] types = new JVM[values.size()];
        for (int i = 0; i < values.size(); i++)
            types[i] = values.get(i).getType();
        return types;
    }



    public static String getDescriptor(ArrayList<Variable> variables, JVM type) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (Variable variable: variables)
            builder.append(variable.getType().getInternalType());
        builder.append(')');
        builder.append(type.getInternalType());
        return builder.toString();
    }

}
