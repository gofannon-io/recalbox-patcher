package org.recalbox.confpatcher;

@SuppressWarnings("serial")
public class TooManyBackupFileException extends RuntimeException {

    public TooManyBackupFileException() {
    }

    public TooManyBackupFileException(String message) {
        super(message);
    }

    public TooManyBackupFileException(Throwable cause) {
        super(cause);
    }

    public TooManyBackupFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyBackupFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
