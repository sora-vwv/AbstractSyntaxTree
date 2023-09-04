package ast.struct;

import org.objectweb.asm.Label;

import java.util.Stack;

public class BodyData {
    Stack<Label> points_break = new Stack<>();
    Stack<Label> points_continue = new Stack<>();
}
