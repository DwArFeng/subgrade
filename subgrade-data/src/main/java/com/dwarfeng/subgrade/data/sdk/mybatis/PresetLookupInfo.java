package com.dwarfeng.subgrade.data.sdk.mybatis;

import com.dwarfeng.subgrade.basic.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.basic.stack.bean.dto.PagingInfo;

import java.io.Serial;
import java.util.Arrays;

/**
 * 预设查询信息。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class PresetLookupInfo implements Dto {

    @Serial
    private static final long serialVersionUID = 8987778363707268855L;

    private String preset;
    private Object[] args;
    private PagingInfo pagingInfo;

    public PresetLookupInfo() {
    }

    public PresetLookupInfo(String preset, Object[] args, PagingInfo pagingInfo) {
        this.preset = preset;
        this.args = args;
        this.pagingInfo = pagingInfo;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public PagingInfo getPagingInfo() {
        return pagingInfo;
    }

    public void setPagingInfo(PagingInfo pagingInfo) {
        this.pagingInfo = pagingInfo;
    }

    @Override
    public String toString() {
        return "PresetLookupInfo{" +
                "preset='" + preset + '\'' +
                ", args=" + Arrays.toString(args) +
                ", pagingInfo=" + pagingInfo +
                '}';
    }
}
