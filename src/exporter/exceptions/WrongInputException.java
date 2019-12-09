package exporter.exceptions;

public final class WrongInputException extends Exception{
    @Override
    public String getMessage() {
        return "Wrong input, try another time";
    }
}
