package com.dwarfeng.subgrade.sdk.mybatis;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 关系信息。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class RelationInfo<PK extends Key, CK extends Key> implements Dto {

    private static final long serialVersionUID = 8055883447384781211L;

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
