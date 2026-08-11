package com.dwarfeng.subgrade.data.sdk.memory.io;

import com.dwarfeng.dutil.basic.sdk.io.FileUtil;
import com.dwarfeng.dutil.basic.stack.lifecycle.ProcessException;
import com.dwarfeng.dutil.basic.stack.model.ReferenceModel;
import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;

/**
 * XML 文件资源映射桥。
 *
 * <p>
 * 将 XML 文件资源与映射进行桥接的映射桥实现。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class XmlFileRefResourceBridge<K extends Key, E extends Entity<K>, XE extends Bean>
        implements RefResourceBridge<K, E> {

    private File file;
    private BeanTransformer<E, XE> transformer;
    private Class<XE> classXE;

    public XmlFileRefResourceBridge(
            @NotNull File file,
            @NotNull BeanTransformer<E, XE> transformer,
            @NotNull Class<XE> classXE
    ) {
        this.file = file;
        this.transformer = transformer;
        this.classXE = classXE;
    }

    @Override
    public void fillRef(ReferenceModel<E> referenceModel) throws NullPointerException, ProcessException {
        try {
            makeSureFileExists(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(classXE);
            @SuppressWarnings("unchecked")
            XE xe = (XE) jaxbContext.createUnmarshaller().unmarshal(file);
            referenceModel.set(transformer.reverseTransform(xe));
        } catch (Exception e) {
            throw new ProcessException("填充 referenceModel 时发生异常", e);
        }
    }

    @Override
    public void saveRef(ReferenceModel<E> referenceModel) throws NullPointerException, ProcessException,
            UnsupportedOperationException {
        try {
            makeSureFileExists(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(classXE);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(transformer.transform(referenceModel.get()), file);
        } catch (Exception e) {
            throw new ProcessException("保存 referenceModel 时发生异常", e);
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

    public BeanTransformer<E, XE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@NotNull BeanTransformer<E, XE> transformer) {
        this.transformer = transformer;
    }

    public Class<XE> getClassXE() {
        return classXE;
    }

    public void setClassXE(@NotNull Class<XE> classXE) {
        this.classXE = classXE;
    }
}
