package ast.util;

import ast.AstException;
import ast.JVM;
import ast.Modifier;
import ast.compatibility.*;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static java.lang.reflect.Modifier.*;

// !!!!!!! Этот файл не трогай. Код здесь не очень

public class JarParser {

    public ArrayList<String> getClassNamesFromJar(String jarPath) throws Exception {
        ArrayList<String> classNames = new ArrayList<>();
        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath));
            JarEntry jar;
            while ((jar = jarFile.getNextJarEntry()) != null) {
                if ((jar.getName().endsWith(".class"))) {
                    String className = jar.getName().replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    classNames.add(myClass);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error while getting class names from jar", e);
        }
        return classNames;
    }

    public ArrayList<IClass> parseJar(String filePath) throws Exception {
        ArrayList<IClass> classes = new ArrayList<>();

        ArrayList<String> classNames = getClassNamesFromJar(filePath);
        URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(filePath).toURI().toURL()});
        for (String className : classNames)
            try { classes.add(parse(classLoader.loadClass(className)));
            } catch (Throwable ignored) {}
        classLoader.close();

        return classes;
    }

    private IClass parse(Class<?> clazz) throws AstException {
        IClass iClass = new IClass(
                new JVM(clazz.getName()),
                new JVM(clazz.getSuperclass().getName()),
                parse(clazz.getModifiers())
        );

        for (Constructor<?> constructor: clazz.getDeclaredConstructors())
            iClass.addConstructor(parse(constructor));

        for (Method method: clazz.getDeclaredMethods())
            iClass.addMethod(parse(method));

        for (Field field: clazz.getDeclaredFields())
            iClass.addField(parse(field));

        return iClass;
    }

    private Modifier parse(int mod) {
        boolean is_static   = isStatic(mod);
        boolean is_final    = isFinal(mod);
        boolean is_abstract = isAbstract(mod);

        Modifier.Access access =
                isPublic(mod) ? Modifier.Access.PUBLIC :
                        isPrivate(mod) ? Modifier.Access.PRIVATE :
                                isProtected(mod) ? Modifier.Access.PRIVATE :
                                        Modifier.Access.PACKAGE_PRIVATE;

        return new Modifier(access, is_static, is_final, is_abstract);
    }

    private IConstructor parse(Constructor<?> constructor) throws AstException {
        IConstructor iconstructor = new IConstructor(
                parse(constructor.getModifiers())
        );

        for (Class<?> type: constructor.getParameterTypes())
            iconstructor.addArgument(new IArgument(
                    parse(type.descriptorString())
            ));

        return iconstructor;
    }

    private IMethod parse(Method method) throws AstException {
        IMethod imethod = new IMethod(
                method.getName(),
                parse(method.getReturnType().descriptorString()),
                parse(method.getModifiers())
        );

        for (Class<?> type: method.getParameterTypes())
            imethod.addArgument(new IArgument(
                    parse(type.descriptorString())
            ));

        return imethod;
    }

    private IField parse(Field field) throws AstException {
        return new IField(
                field.getName(),
                parse(field.getType().descriptorString()),
                parse(field.getModifiers())
        );
    }

    private JVM parse(String value) throws AstException {
        String type_value = value.replace("[", "");
        int depth = value.length() - type_value.length();

        return switch (type_value.charAt(0)) {
            case 'V' -> new JVM(JVM.Type.VOID);
            case 'Z' -> new JVM(JVM.Type.BOOLEAN, depth);
            case 'C' -> new JVM(JVM.Type.CHAR,    depth);
            case 'B' -> new JVM(JVM.Type.BYTE,    depth);
            case 'S' -> new JVM(JVM.Type.SHORT,   depth);
            case 'I' -> new JVM(JVM.Type.INT,     depth);
            case 'J' -> new JVM(JVM.Type.LONG,    depth);
            case 'F' -> new JVM(JVM.Type.FLOAT,   depth);
            case 'D' -> new JVM(JVM.Type.DOUBLE,  depth);
            default -> new JVM(type_value.substring(1, type_value.length()-1), depth);
        };
    }

}
