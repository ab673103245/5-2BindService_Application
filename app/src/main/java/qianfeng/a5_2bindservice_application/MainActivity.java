package qianfeng.a5_2bindservice_application;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private MyMusicPlayer myMusicPlayer;
    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void bindService(View view) { // 绑定Service

    }

    public void unbindService(View view) { // 解绑定
        unbindService(conn);
    }

    public void play(View view) { // 播放

        conn = new ServiceConnection() {

            // 绑定成功后，会调用这个方法
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myMusicPlayer = ((MyMusicPlayer.MyBinder) service).getInstance();
                Log.d("google-my:", "onServiceConnected: " + myMusicPlayer.hashCode());
                myMusicPlayer.play();
            }

            // 解绑成功后，会调用这个方法
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, MyMusicPlayer.class),  // 这里是用反射，来获取MyMusicPlayer的实例,是ActivityManage里面帮你管理的，只管反射获取到MyMusicPlayer实例就行，它会帮你赋值什么的。
                conn,BIND_AUTO_CREATE // 这个常量是 表示如果没有绑定会自动帮你创建一个绑定，如果绑定了就不用。
        );

    }

    public void pause(View view) { // 暂停
        myMusicPlayer.pause();
    }

    public void stop(View view) { // 停止
        myMusicPlayer.stop();
    }
}
