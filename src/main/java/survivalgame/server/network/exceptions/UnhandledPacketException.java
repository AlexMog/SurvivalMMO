package survivalgame.server.network.exceptions;

@SuppressWarnings("serial")
public class UnhandledPacketException extends RuntimeException {

    public UnhandledPacketException() {
    }

    public UnhandledPacketException(String message) {
        super(message);
    }

    public UnhandledPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnhandledPacketException(Throwable cause) {
        super(cause);
    }

    public UnhandledPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
