module com.dwarfeng.subgrade.kafka {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.subgrade.basic;
    requires kafka.clients;
    requires com.alibaba.fastjson2;

    exports com.dwarfeng.subgrade.kafka.sdk.exception;
    exports com.dwarfeng.subgrade.kafka.impl.serializer.fastjson;

    opens com.dwarfeng.subgrade.kafka.sdk.i18n to com.dwarfeng.subgrade.base;
}
