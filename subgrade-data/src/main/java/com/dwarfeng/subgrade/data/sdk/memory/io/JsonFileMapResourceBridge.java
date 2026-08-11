package com.dwarfeng.subgrade.data.sdk.memory.io;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.dwarfeng.dutil.basic.impl.io.StringInputStream;
import com.dwarfeng.dutil.basic.impl.io.StringOutputStream;
import com.dwarfeng.dutil.basic.sdk.io.FileUtil;
import com.dwarfeng.dutil.basic.sdk.io.IOUtil;
import com.dwarfeng.dutil.basic.stack.lifecycle.ProcessException;
import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JSON 文件资源映射桥。
 *
 * <p>
 * 将 JSON 文件资源与映射进行桥接的映射桥实现。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class JsonFileMapResourceBridge<K extends Key, E extends Entity<K>, JE extends Bean>
        implements MapResourceBridge<K, E> {

    private File file;
    private BeanTransformer<E, JE> transformer;
    private Class<JE> classJE;

    public JsonFileMapResourceBridge(
            @NotNull File file,
            @NotNull BeanTransformer<E, JE> transformer,
            @NotNull Class<JE> classJE
    ) {
        this.file = file;
        this.transformer = transformer;
        this.classJE = classJE;
    }

    @Override
    public void fillMap(Map<K, E> map) throws ProcessException {
        try {
            makeSureFileExists(file);
            StringOutputStream sout = null;
            FileInputStream fin = null;
            try {
                sout = new StringOutputStream();
                fin = new FileInputStream(file);
                IOUtil.trans(fin, sout, 4096);
                sout.flush();
                Map<K, E> collect = JSON.parseArray(sout.toString(), classJE).stream()
                        .map(transformer::reverseTransform)
                        .collect(Collectors.toMap(E::getKey, Function.identity()));
                map.putAll(collect);
            } finally {
                if (Objects.nonNull(sout)) {
                    sout.close();
                }
                if (Objects.nonNull(fin)) {
                    fin.close();
                }
            }
        } catch (Exception e) {
            throw new ProcessException("填充 Map 时发生异常", e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void saveMap(Map<K, E> map) throws ProcessException, UnsupportedOperationException {
        try {
            makeSureFileExists(file);
            List<JE> collect = map.values().stream().map(transformer::transform).collect(Collectors.toList());
            String json = JSON.toJSONString(
                    collect,
                    JSONWriter.Feature.PrettyFormat,
                    JSONWriter.Feature.WriteNulls);
            StringInputStream sin = null;
            FileOutputStream fout = null;
            try {
                sin = new StringInputStream(json);
                fout = new FileOutputStream(file);
                IOUtil.trans(sin, fout, 4096);
                fout.flush();
            } finally {
                if (Objects.nonNull(sin)) {
                    sin.close();
                }
                if (Objects.nonNull(fout)) {
                    fout.close();
                }
            }
        } catch (Exception e) {
            throw new ProcessException("保存 Map 时发生异常", e);
        }
    }

    private void makeSureFileExists(File file) throws IOException {
        FileUtil.createFileIfNotExists(file);
    }

    public File getFile() {
        return file;
    }

    public void setFile(@NotNull File file) {
        this.file = file;
    }

    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@NotNull BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }

    public Class<JE> getClassJE() {
        return classJE;
    }

    public void setClassJE(@NotNull Class<JE> classJE) {
        this.classJE = classJE;
    }
}
