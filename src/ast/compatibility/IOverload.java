package ast.compatibility;

import ast.JVM;

public abstract class IOverload {

    public abstract JVM[] getArguments();

    public abstract int getSizeArguments();

    public abstract IClass getParent();

}
