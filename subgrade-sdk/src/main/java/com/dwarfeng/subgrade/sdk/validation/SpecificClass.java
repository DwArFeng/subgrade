package com.dwarfeng.subgrade.sdk.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Objects;

/**
 * 参数需要为指定的类或其子类的验证器注解。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
@Documented
@Constraint(
        validatedBy = {SpecificClass.SpecificClassConstraintValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecificClass {

    String message() default "{com.dwarfeng.fdr.sdk.validation.SpecificClass.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 被检查的对象应该属于的类型。
     *
     * @return 被检查的对象应该属于的类型。
     */
    Class<?> requiredClass();

    /**
     * 是否允许被检查的类的对象是指定类的子类。
     *
     * @return 是否允许被检查的类的对象是指定类的子类。
     */
    boolean allowSubClass() default false;

    class SpecificClassConstraintValidator implements ConstraintValidator<SpecificClass, Object> {

        private static final Logger LOGGER = LoggerFactory.getLogger(SpecificClassConstraintValidator.class);

        private Class<?> requiredClass;
        private boolean allowSubClass;

        @Override
        public void initialize(SpecificClass constraintAnnotation) {
            LOGGER.debug("初始化...");
            LOGGER.debug("获取注解的 requiredClass 属性，为: {}...", constraintAnnotation.requiredClass().getCanonicalName());
            requiredClass = constraintAnnotation.requiredClass();
            LOGGER.debug("获取注解的 allowSubClass 属性，为: {}...", constraintAnnotation.allowSubClass());
            allowSubClass = constraintAnnotation.allowSubClass();
        }

        // 执行校验操作
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            LOGGER.debug("开始验证...");
            try {
                if (allowSubClass) {
                    LOGGER.debug("允许验证对象为指定类的子类，判断验证对象的类是否是 {} 的子类或本身类...", requiredClass.getCanonicalName());
                    boolean aFlag = requiredClass.isAssignableFrom(value.getClass());
                    LOGGER.debug("验证结束，判断验证对象的类是否是 {} 的子类或本身类，结果为 {}...", requiredClass.getCanonicalName(), aFlag);
                    return aFlag;
                } else {
                    LOGGER.debug("不允许验证对象为指定类的子类，判断验证对象的类与 {} 是否相等...", requiredClass.getCanonicalName());
                    boolean aFlag = Objects.equals(value.getClass(), requiredClass);
                    LOGGER.debug("验证结束，判断验证对象的类与 {} 是否相等，结果为 {}...", requiredClass.getCanonicalName(), aFlag);
                    return aFlag;
                }
            } catch (Exception e) {
                LOGGER.warn("验证过程中发生了意料之外的异常", e);
                return false;
            }
        }
    }
}
