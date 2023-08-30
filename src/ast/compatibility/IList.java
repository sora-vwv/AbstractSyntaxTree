package ast.compatibility;

import ast.AstException;

import java.util.HashMap;

public class IList {

    private static final HashMap<String, IClass> classes = new HashMap<>();

    private IList() {}

    public static void add(IClass clazz) throws AstException {
        if (clazz == null)
            return;

        classes.put(clazz.getType().getReference(), clazz);
    }

    public static void clear() {
        classes.clear();
    }

    public static IClass get(String classname) {
        if (classes.containsKey(classname))
            return classes.get(classname);
        return null;
    }

}
