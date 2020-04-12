package com.cr.pn.Utils.Media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.collection.ArrayMap;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zy on 2017/7/21.
 */

public class MediaScreenView extends SurfaceView implements SurfaceHolder.Callback {

    public enum MediaModel{
        NEW,
        CREATE
    }

    private SurfaceHolder surfaceHolder;
    private MediaPlayer player;
    private String path = "";
    private boolean loop = false;
    private boolean isStart = false;
    private Map<String,String> map;
    private MediaPlayer.OnPreparedListener onPreparedListener;
    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener;
    private MediaPlayer.OnErrorListener onErrorListener;
    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener;
    private MediaPlayer.OnCompletionListener onCompletionListener;
    private MediaModel mediaMode = MediaModel.NEW;

    public MediaScreenView(Context context) {
        super(context);
        init();
    }

    public MediaScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediaScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMediaMode(MediaModel mediaMode){
        this.mediaMode = mediaMode;
    }

    private void init(){
        map = new ArrayMap<>();
        surfaceHolder = getHolder();
//        player = new MediaPlayer();
    }

    public void first(String path){
        isStart = true;
        this.path = path;
        ready();
        surfaceHolder.addCallback(this);
    }

    public void first(){
        first(path);
    }

    /**
     * 播放准备.
     * @return
     */
    private void ready(){
        switch (mediaMode){
            case NEW:
                readyNew();
                break;
            case CREATE:
                readyCreate();
                break;
        }
        initListener();
    }

    /**
     * 初始化mediaPlayer.
     */
    private void readyNew(){
        if (player == null) {
            player = new MediaPlayer();
        } else {
            if (player.isPlaying()) {
                player.stop();
            }
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player = null;
            player = new MediaPlayer();
        }
    }

    private void readyCreate(){
        if (player == null) {
            player = MediaPlayer.create(getContext(), Uri.parse(this.path));
        } else {
            if (player.isPlaying()) {
                player.stop();
            }
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player = null;
            player = MediaPlayer.create(getContext(), Uri.parse(this.path));
        }
    }

    private void initListener(){
        if (onPreparedListener!=null){
            player.setOnPreparedListener(onPreparedListener);
        }
        if (onSeekCompleteListener!=null){
            player.setOnSeekCompleteListener(onSeekCompleteListener);
        }
        if (onErrorListener!=null){
            player.setOnErrorListener(onErrorListener);
        }
        if (onBufferingUpdateListener!=null){
            player.setOnBufferingUpdateListener(onBufferingUpdateListener);
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!loop){
                    isStart = false;
                }
                if (onCompletionListener!=null){
                    onCompletionListener.onCompletion(mp);
                }
            }
        });
    }

    /**
     * 流媒体加载.
     */
    public void playPrepare(){
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放.
     * @return
     */
    public void start(){
        if (player!=null&&!player.isPlaying()){
            player.setLooping(loop);
            player.start();
        }
    }

    /**
     * 播放暂停.
     * @return
     */
    public  void pausePlay() {
        player.pause();
    }

    /**
     * 停止播放.
     * @return
     */
    public void stop(){
        if (player.isPlaying()&&player!=null) {
            player.stop();
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player = null;
        }
        isStart = false;
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
        isStart = false;
    }

    /**
     * 判断是否进入播放.
     * @return
     */
    public boolean isPlay(){
        return isStart;
    }

    /**
     * 播放完成时的回调.
     * @param onCompletionListener
     */
    public void playResult(final MediaPlayer.OnCompletionListener onCompletionListener){
       this.onCompletionListener = onCompletionListener;
    }

    /**
     * 使用seekTo后的回调.
     */
    public void playSeekTo(MediaPlayer.OnSeekCompleteListener listener){
        this.onSeekCompleteListener = listener;
    }

    /**
     * 流媒体加载完成后的回调.
     */
    public void playPrepared(MediaPlayer.OnPreparedListener listener){
        this.onPreparedListener = listener;
    }

    /**
     * 播放出错时候的回调.
     */
    public void playError(MediaPlayer.OnErrorListener listener){
        this.onErrorListener = listener;
    }

    /**
     * 追踪流播放的缓冲的状态.
     * @param bufferingUpdateListener
     */
    public void bufferUpdateListener(MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener){
        this.onBufferingUpdateListener = bufferingUpdateListener;
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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(surfaceHolder);
        if (mediaMode== MediaModel.NEW){
            playPrepare();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroy();
    }

    public String getPath() {
        return path;
    }
}
