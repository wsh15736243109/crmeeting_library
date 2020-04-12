package com.cr.pn.Utils.Media;

import android.media.MediaPlayer;

import java.io.IOException;


/**
 * Created by zy on 2017/7/15.
 * 音频媒体播放.
 */
public class MediaMp3 {
    private MediaMp3 mediaPlayer = null;
    private MediaPlayer player;
    private boolean loop = false;

    private static class MediaMp3Help {
        static MediaMp3 instance = new MediaMp3();
    }

    public static MediaMp3 getInstance() {
        return MediaMp3.MediaMp3Help.instance;
    }


    /**
     * 播放准备.
     * @return
     */
    public MediaMp3 ready(){
        if (player == null) {
            player = new MediaPlayer();
        } else {
            if (player.isPlaying()) {
                player.stop();
                try {
                    player.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player = null;
            }
            player = new MediaPlayer();
        }
        return mediaPlayer;
    }

    /**
     * 流媒体加载.
     * @param path
     */
    public void playPrepare(String path){
        try {
            player.setDataSource(path);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放.
     * @return
     */
    public MediaMp3 start(){
        if (!player.isPlaying()&&player!=null){
            player.setLooping(loop);
            player.start();
        }
        return mediaPlayer;
    }

    /**
     * 播放暂停.
     * @return
     */
    public  MediaMp3 pausePlay() {
        player.pause();
        return mediaPlayer;
    }

    /**
     * 停止播放.
     * @return
     */
    public MediaMp3 stop(){
        if (player.isPlaying()&&player!=null) {
            player.stop();
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player = null;
        }
        return mediaPlayer;
    }

    /**
     * 释放资源.
     */
    public void destroy(){
        if (player != null && player.isPlaying()) {
            player.stop();
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player = null;
        }
    }

    /**
     * 播放完成时的回调.
     * @param onCompletionListener
     */
    public void playResult(MediaPlayer.OnCompletionListener onCompletionListener){
        player.setOnCompletionListener(onCompletionListener);
    }

    /**
     * 使用seekTo后的回调.
     */
    public void playSeekTo(MediaPlayer.OnSeekCompleteListener listener){
        player.setOnSeekCompleteListener(listener);
    }

    /**
     * 流媒体加载完成后的回调.
     */
    public void playPrepared(MediaPlayer.OnPreparedListener listener){
        player.setOnPreparedListener(listener);
    }

    /**
     * 播放出错时候的回调.
     */
    public void playError(MediaPlayer.OnErrorListener listener){
        player.setOnErrorListener(listener);
    }

    /**
     * 获取时长.
     * @return
     */
    public int getTime(){
        return player.getDuration();
    }

    /**
     * 获取当前播放的位置.
     * @return
     */
    public int getTimeing(){
        return player.getCurrentPosition();
    }

    /**
     * 设置播放的位置.
     * @param seek
     */
    public void setSeek(int seek){
        player.seekTo(seek);
    }

    /**
     * 设置是否循环.
     * @param loop
     */
    public void setLooping(boolean loop){
        this.loop = loop;
        if (player!=null){
            player.setLooping(loop);
        }
    }

    /**
     * 判断是否循环播放.
     * @return
     */
    public boolean isLoop(){
        return player.isLooping();
    }

    /**
     * 获取player.
     * @return
     */
    public MediaPlayer getPlayer(){
        return player;
    }

}
