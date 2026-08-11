module com.dwarfeng.subgrade.cache {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.subgrade.basic;
    requires org.slf4j;
    requires static org.jetbrains.annotations;

    exports com.dwarfeng.subgrade.cache.stack.handler;
    exports com.dwarfeng.subgrade.cache.stack.loader;
    exports com.dwarfeng.subgrade.cache.sdk.exception;
    exports com.dwarfeng.subgrade.cache.impl.handler;
}
