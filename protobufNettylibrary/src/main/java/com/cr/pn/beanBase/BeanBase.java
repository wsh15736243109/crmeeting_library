package com.cr.pn.beanBase;

import com.cr.pn.Utils.Json.RemoveExclus;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/7/31.
 */

public class BeanBase implements Serializable {

    @RemoveExclus
    private List<Method> Cname;

    @RemoveExclus
    private List<Method> Bname;

    @RemoveExclus
    private Class Cclazz;

    @RemoveExclus
    private Class Bclazz;

    @RemoveExclus
    private BeanBase dataValue;

    public void copy(BeanBase dataValue){
        try {
            this.dataValue = dataValue;
            if (dataValue==null){
                return;
            }
            Class clazz = Class.forName(dataValue.getClass().getName());
            Cclazz = clazz;
            Method[] methods = clazz.getMethods();
            Cname = new ArrayList<>();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.substring(0,3).equals("get")){
                    Cname.add(method);
                }
            }
            clazz = Class.forName(this.getClass().getName());
            Bclazz = clazz;
            methods = clazz.getMethods();
            Bname = new ArrayList<>();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.substring(0,3).equals("set")){
                    Bname.add(method);
                }

            }
            startCopy();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startCopy(){
        for (int i = 0;i<Bname.size();i++){
            String bname = Bname.get(i).getName();
            for (int j = 0;j<Cname.size();j++){
                String cname = Cname.get(j).getName();
                if (bname.substring(3,bname.length()).equals(cname.substring(3,cname.length()))){
                    try {
                        Bname.get(i).invoke(this,Cname.get(j).invoke(dataValue));
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
