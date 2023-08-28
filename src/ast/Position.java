package ast;

/*
Класс, от которого так или иначе наследуются все узлы дерева.
 */

public class Position {

    // координаты в тексте, на которых возникла ошибка
    private final int row;
    private final int column;

    protected Position(Position pos) {
        this.row = pos.row;
        this.column = pos.column;
    }

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
