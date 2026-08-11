package com.dwarfeng.subgrade.basic.stack.exception;

import com.dwarfeng.subgrade.basic.internal.i18n.BasicMessageKey;
import com.dwarfeng.subgrade.basic.internal.i18n.BasicMessages;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 服务端异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ServiceException extends Exception {

    @Serial
    private static final long serialVersionUID = 8794331092705846344L;

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
            return BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_MESSAGE, code.getCode(), code.getTip());
        }
    }

    public static class Code implements Serializable {

        @Serial
        private static final long serialVersionUID = -1917000875093422236L;

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
                    ", tip='" + getTip() + '\'' +
                    '}';
        }
    }
}
