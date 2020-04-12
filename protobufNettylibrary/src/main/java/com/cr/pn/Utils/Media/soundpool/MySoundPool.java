package com.cr.pn.Utils.Media.soundpool;

import android.media.SoundPool;

import androidx.collection.ArrayMap;

/**
 * Created by zy on 2017/10/28.
 */

public class MySoundPool {

    /**
     * 音乐池map.
     */
    private static ArrayMap<Integer,SoundPool> soundPoolBuildHashMap;

    /**
     * 当前音乐池.
     */
    private static SoundPool thisSoundPool;

    static {
        soundPoolBuildHashMap = new ArrayMap<>();
    }

    public static SoundPoolBuild build(SoundPoolBuild poolBuild){
        thisSoundPool = new SoundPool(poolBuild.getTypeMax(),poolBuild.getType(),poolBuild.getQuality());
        poolBuild.setSoundPoolId(soundPoolBuildHashMap.size());
        soundPoolBuildHashMap.put(soundPoolBuildHashMap.size(),thisSoundPool);
        return poolBuild;
    }

    /**
     * 获取所有音乐池,
     * @return
     */
    public static ArrayMap<Integer, SoundPool> getSoundPoolBuildHashMap() {
        return soundPoolBuildHashMap;
    }

    /**
     * 获取当前音乐池.
     * @return
     */
    public static SoundPool getThisSoundPool() {
        return thisSoundPool;
    }

    /**
     * 设置当前音乐池.
     * @param thisSoundPool
     */
    public static void setThisSoundPool(SoundPool thisSoundPool) {
        MySoundPool.thisSoundPool = thisSoundPool;
    }

    /**
     * 移除soundPool.
     * @param soundPoolId
     */
    public static void removeSoundPool(Integer soundPoolId){
        SoundPool soundPool = soundPoolBuildHashMap.get(soundPoolId);
        if(soundPool!=null){
            soundPool.release();
        }
        soundPoolBuildHashMap.remove(soundPoolId);
    }
}
