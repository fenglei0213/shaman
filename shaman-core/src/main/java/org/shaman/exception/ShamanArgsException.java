package org.shaman.exception;

/**
 * Created by fenglei on 2016/11/28.
 */
public class ShamanArgsException extends RuntimeException {

    public ShamanArgsException() {
    }

    public ShamanArgsException(String msg) {
        super(msg);
    }

    public ShamanArgsException(String msg, Throwable e) {
        super(msg, e);
    }
}
