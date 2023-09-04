package ast;

import static org.objectweb.asm.Opcodes.*;

public class Modifier {

    public enum Access {
        PUBLIC,
        PRIVATE,
        PROTECTED,
        PACKAGE_PRIVATE
    }

    private Access access;

    private boolean is_static;
    private boolean is_final;
    private boolean is_abstract;

    public Modifier() {
        access = Access.PACKAGE_PRIVATE;
        is_static = false;
        is_final = false;
        is_abstract = false;
    }

    public Modifier(Access access) {
        this.access = access;
        is_static = false;
        is_final = false;
        is_abstract = false;
    }

    public Modifier(Access access, boolean is_static, boolean is_final) {
        this.access = access;
        this.is_static = is_static;
        this.is_final = is_final;
    }

    public Modifier(Access access, boolean is_static, boolean is_final, boolean is_abstract) {
        this.access = access;
        this.is_static = is_static;
        this.is_final = is_final;
        this.is_abstract = is_abstract;
    }

    public int codegen() {
        int modifier = 0;

        modifier += switch (access) {
            case PUBLIC -> ACC_PUBLIC;
            case PRIVATE -> ACC_PRIVATE;
            case PROTECTED -> ACC_PROTECTED;
            case PACKAGE_PRIVATE -> 0;
        };

        if (is_static)
            modifier += ACC_STATIC;

        if (is_final)
            modifier += ACC_FINAL;

        if (is_abstract)
            modifier += ACC_ABSTRACT;

        return modifier;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public boolean isPublic() {
        return access == Access.PUBLIC;
    }

    public boolean isPrivate() {
        return access == Access.PRIVATE;
    }

    public boolean isProtected() {
        return access == Access.PRIVATE;
    }

    public boolean isPackagePrivate() {
        return access == Access.PACKAGE_PRIVATE;
    }

    public boolean isStatic() {
        return is_static;
    }

    public boolean isFinal() {
        return is_final;
    }

    public boolean isAbstract() {
        return is_abstract;
    }

    public void setStatic(boolean is_static) {
        this.is_static = is_static;
    }

    public void setFinal(boolean is_final) {
        this.is_final = is_final;
    }

    public void setAbstract(boolean is_abstract) {
        this.is_abstract = is_abstract;
    }

}
