package com.tonkia.utils;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AudioPlayUtils {


    private boolean isPlaying;
    private ExecutorService mExecutorService;
    private MediaPlayer mediaPlayer;
    private AudioPlayerListener myListener;
    private Handler mHandler = new Handler();

    public interface AudioPlayerListener {
        public void onError();

        public void onComplete();

        public void currentPosition(int position);

        public void onStart(int total);
    }

    public void setAudioPlayerListener(AudioPlayerListener myListener) {
        this.myListener = myListener;
    }

    public AudioPlayUtils() {
        mExecutorService = Executors.newSingleThreadExecutor();
        isPlaying = false;
    }

    public void playAudio(final File mFile) {
        if (null != mFile && !isPlaying && myListener != null) {
            isPlaying = true;
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    startPlay(mFile);
                }
            });
        }
    }


    private void startPlay(File mFile) {
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(mFile.getAbsolutePath());
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
            int total = mediaPlayer.getDuration();
            myListener.onStart(total);
            updatePositon();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }

    }

    private int SPACE = 100;
    private Runnable update = new Runnable() {
        @Override
        public void run() {
            updatePositon();
        }
    };

    private void updatePositon() {
        if (mediaPlayer != null) {
            int positon = mediaPlayer.getCurrentPosition();
            myListener.currentPosition(positon);
            mHandler.postDelayed(update, SPACE);
        }
    }

    private void playEndOrFail(boolean isEnd) {
        isPlaying = false;
        if (isEnd) {
            myListener.onComplete();
        } else {
            myListener.onError();
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
