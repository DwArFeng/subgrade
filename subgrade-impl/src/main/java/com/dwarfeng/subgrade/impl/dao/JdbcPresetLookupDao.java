package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.mapper.PresetLookupMapper;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.ResultMapper;
import com.dwarfeng.subgrade.sdk.jdbc.template.CreateTableTemplate;
import com.dwarfeng.subgrade.sdk.jdbc.template.PresetLookupTemplate;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 使用 Jdbc 实现的 PresetLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class JdbcPresetLookupDao<E extends Entity<?>> implements PresetLookupDao<E> {

    private JdbcTemplate jdbcTemplate;
    private PresetLookupTemplate presetLookupTemplate;
    private PresetLookupMapper presetLookupMapper;
    private ResultMapper<E> resultMapper;

    public JdbcPresetLookupDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull PresetLookupTemplate presetLookupTemplate,
            @NonNull PresetLookupMapper presetLookupMapper,
            @NonNull ResultMapper<E> resultMapper) {
        this(jdbcTemplate, presetLookupTemplate, presetLookupMapper, resultMapper, null);
    }

    public JdbcPresetLookupDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull PresetLookupTemplate presetLookupTemplate,
            @NonNull PresetLookupMapper presetLookupMapper,
            @NonNull ResultMapper<E> resultMapper,
            CreateTableTemplate createTableTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.presetLookupTemplate = presetLookupTemplate;
        this.presetLookupMapper = presetLookupMapper;
        this.resultMapper = resultMapper;
        if (Objects.nonNull(createTableTemplate)) {
            creatTable(createTableTemplate);
        }
    }

    private void creatTable(CreateTableTemplate createTableTemplate) {
        List<String> crateTableSQLList = createTableTemplate.createTableSQL();
        for (String crateTableSQL : crateTableSQLList) {
            jdbcTemplate.execute(crateTableSQL);
        }
    }

    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
            String querySQL = presetLookupTemplate.lookupSQL(preset, objs);
            Object[] args = presetLookupMapper.lookup2Args(preset, objs);
            return queryEntity(querySQL, args);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            String querySQL = presetLookupTemplate.pagingSQL(preset, objs, pagingInfo);
            Object[] args = presetLookupMapper.paging2Args(preset, objs, pagingInfo);
            return queryEntity(querySQL, args);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private List<E> queryEntity(String querySQL, Object[] args) {
        return jdbcTemplate.query(querySQL, args, rs -> {
            List<E> list = new ArrayList<>();
            while (rs.next()) {
                list.add(resultMapper.result2Entity(rs));
            }
            return list;
        });
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            String querySQL = presetLookupTemplate.countSQL(preset, objs);
            Object[] args = presetLookupMapper.count2Args(preset, objs);
            return queryCount(querySQL, args);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private int queryCount(String querySQL, Object[] args) {
        Integer count = jdbcTemplate.query(querySQL, args, rs -> {
            if (rs.next()) {
                Object object = rs.getObject(1);
                //尝试将返回的对象转换与任何已知的数字类型进行比对，并转化为Integer。
                //可能性越小的类型越靠下，保证对象以尽可能少的判断次数转换为Integer。
                if (object instanceof Integer) {
                    return (Integer) object;
                } else if (object instanceof Long) {
                    return ((Long) object).intValue();
                } else if (object instanceof BigInteger) {
                    return ((BigInteger) object).intValue();
                } else if (object instanceof BigDecimal) {
                    return ((BigDecimal) object).intValue();
                } else if (object instanceof Short) {
                    return ((Short) object).intValue();
                } else if (object instanceof Byte) {
                    return ((Byte) object).intValue();
                } else {
                    throw new IllegalArgumentException("返回的结果类型为 " + object.getClass().getCanonicalName() +
                            " 不属于任何已知的数字类型");
                }
            } else {
                return 0;
            }
        });
        assert count != null;
        return count;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(@NonNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PresetLookupTemplate getPresetLookupTemplate() {
        return presetLookupTemplate;
    }

    public void setPresetLookupTemplate(@NonNull PresetLookupTemplate presetLookupTemplate) {
        this.presetLookupTemplate = presetLookupTemplate;
    }

    public PresetLookupMapper getPresetLookupMapper() {
        return presetLookupMapper;
    }

    public void setPresetLookupMapper(@NonNull PresetLookupMapper presetLookupMapper) {
        this.presetLookupMapper = presetLookupMapper;
    }

    public ResultMapper<E> getResultMapper() {
        return resultMapper;
    }

    public void setResultMapper(@NonNull ResultMapper<E> resultMapper) {
        this.resultMapper = resultMapper;
    }
}
