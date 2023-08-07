package com.dwarfeng.subgrade.sdk.hibernate.hql;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;

import java.util.Map;

/**
 * 查询信息
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class HqlQueryInfo implements Dto {

    private static final long serialVersionUID = 7760761072008369641L;

    private String hql;
    private Map<String, Object> paramMap;

    public HqlQueryInfo() {
    }

    public HqlQueryInfo(String hql, Map<String, Object> paramMap) {
        this.hql = hql;
        this.paramMap = paramMap;
    }

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public String toString() {
        return "HqlQueryInfo{" +
                "hql='" + hql + '\'' +
                ", paramMap=" + paramMap +
                '}';
    }
}
