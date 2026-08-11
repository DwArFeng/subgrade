package com.dwarfeng.subgrade.web.stack.response;

import com.dwarfeng.subgrade.basic.stack.bean.dto.Dto;

import java.io.Serial;

/**
 * 响应数据。
 *
 * @author DwArFeng
 * @since 0.1.3-beta
 */
public class ResponseData<T> implements Dto {

    @Serial
    private static final long serialVersionUID = -8257719800343857492L;

    private T data;
    private Meta meta;

    public ResponseData() {
    }

    public ResponseData(T data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }

    /**
     * 响应数据元数据。
     *
     * @author DwArFeng
     * @since 0.1.3-beta
     */
    public static class Meta implements Dto {

        @Serial
        private static final long serialVersionUID = -5781881402467280737L;

        private int code;
        private String message;

        public Meta() {
        }

        public Meta(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
