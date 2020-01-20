package com.dwarfeng.subgrade.stack.bean.key;

/**
 * 名称主键。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class NameKey implements Key {

    private static final long serialVersionUID = -8540019581843993464L;

    private String name;

    public NameKey() {
    }

    public NameKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NameKey{" +
                "name='" + name + '\'' +
                '}';
    }
}
