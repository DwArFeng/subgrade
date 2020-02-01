package com.dwarfeng.subgrade.sdk.hibernate.modification;

import java.util.Collections;
import java.util.List;

/**
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public class DefaultDeletion<E> implements Deletion<E> {

    @Override
    public List<Object> updateBeforeDelete(E element) {
        return Collections.emptyList();
    }
}
