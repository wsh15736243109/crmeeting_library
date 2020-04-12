package com.cr.pn.Utils.classUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by zy on 2018/6/13.
 */

public class ClassUtils {

    /**
     * 获取类的泛型实例化,含参
     * @param clazz1
     * @param args
     * @return
     * @throws Exception
     */
    public static Object T2Class(Class clazz1, Object[] args){
        ParameterizedType pt = (ParameterizedType) clazz1.getGenericSuperclass();
        Class[] argsClass = new Class[args.length];
        for(int i=0,j=args.length; i<j;i++){
            argsClass[i] = args[i].getClass();
        }
        Constructor cons = null;
        try {
            Class clazz = (Class) pt.getActualTypeArguments()[0];
            cons = clazz.getConstructor(argsClass);
            return cons.newInstance(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return cons;
    }

    /**
     * 获取类的泛型实例化,无参.
     * @param clazz1
     * @return
     * @throws Exception
     */
    public static Object T2Class(Class clazz1){
        ParameterizedType pt = (ParameterizedType) clazz1.getGenericSuperclass();
        try {
            Class clazz = (Class) pt.getActualTypeArguments()[0];
            return clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
