package com.dwarfeng.subgrade.sdk.memory.io;

import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * XML文件资源映射桥。
 *
 * <p>
 * 将XML文件资源与映射进行桥接的映射桥实现。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class XmlFileMapResourceBridge<K extends Key, E extends Entity<K>, XE extends Bean,
        XR extends XmlFileMapResourceBridge.Root<XE>> implements MapResourceBridge<K, E> {

    private File file;
    private BeanTransformer<E, XE> transformer;
    private Class<XR> classXR;

    /**
     * 生成一个 XML 文件资源映射桥。
     *
     * @param file        指定的文件。
     * @param transformer 指定的转换器。
     * @param classXR     指定的 XR 类型。
     */
    public XmlFileMapResourceBridge(
            @Nonnull File file,
            @Nonnull BeanTransformer<E, XE> transformer,
            @Nonnull Class<XR> classXR
    ) {
        this.file = file;
        this.transformer = transformer;
        this.classXR = classXR;
    }

    /**
     * 生成一个 XML 文件资源映射桥。
     *
     * @param file        指定的文件。
     * @param transformer 指定的转换器。
     * @param classXE     指定的 XE 类型。
     * @param classXR     指定的 XR 类型。
     * @deprecated 由于参数 classXE 没有使用，因此该构造器不再推荐使用。
     */
    @SuppressWarnings("unused")
    @Deprecated
    public XmlFileMapResourceBridge(
            @Nonnull File file,
            @Nonnull BeanTransformer<E, XE> transformer,
            @Nonnull Class<XE> classXE,
            @Nonnull Class<XR> classXR
    ) {
        this.file = file;
        this.transformer = transformer;
        this.classXR = classXR;
    }

    @Override
    public void fillMap(Map<K, E> map) throws ProcessException {
        try {
            makeSureFileExists(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(classXR);
            @SuppressWarnings("unchecked")
            XmlFileMapResourceBridge.Root<XE> unmarshal = (Root<XE>) jaxbContext.createUnmarshaller().unmarshal(file);
            Map<K, E> collect = unmarshal.getXmlElements().stream().map(transformer::reverseTransform)
                    .collect(Collectors.toMap(E::getKey, Function.identity()));
            map.putAll(collect);
        } catch (Exception e) {
            throw new ProcessException("填充 Map 时发生异常", e);
        }
    }

    @Override
    public void saveMap(Map<K, E> map) throws ProcessException, UnsupportedOperationException {
        try {
            makeSureFileExists(file);
            List<XE> collect = map.values().stream().map(transformer::transform).collect(Collectors.toList());
            XR xr = classXR.newInstance();
            xr.setXmlElements(collect);
            JAXBContext jaxbContext = JAXBContext.newInstance(classXR);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(xr, file);
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

    public void setFile(@Nonnull File file) {
        this.file = file;
    }

    public BeanTransformer<E, XE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@Nonnull BeanTransformer<E, XE> transformer) {
        this.transformer = transformer;
    }

    public Class<XR> getClassXR() {
        return classXR;
    }

    public void setClassXR(@Nonnull Class<XR> classXR) {
        this.classXR = classXR;
    }

    /**
     * 根元素接口。
     *
     * @author DwArFeng
     * @since 0.0.3-beta
     */
    public interface Root<XE extends Bean> {

        List<XE> getXmlElements();

        void setXmlElements(List<XE> list);
    }
}
