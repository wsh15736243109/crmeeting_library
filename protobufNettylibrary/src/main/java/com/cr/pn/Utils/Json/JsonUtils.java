package com.cr.pn.Utils.Json;

import com.cr.pn.Utils.reflect.TypeBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Json工具类.
 * Created by zy on 2017/2/8.
 */
public class JsonUtils {

    private static Gson gson;

    /**
     * 实例化gson.
     */
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd hh:mm:ss")
                .registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter())
                .registerTypeAdapter(Date.class,new DateTypeAdapter())
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .setExclusionStrategies(new CustomExclusionStrategy());
        gson = builder.create();
    }

    public static Gson getGson() {
        return gson;
    }

    /**
     * Object转json.
     * @param object
     * @return
     */
    public static String toJson(Object object){
        return gson.toJson(object);
    }

    /**
     * JsonArray转List对象.
     * @param json
     * @param clazz
     * @param clazz2
     * list对象中对象的泛型
     * @return
     */
    public static <T> List<T> toArrayObject(String json,Class<T> clazz,Class<?> clazz2){
        Type type = TypeBuilder
                .newInstance(List.class)
                .beginSubType(clazz)
                .addTypeParam(clazz2)
                .endSubType()
                .build();
        List<T> list = gson.fromJson(json,type);
        return list;
    }

    /**
     * JsonArray转List对象.
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> toArrayObject(String json,Class<T> clazz){
        Type type = TypeBuilder
                .newInstance(List.class)
                .beginSubType(clazz)
                .endSubType()
                .build();
        List<T> list = gson.fromJson(json,type);
        return list;
    }

    /**
     * JsonObject转对象.
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json,Class<T> clazz){
        return gson.fromJson(json,clazz);
    }

    /**
     * JsonObject转对象.
     * @param json
     * @param clazz
     * @param clazz2
     * 泛型类型.
     * @return
     */
    public static <T> T toObject(String json,Class<T> clazz,Class<?> clazz2){
        Type type = TypeBuilder
                .newInstance(clazz)
                .addTypeParam(clazz2)
                .build();
        return gson.fromJson(json,type);
    }

    /**
     * JsonObject转对象,其中泛型为List.
     * @param json
     * @param clazz
     * @param clazz2
     * list中泛型的类型
     * @return
     */
    public static <T> T toObjectT2List(String json,Class<T> clazz,Class<?> clazz2){
        Type type = TypeBuilder
                .newInstance(clazz)
                .beginSubType(List.class)
                .addTypeParam(clazz2)
                .endSubType()
                .build();
        return gson.fromJson(json, type);
    }

    /**
     * json转字符串,自定义type.
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T toObjectType(String json,Type type){
        return gson.fromJson(json, type);
    }

    public static ArrayList<String> getAllInvitePeople(String invitePeopleListJson) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(invitePeopleListJson);
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = (JSONObject) json.get(i);
                if (jsonObject.has("")) {
                    arrayList.add(jsonObject.getString("personMeetingId"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
