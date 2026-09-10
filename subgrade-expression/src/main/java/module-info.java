module com.dwarfeng.subgrade.expression {

    requires com.dwarfeng.subgrade.base;
    requires com.dwarfeng.subgrade.basic;
    requires spring.expression;

    exports com.dwarfeng.subgrade.expression.stack.parser;
    exports com.dwarfeng.subgrade.expression.stack.exception;
    exports com.dwarfeng.subgrade.expression.sdk.exception;
    exports com.dwarfeng.subgrade.expression.sdk.parser;
    exports com.dwarfeng.subgrade.expression.impl.parser;
    exports com.dwarfeng.subgrade.expression.impl.spel;

    opens com.dwarfeng.subgrade.expression.sdk.i18n to com.dwarfeng.subgrade.base;
    opens com.dwarfeng.subgrade.expression.stack.i18n to com.dwarfeng.subgrade.base;
}
