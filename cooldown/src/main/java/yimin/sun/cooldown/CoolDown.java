package yimin.sun.cooldown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yzsh-sym on 2017/9/11.
 */

public class CoolDown {

    private static final String ACTION = "yimin.sun.cooldown.COOL_DOWN_ACTION";
    private static final String KEY_TAG = "KEY_TAG";
    private static final String KEY_REMAINING_SECONDS = "KEY_REMAINING_SECONDS";

    public interface Listener {
        void onRemainingSecondsChange(int remainingSeconds);
    }

    private static Handler handler;

    private static Map<String, CoolDownInfo> map;

    private static void init(final Context applicationContext) {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // tag
                String tag = (String) msg.obj;

                // calculate remaining seconds
                CoolDownInfo info = map.get(tag);
                int remainingSeconds = calculateRemainingSeconds(tag);

                Intent intent = new Intent(ACTION);
                intent.putExtra(KEY_TAG, tag);
                intent.putExtra(KEY_REMAINING_SECONDS, remainingSeconds);
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);

                if (remainingSeconds > 0) {
                    Message next = Message.obtain();
                    next.obj = tag;
                    handler.sendMessageDelayed(next, 1000);
                } else {
                    // remove from map
                    map.remove(tag);
                }
                return true;
            }
        });

        map = new HashMap<>();
    }


    private static class CoolDownInfo {
        CoolDownInfo(long startTsMillis, int spanInSeconds) {
            this.startTsMillis = startTsMillis;
            this.spanInSeconds = spanInSeconds;
        }

        long startTsMillis;
        int spanInSeconds;
    }


    private static int calculateRemainingSeconds(String tag) {
        CoolDownInfo info = map.get(tag);
        long passedInMillis = System.currentTimeMillis() - info.startTsMillis;
        long remainingInMillis = info.spanInSeconds * 1000L - passedInMillis;
        if (remainingInMillis < 0) {
            remainingInMillis = 0;
        }
        return (int) (remainingInMillis / 1000);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String tag = intent.getStringExtra(KEY_TAG);
            if (CoolDown.this.tag.equals(tag) && CoolDown.this.listener != null) {
                int remainingSeconds = intent.getIntExtra(KEY_REMAINING_SECONDS, 9999);
                listener.onRemainingSecondsChange(remainingSeconds);
            }
        }
    };

    private String tag; //通过构造器确保不为null
    private int timeSpanSeconds;
    private Listener listener;
    private LocalBroadcastManager lbm;

    public CoolDown(Context context) {
        if (map == null) {
            init(context.getApplicationContext());
        }
        lbm = LocalBroadcastManager.getInstance(context);
    }

    public void bind(String tag, int timeSpanSeconds, Listener listener) {
        if (tag == null) {
            throw new IllegalArgumentException("tag must not be null!");
        }

        this.tag = tag;
        this.timeSpanSeconds = timeSpanSeconds;
        this.listener = listener;

        lbm.registerReceiver(receiver, new IntentFilter(ACTION));
    }

    public void unBind() {
        lbm.unregisterReceiver(receiver);
    }

    public void startCoolDown() {
        CoolDownInfo info = map.get(tag);
        if (info == null) {
            // 为null意味着这个tag的cool down没有正在进行
            info = new CoolDownInfo(System.currentTimeMillis(), timeSpanSeconds);
            map.put(tag, info);

            Message msg1 = Message.obtain();
            msg1.obj = tag;
            handler.sendMessage(msg1);
        } else {
            // 这个tag的cool down已经在进行，此时什么也不做
        }
    }

    public boolean isCoolingDown() {
        return map.get(tag) != null;
    }

    public int getRemainingSeconds() {
        if (isCoolingDown()) {
            return calculateRemainingSeconds(tag);
        } else {
            return -1;
        }
    }
}
