package ast;

/*
Класс, от которого так или иначе наследуются все узлы дерева.
 */

public class Position {

    // координаты в тексте, на которых возникла ошибка
    private final int row;
    private final int column;
    private final String filename;
    private final String line;

    protected Position(Position pos) {
        this.row = pos.row;
        this.column = pos.column;
        this.filename = pos.filename;
        this.line = pos.line;
    }

    public Position(int row, int column, String filename, String line) {
        this.row = row;
        this.column = column;
        this.filename = filename;
        this.line = line;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getFileName() {
        return filename;
    }

    public String getLine() {
        return line;
    }

}
