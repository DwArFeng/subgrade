package com.dwarfeng.subgrade.sdk.jdbc.definition;

import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.ConstraintDefinition;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Phoenix 工具类。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public final class PhoenixUtil {

    /**
     * 寻找数据表定义中的主键列定义。
     *
     * @param tableDefinition 指定的数据表定义。
     * @return 数据表定义中的主键列定义。
     */
    public static List<ColumnDefinition> findPrimaryKeyColumns(@NonNull TableDefinition tableDefinition) {
        ConstraintDefinition constraintDefinition = tableDefinition.getConstraintDefinitions().stream()
                .filter(def -> Objects.equals(def.getType(), PhoenixConstants.TYPE_PRIMARY_KEY)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("未能在数据表定义中找到任何主键，请确认该数据表定义中拥有" +
                        "类型为 " + PhoenixConstants.TYPE_PRIMARY_KEY + " 的约束"));
        if (Objects.isNull(constraintDefinition.getColumnDefinitions()) ||
                constraintDefinition.getColumnDefinitions().isEmpty()) {
            throw new IllegalArgumentException("数据表定义的主键约束中没有指定任何列");
        }
        return constraintDefinition.getColumnDefinitions();
    }
}
