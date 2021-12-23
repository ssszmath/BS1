package ir.s.s.bs1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import ir.s.s.bs1.Networking.NetworkModel;
import ir.s.s.bs1.Networking.NetworkPresenter;
import ir.s.s.bs1.Networking.NetworkView;

public class BSService extends Service implements NetworkView {


    NotificationManager notificationManager;
    private final int NOTIFICATION_ID=124;
    private final String CHANEL_ID="BETASPORT";

    static PublishSubject<String> data = PublishSubject.create();


    NetworkPresenter networkPresenter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onCreate() {
        super.onCreate();
        networkPresenter=new NetworkModel(this);
        BSTimer();
        startFForeground();
        notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }


    private void startFForeground(){
        String channelId="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId=createNotificationChannel(CHANEL_ID, "BS Background Service");
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("وضعیت دریافت سرویس فعال است")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        this.startForeground(NOTIFICATION_ID,notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId,String channelName){
        NotificationChannel chan=new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager serv=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        serv.createNotificationChannel(chan);
        return channelId;
    }


    void showRequestNotification(String title,String des) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String chId = "BETASPORT";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(chId, "BS", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(getApplicationContext(), chId)
                .setContentText(des)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notificationCompat.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_REDELIVER_INTENT;
    }


    void BSTimer(){
//        40*60*1000
        //Time 15  Min
        new CountDownTimer(15*60*1000, 1000) {

            public void onTick(long millisUntilFinished) {
//                Log.v("TTT","S");
            }

            public void onFinish() {
                BSTimer();
                //TODO Send Server Request
                networkPresenter.loadDataFromServer(null,"","BS_EVENT");
                System.out.println("Screen off " + "UNLOCKED");

            }
        }.start();
    }

    @Override
    public void successLoad(JSONObject jsonObject, String event) {
        try {
            if(event.equals("BS_EVENT")){
                showRequestNotification("BS",jsonObject.getString("message"));
//                BSData.instance.setText(jsonObject.getString("message"));
                data.onNext(jsonObject.getString("message"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void errorLoad(String error_text, String event) {
        showRequestNotification("BS",error_text);
//        BSData.instance.setText(error_text);
    }



    public static Observable<String> getObservable(){
        return data;
    }



}
