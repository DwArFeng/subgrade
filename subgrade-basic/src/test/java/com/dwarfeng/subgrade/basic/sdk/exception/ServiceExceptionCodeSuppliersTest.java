package com.dwarfeng.subgrade.basic.sdk.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 基础模块异常代码测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionCodeSuppliersTest {

    @Test
    public void shouldApplyUpdatedOffsetToNewCodes() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            assertEquals(9130, ServiceExceptionCodeSuppliers.PAGING_FAILED.get().getCode());
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }
}
