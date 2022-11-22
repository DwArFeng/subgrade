package com.dwarfeng.subgrade.impl.bean;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;

/**
 * 本征 BeanTransformer。
 *
 * <p>
 * 将对象本身映射为自己的 BeanTransformer。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class IdentityBeanTransformer<U extends Bean> implements BeanTransformer<U, U> {

    @Override
    public U transform(U u) {
        return u;
    }

    @Override
    public U reverseTransform(U u) {
        return u;
    }
}
