package com.dwarfeng.subgrade.sdk.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.stack.bean.dto.ResponseData;
import com.dwarfeng.subgrade.stack.bean.dto.ResponseData.Meta;

import javax.annotation.Nonnull;

/**
 * 适用于 FastJson 的 ResponseData。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public class FastJsonResponseData<T> implements Dto {

    private static final long serialVersionUID = -179333622113191439L;

    /**
     * 通过指定的 ResponseData 生成的 FastJsonPagedData。
     *
     * @param responseData 指定的 responseData。
     * @param <T>          ResponseData 中的元素的类型。
     * @return 生成的 FastJsonPagedData。
     */
    public static <T> FastJsonResponseData<T> of(@Nonnull ResponseData<T> responseData) {
        return new FastJsonResponseData<>(
                responseData.getData(),
                FastJsonMeta.of(responseData.getMeta())
        );
    }

    private T data;
    private FastJsonMeta meta;

    public FastJsonResponseData() {
    }

    public FastJsonResponseData(T data, FastJsonMeta meta) {
        this.data = data;
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public FastJsonMeta getMeta() {
        return meta;
    }

    public void setMeta(FastJsonMeta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "FastJsonResponseData{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }

    /**
     * 适用于 FastJson 的 Meta。
     *
     * @author DwArFeng
     * @since 0.2.4-beta
     */
    public static class FastJsonMeta implements Dto {

        private static final long serialVersionUID = -4026795378227479493L;

        /**
         * 通过指定的 Meta 生成的 FastJsonMeta。
         *
         * @param meta 指定的 PagedData。
         * @return 生成的 FastJsonMeta。
         */
        public static FastJsonMeta of(@Nonnull Meta meta) {
            return new FastJsonMeta(
                    meta.getCode(),
                    meta.getMessage()
            );
        }

        @JSONField(name = "code", ordinal = 1)
        private int code;

        @JSONField(name = "message", ordinal = 2)
        private String message;

        public FastJsonMeta() {
        }

        public FastJsonMeta(int code, String message) {
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
            return "FastJsonMeta{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
