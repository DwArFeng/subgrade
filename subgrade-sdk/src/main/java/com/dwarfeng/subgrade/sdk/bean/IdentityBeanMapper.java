package com.dwarfeng.subgrade.sdk.bean;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanMapper;

/**
 * 本征 BeanMapper。
 * <p>将对象本身映射为自己的 BeanMapper</p>
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class IdentityBeanMapper<U extends Bean> implements BeanMapper<U, U> {

    @Override
    public U transform(U u) {
        return u;
    }

    @Override
    public U reverseTransform(U u) {
        return u;
    }
}
