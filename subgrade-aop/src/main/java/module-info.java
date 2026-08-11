module com.dwarfeng.subgrade.aop {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.subgrade.basic;
    requires spring.core;
    requires spring.context;
    requires org.aspectj.weaver;
    requires org.slf4j;
    requires static org.jetbrains.annotations;

    exports com.dwarfeng.subgrade.aop.sdk.configuration;
    exports com.dwarfeng.subgrade.aop.sdk.exception;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor.analyse;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor.friendly;
}
