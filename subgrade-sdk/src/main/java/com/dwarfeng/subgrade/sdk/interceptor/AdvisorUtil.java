package com.dwarfeng.subgrade.sdk.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 增强工具类。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public final class AdvisorUtil {

    /**
     * 获取 ProceedingJoinPoint 的方法签名。
     *
     * @param pjp ProceedingJoinPoint。
     * @return ProceedingJoinPoint 的方法签名。
     */
    public static MethodSignature methodSignature(ProceedingJoinPoint pjp) {
        return (MethodSignature) pjp.getSignature();
    }

    /**
     * 获取 ProceedingJoinPoint 的直接类。
     *
     * <p> 获取被代理的对象的直接类，而不是父接口/父类。
     *
     * @param pjp ProceedingJoinPoint。
     * @return ProceedingJoinPoint 的直接类。
     */
    public static Class<?> originClass(ProceedingJoinPoint pjp) {
        return pjp.getTarget().getClass();
    }

    /**
     * 获取 ProceedingJoinPoint 的直接方法。
     *
     * <p> 获取被代理的对象的直接类的方法，而不是父接口/父类的方法。
     *
     * @param pjp ProceedingJoinPoint。
     * @return ProceedingJoinPoint 的直接方法。
     */
    public static Method directMethod(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = methodSignature(pjp);
        // 获取拦截方法的名称。
        String methodName = methodSignature.getName();
        // 获取拦截方法的类型。
        Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        Method method = null;
        try {
            // 通过反射获得拦截的method
            method = originClass(pjp).getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ignored) {
            // 可以保证方法一定存在。
        }
        assert method != null;
        return method;
    }

    /**
     * 获取 ProceedingJoinPoint 的直接类的注解。
     *
     * <p> 获取被代理的对象的直接类的注解，而不是父接口/父类的注解。
     *
     * @param pjp             ProceedingJoinPoint。
     * @param annotationClass 注解的类。
     * @param <T>             注解类的泛型。
     * @return ProceedingJoinPoint 的直接类的注解。
     */
    public static <T extends Annotation> T directClassAnnotation(
            ProceedingJoinPoint pjp, Class<T> annotationClass) {
        return pjp.getTarget().getClass().getAnnotation(annotationClass);
    }

    /**
     * 获取 ProceedingJoinPoint 的直接类的注解。
     *
     * <p> 获取被代理的对象的直接类的注解，而不是父接口/父类的注解。
     *
     * @param pjp             ProceedingJoinPoint。
     * @param annotationClass 注解的类。
     * @param <T>             注解类的泛型。
     * @return ProceedingJoinPoint 的直接类的注解组成的数组。
     */
    public static <T extends Annotation> T[] directClassAnnotations(
            ProceedingJoinPoint pjp, Class<T> annotationClass) {
        return pjp.getTarget().getClass().getAnnotationsByType(annotationClass);
    }

    /**
     * 获取 ProceedingJoinPoint 的直接方法的注解。
     *
     * <p> 获取被代理的对象的直接方法的注解，而不是父接口/父类方法的注解。
     *
     * @param pjp             ProceedingJoinPoint。
     * @param annotationClass 注解的类。
     * @param <T>             注解类的泛型。
     * @return ProceedingJoinPoint 的直接方法的注解。
     */
    public static <T extends Annotation> T directMethodAnnotation(
            ProceedingJoinPoint pjp, Class<T> annotationClass) {
        return directMethod(pjp).getAnnotation(annotationClass);
    }

    /**
     * 获取 ProceedingJoinPoint 的直接方法的注解。
     *
     * <p> 获取被代理的对象的直接方法的注解，而不是父接口/父类方法的注解。
     *
     * @param pjp             ProceedingJoinPoint。
     * @param annotationClass 注解的类。
     * @param <T>             注解类的泛型。
     * @return ProceedingJoinPoint 的直接方法的注解组成的数组。
     */
    public static <T extends Annotation> T[] directMethodAnnotations(
            ProceedingJoinPoint pjp, Class<T> annotationClass) {
        return directMethod(pjp).getAnnotationsByType(annotationClass);
    }

    /**
     * 获取 ProceedingJoinPoint 的直接方法上参数的注解。
     *
     * <p> 获取被代理的对象的直接方法上参数的注解，而不是父接口/父类方法上参数的注解。
     *
     * @param pjp             ProceedingJoinPoint。
     * @param annotationClass 注解的类。
     * @param <T>             注解类的泛型。
     * @return ProceedingJoinPoint 的直接方法上参数的注解。
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T[] directParameterAnnotation(
            ProceedingJoinPoint pjp, Class<T> annotationClass) {
        Method method = directMethod(pjp);
        T[] array = (T[]) java.lang.reflect.Array.newInstance(annotationClass, method.getParameterCount());
        for (int i = 0; i < method.getParameters().length; i++) {
            array[i] = method.getParameters()[i].getAnnotation(annotationClass);
        }
        return array;
    }

    /**
     * 获取 ProceedingJoinPoint 的直接方法上参数的注解。
     *
     * <p> 获取被代理的对象的直接方法上参数的注解，而不是父接口/父类方法上参数的注解。
     *
     * @param pjp             ProceedingJoinPoint。
     * @param annotationClass 注解的类。
     * @param <T>             注解类的泛型。
     * @return ProceedingJoinPoint 的直接方法上参数的注解组成的数组。
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T[][] directParameterAnnotations(
            ProceedingJoinPoint pjp, Class<T> annotationClass) {
        Class<?> annotationArrayClass = java.lang.reflect.Array.newInstance(annotationClass, 0).getClass();
        Method method = directMethod(pjp);
        T[][] array = (T[][]) java.lang.reflect.Array.newInstance(annotationArrayClass, method.getParameterCount());
        for (int i = 0; i < method.getParameters().length; i++) {
            array[i] = method.getParameters()[i].getAnnotationsByType(annotationClass);
        }
        return array;
    }

    /**
     * 获取 ProceedingJoinPoint 的直接方法的参数。
     *
     * <p> 获取被代理的对象的直接方法的参数，而不是父接口/父类方法的参数。
     *
     * @param pjp ProceedingJoinPoint。
     * @return ProceedingJoinPoint 的直接方法的参数。
     */
    public static Parameter[] directParameters(ProceedingJoinPoint pjp) {
        return directMethod(pjp).getParameters();
    }

    private AdvisorUtil() {
        throw new IllegalStateException("禁止外部实例化");
    }
}
