package com.cr.pn.eventBus.synEvent;


import androidx.collection.ArrayMap;

import com.cr.pn.Utils.ViewUtils.StringUtil;
import com.cr.pn.beanBase.BeanBase;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by zy on 2018/8/23.
 * Event同步消息key值管理类
 */
public class SynEventKeyManager {

    private static SynEventKeyManager instance;

    private ArrayMap<String,Type> typeHashMap = new ArrayMap<>();

    private ArrayMap<String,Type> typeHashMapForever = new ArrayMap<>();

    public static SynEventKeyManager getInstance(){
        if (instance == null){
            synchronized (SynEventKeyManager.class){
                if (instance == null){
                    instance = new SynEventKeyManager();
                }
            }
        }
        return instance;
    }

    /**
     * 保存需要同步的消息
     * @param key
     * @param type
     */
    public synchronized void registerKey(String key,Type type){
        if (!typeHashMap.containsKey(key)){
            typeHashMap.put(key,type);
        }
    }

    /**
     * 保存长存消息
     * @param key
     * @param type
     */
    public synchronized void registerKeyForever(String key,Type type){
        if (!typeHashMapForever.containsKey(key)){
            typeHashMapForever.put(key,type);
        }
    }

    /**
     * 返回类型值.
     * @param key
     * @return
     */
    public synchronized Type getType(String key){
        if (typeHashMap.containsKey(key)){
            return typeHashMap.get(key);
        }
        return null;
    }

    /**
     * 遍历map，获取type
     * @param value
     * @return
     */
    public synchronized TypeAndKey whileType(String value){
        if (StringUtil.isEmpty(value)){
            return null;
        }
        TypeAndKey typeAndKey = null;
        for(Map.Entry<String, Type> entry: typeHashMap.entrySet()){
            if (value.contains(entry.getKey())){
                typeAndKey = new TypeAndKey().setType(entry.getValue()).setKey(entry.getKey());
            }
        }
        return typeAndKey;
    }

    /**
     * 遍历长存map，获取type
     * @param value
     * @return
     */
    public synchronized TypeAndKey whileTypeForever(String value){
        if (StringUtil.isEmpty(value)){
            return null;
        }
        TypeAndKey typeAndKey = null;
        for(Map.Entry<String, Type> entry: typeHashMapForever.entrySet()){
            if (value.contains(entry.getKey())){
                typeAndKey = new TypeAndKey().setType(entry.getValue()).setKey(entry.getKey());
            }
        }
        return typeAndKey;
    }

    /**
     * 消息获取后移除.
     * @param key
     */
    public synchronized void removeEvent(String key){
        if (typeHashMap.containsKey(key)){
            typeHashMap.remove(key);
        }
    }


    /**
     * 移除长存.
     * @param key
     */
    public synchronized void removeEventForever(String key){
        if (typeHashMapForever.containsKey(key)){
            typeHashMapForever.remove(key);
        }
    }

    public class TypeAndKey extends BeanBase{

        private Type type;

        private String key;

        private Object object;

        public Type getType() {
            return type;
        }

        public TypeAndKey setType(Type type) {
            this.type = type;
            return this;
        }

        public String getKey() {
            return key;
        }

        public TypeAndKey setKey(String key) {
            this.key = key;
            return this;
        }

        public Object getObject() {
            return object;
        }

        public TypeAndKey setObject(Object object) {
            this.object = object;
            return this;
        }
    }

}
