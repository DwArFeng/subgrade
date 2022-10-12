package com.dwarfeng.subgrade.sdk.memory.io;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.io.IOUtil;
import com.dwarfeng.dutil.basic.io.StringInputStream;
import com.dwarfeng.dutil.basic.io.StringOutputStream;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * JSON文件资源映射桥。
 *
 * <p>
 * 将JSON文件资源与映射进行桥接的映射桥实现。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class JsonFileRefResourceBridge<K extends Key, E extends Entity<K>, JE extends Bean>
        implements RefResourceBridge<K, E> {

    private File file;
    private BeanTransformer<E, JE> transformer;
    private Class<JE> classJE;

    public JsonFileRefResourceBridge(
            @NonNull File file,
            @NonNull BeanTransformer<E, JE> transformer,
            @NonNull Class<JE> classJE
    ) {
        this.file = file;
        this.transformer = transformer;
        this.classJE = classJE;
    }

    @Override
    public void fillRef(ReferenceModel<E> referenceModel) throws NullPointerException, ProcessException {
        try {
            makeSureFileExists(file);
            StringOutputStream sout = null;
            FileInputStream fin = null;
            try {
                sout = new StringOutputStream();
                fin = new FileInputStream(file);
                IOUtil.trans(fin, sout, 4096);
                sout.flush();
                JE je = JSON.parseObject(sout.toString(), classJE);
                referenceModel.set(transformer.reverseTransform(je));
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

    @Override
    public void saveRef(ReferenceModel<E> referenceModel) throws NullPointerException, ProcessException, UnsupportedOperationException {
        try {
            makeSureFileExists(file);
            JE je = transformer.transform(referenceModel.get());
            String json = JSON.toJSONString(
                    je,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteMapNullValue);
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

    public void setFile(@NonNull File file) {
        this.file = file;
    }

    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@NonNull BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }

    public Class<JE> getClassJE() {
        return classJE;
    }

    public void setClassJE(@NonNull Class<JE> classJE) {
        this.classJE = classJE;
    }
}
