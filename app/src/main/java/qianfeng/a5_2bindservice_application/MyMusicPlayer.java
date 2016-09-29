package qianfeng.a5_2bindservice_application;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class MyMusicPlayer extends Service {

    private MediaPlayer mediaPlayer;

    // 绑定Service的时候，是调用onCreate(),以及 onBind()
    // 解绑定的时候，是调用onUnbind(),以及 onDestory()
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("google-my:", "onCreate: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("google-my:", "onBind: ");
        return new MyBinder(); // 待会返过去绑定它的Activity中的绑定成功的方法

    }

    public class MyBinder extends Binder {

        public MyMusicPlayer getInstance()
        {
            Log.d("google-my:", "getInstance: MyMusicPlayer.this:"+MyMusicPlayer.this.hashCode());
            return MyMusicPlayer.this;
        }

    }

    public void initMusicPlayer()
    {
        mediaPlayer = new MediaPlayer(); // 因为绑定的Service每次都会在Activity销毁时自动解绑，所以再点击进入应用时，
         // 绑定的Service的MyBinder每次都不一样了，这个new MediaPlayer(),是每次点击play()方法，都会重新初始化的，这个是不可捕捉的，
        // 所以只能在当次的Activity销毁，当次的Service解绑时，清除掉这个当次new出来的MediaPlayer，这样应用后，歌曲的播放就马上停止，
        // 解决了再次进入应用有两首歌同时播的问题。

        Log.d("google-my:", "initMusicPlayer:new MediaPlayer();hashCode(): "+mediaPlayer.hashCode());

        try {
            mediaPlayer.setDataSource(this, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"m1.mp3")));
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public void play()
    {
        if(mediaPlayer == null)
        {
            initMusicPlayer();
        }
        mediaPlayer.start();
    }

    public void pause()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
        }
    }

    public void stop()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(mediaPlayer != null)
        {
            Log.d("google-my:", "onUnbind: if(mediaPlayer!=null)" + mediaPlayer.hashCode());
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.d("google-my:", "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("google-my:", "onDestroy: ");
    }
}
