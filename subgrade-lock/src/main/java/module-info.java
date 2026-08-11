module com.dwarfeng.subgrade.lock {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.subgrade.basic;
    requires com.dwarfeng.subgrade.lifecycle;
    requires curator.framework;
    requires curator.recipes;
    requires org.slf4j;
    requires static org.jetbrains.annotations;

    exports com.dwarfeng.subgrade.lock.stack.handler;
    exports com.dwarfeng.subgrade.lock.sdk.exception;
    exports com.dwarfeng.subgrade.lock.impl.handler.curator;
}
