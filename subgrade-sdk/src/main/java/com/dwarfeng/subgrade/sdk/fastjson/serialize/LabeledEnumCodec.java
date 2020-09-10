package com.dwarfeng.subgrade.sdk.fastjson.serialize;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.dwarfeng.subgrade.sdk.enumeration.LabeledEnum;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * LabeledEnum 转换器。
 *
 * <p>
 * 注意，该类是一个抽象类，其子类必须实现无参数构造器方法。
 *
 * @author DwArFeng
 * @since 1.1.5
 */
public abstract class LabeledEnumCodec<E extends Enum<E> & LabeledEnum> implements ObjectSerializer, ObjectDeserializer {

    private final Class<? extends E> clazz;

    public LabeledEnumCodec(Class<? extends E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        E val = clazz.cast(object);
        out.writeString(val.getLabel());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.getLexer();

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        String fileTypeString = lexer.stringVal();
        for (E enumConstant : clazz.getEnumConstants()) {
            if (Objects.equals(fileTypeString, enumConstant.getLabel())) {
                lexer.nextToken(JSONToken.COMMA);
                return (T) enumConstant;
            }
        }
        throw new IllegalStateException("枚举类 " + clazz.getCanonicalName() + " 中找不到标签为 " + fileTypeString + " 的值");
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
