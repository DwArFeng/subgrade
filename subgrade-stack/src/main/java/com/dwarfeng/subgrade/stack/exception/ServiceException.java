package com.dwarfeng.subgrade.stack.exception;

import java.io.Serializable;
import java.util.Objects;

/**
 * 服务端异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 5605302745825108621L;

    private Code code;

    public ServiceException() {
        this.code = null;
    }

    public ServiceException(Code code) {
        this.code = code;
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.code = null;
    }

    public ServiceException(Code code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        if (Objects.isNull(code)) {
            return super.getMessage();
        } else {
            return "exception code=" + code.getCode() + " - " + code.getTip();
        }
    }

    public static class Code implements Serializable {

        private static final long serialVersionUID = -7378017610648648075L;

        private int code;
        private String tip;

        public Code() {
        }

        public Code(int code, String tip) {
            this.code = code;
            this.tip = tip;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        @Override
        public String toString() {
            return "Code{" +
                    "code=" + code +
                    ", tip='" + tip + '\'' +
                    '}';
        }
    }
}
