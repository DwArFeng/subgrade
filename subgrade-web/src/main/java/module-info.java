module com.dwarfeng.subgrade.web {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.subgrade.basic;
    requires com.dwarfeng.subgrade.aop;
    requires com.dwarfeng.subgrade.expression;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.aop;
    requires transitive jakarta.servlet;
    requires transitive jakarta.validation;
    requires com.alibaba.fastjson2;
    requires org.slf4j;
    requires static org.jetbrains.annotations;
    requires org.aspectj.weaver;

    exports com.dwarfeng.subgrade.web.stack.exception;
    exports com.dwarfeng.subgrade.web.stack.handler;
    exports com.dwarfeng.subgrade.web.stack.response;
    exports com.dwarfeng.subgrade.web.sdk.configuration;
    exports com.dwarfeng.subgrade.web.sdk.bean.dto;
    exports com.dwarfeng.subgrade.web.sdk.bean.key;
    exports com.dwarfeng.subgrade.web.sdk.exception;
    exports com.dwarfeng.subgrade.web.sdk.fastjson.serialize;
    exports com.dwarfeng.subgrade.web.sdk.interceptor;
    exports com.dwarfeng.subgrade.web.sdk.interceptor.http;
    exports com.dwarfeng.subgrade.web.sdk.interceptor.login;
    exports com.dwarfeng.subgrade.web.sdk.interceptor.permission;
    exports com.dwarfeng.subgrade.web.sdk.validation;
    exports com.dwarfeng.subgrade.web.sdk.validation.group;
    exports com.dwarfeng.subgrade.web.impl.interceptor.friendly;
}
