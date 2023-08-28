package ast;

/*

@author Sora

любые исключения для дебага и вывода ошибок компилятора в будующем - это AstException
 */

public class AstException extends Exception {

    // координаты в тексте, на которых возникла ошибка
    public final Position position;

    public AstException(String message) {
        super(message);

        position = new Position(-1, -1);
    }

    public AstException(String message, Position position) {
        super(message);

        this.position = position;
    }

}
