package com.dwarfeng.subgrade.sdk.mybatis;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.util.List;

/**
 * 批量关系信息。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class BatchRelationInfo<PK extends Key, CK extends Key> implements Dto {

    private static final long serialVersionUID = 7300328911379492539L;

    private PK pk;
    private List<CK> cks;

    public BatchRelationInfo() {
    }

    public BatchRelationInfo(PK pk, List<CK> cks) {
        this.pk = pk;
        this.cks = cks;
    }

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    public List<CK> getCks() {
        return cks;
    }

    public void setCks(List<CK> cks) {
        this.cks = cks;
    }

    @Override
    public String toString() {
        return "BatchRelationInfo{" +
                "pk=" + pk +
                ", cks=" + cks +
                '}';
    }
}
