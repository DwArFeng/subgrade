package com.dwarfeng.subgrade.sdk.hibernate.modification;

import com.dwarfeng.subgrade.stack.bean.Bean;

import java.util.Collections;
import java.util.List;

/**
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public class DefaultDeletionMod<E extends Bean> implements DeletionMod<E> {

    @Override
    public List<Object> updateBeforeDelete(E element) {
        return Collections.emptyList();
    }
}
