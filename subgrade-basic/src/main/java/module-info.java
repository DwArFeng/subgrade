module com.dwarfeng.subgrade.basic {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.dutil.basic;
    requires transitive org.slf4j;
    requires static org.jetbrains.annotations;

    exports com.dwarfeng.subgrade.basic.stack.bean;
    exports com.dwarfeng.subgrade.basic.stack.bean.dto;
    exports com.dwarfeng.subgrade.basic.stack.bean.entity;
    exports com.dwarfeng.subgrade.basic.stack.bean.key;
    exports com.dwarfeng.subgrade.basic.stack.exception;
    exports com.dwarfeng.subgrade.basic.stack.generation;
    exports com.dwarfeng.subgrade.basic.stack.handler;
    exports com.dwarfeng.subgrade.basic.stack.log;
    exports com.dwarfeng.subgrade.basic.stack.service;
    exports com.dwarfeng.subgrade.basic.sdk.bean.dto;
    exports com.dwarfeng.subgrade.basic.sdk.enumeration;
    exports com.dwarfeng.subgrade.basic.sdk.exception;
    exports com.dwarfeng.subgrade.basic.sdk.log;
    exports com.dwarfeng.subgrade.basic.impl.bean;
    exports com.dwarfeng.subgrade.basic.impl.exception;
    exports com.dwarfeng.subgrade.basic.impl.generation;
}
