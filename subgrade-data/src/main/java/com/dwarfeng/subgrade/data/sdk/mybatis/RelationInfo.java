package com.dwarfeng.subgrade.data.sdk.mybatis;

import com.dwarfeng.subgrade.basic.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

import java.io.Serial;

/**
 * 关系信息。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class RelationInfo<PK extends Key, CK extends Key> implements Dto {

    @Serial
    private static final long serialVersionUID = -6935349607628688367L;

    private PK pk;
    private CK ck;

    public RelationInfo() {
    }

    public RelationInfo(PK pk, CK ck) {
        this.pk = pk;
        this.ck = ck;
    }

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    public CK getCk() {
        return ck;
    }

    public void setCk(CK ck) {
        this.ck = ck;
    }

    @Override
    public String toString() {
        return "RelationInfo{" +
                "pk=" + pk +
                ", ck=" + ck +
                '}';
    }
}
