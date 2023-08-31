package ast;

/*
любые исключения для дебага и вывода ошибок компилятора в будующем - это AstException
*/

public class AstException extends Exception {

    // координаты в тексте, на которых возникла ошибка
    public final Position position;

    public AstException(String message) {
        super(message);

        position = new Position(-1, -1, null, null);
    }

    public AstException(String message, Position position) {
        super(message);

        this.position = position;
    }

    @Override
    public String toString() {
        if (position.getRow() == -1) return "[ERROR]";

        String whitespaces = " ".repeat(String.valueOf(position.getRow()).length() + 1);
        String format = """
                %s[ERROR]: %s
                                
                %s╭⎯⎯⎯%s:%d:%d⎯⎯⎯⎯⎯
                %d │ %s
                %s│%s╰ %s""";
        return format.formatted(
                whitespaces, getMessage(),

                whitespaces, position.getFileName(), position.getRow(), position.getColumn(),
                position.getRow(), position.getLine(),
                whitespaces, " ".repeat(position.getColumn()), getMessage()
        );
    }

}
