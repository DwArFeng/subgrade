module com.dwarfeng.subgrade.aop {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.subgrade.basic;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires org.aspectj.weaver;
    requires org.slf4j;
    requires static org.jetbrains.annotations;

    exports com.dwarfeng.subgrade.aop.stack.handler;
    exports com.dwarfeng.subgrade.aop.sdk.util;
    exports com.dwarfeng.subgrade.aop.sdk.exception;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor.analyse;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor.audit;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor.audit.propctr;
    exports com.dwarfeng.subgrade.aop.sdk.interceptor.friendly;

    opens com.dwarfeng.subgrade.aop.sdk.i18n to com.dwarfeng.subgrade.base;
    opens com.dwarfeng.subgrade.aop.sdk.interceptor.audit to spring.core, spring.beans;
}
