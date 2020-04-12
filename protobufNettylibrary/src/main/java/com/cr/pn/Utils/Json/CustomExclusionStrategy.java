package com.cr.pn.Utils.Json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by zy on 2017/7/20.
 */
public class CustomExclusionStrategy  implements ExclusionStrategy {


    public CustomExclusionStrategy() {
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(RemoveExclus.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
