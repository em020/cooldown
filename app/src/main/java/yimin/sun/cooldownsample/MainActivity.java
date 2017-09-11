package yimin.sun.cooldownsample;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yimin.sun.cooldown.CoolDown;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    Button button1, button2, button3;

    CoolDown coolDown1, coolDown2, coolDown3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button1 = (Button) findViewById(R.id.b1);
        button2 = (Button) findViewById(R.id.b2);
        button3 = (Button) findViewById(R.id.b3);


        coolDown1 = new CoolDown(this);
        coolDown1.bind("1", 15, new CoolDown.Listener() {
            @Override
            public void onRemainingSecondsChange(int remainingSeconds) {
                if (remainingSeconds > 0) {
                    button1.setEnabled(false);
                    button1.setText("Remaining: " + remainingSeconds);
                } else {
                    button1.setEnabled(true);
                    button1.setText("我是B1，点我啊");
                }
            }
        });

        if (coolDown1.isCoolingDown()) {
            button1.setEnabled(false);
            button1.setText("Remaining: " + coolDown1.getRemainingSeconds());
        } else {
            button1.setEnabled(true);
            button1.setText("我是B1，点我啊");
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setEnabled(false);
                coolDown1.startCoolDown();
            }
        });


        coolDown2 = new CoolDown(this);
        coolDown2.bind("1", 20, new CoolDown.Listener() {
            @Override
            public void onRemainingSecondsChange(int remainingSeconds) {
                if (remainingSeconds > 0) {
                    button2.setEnabled(false);
                    button2.setText("Remaining: " + remainingSeconds);
                } else {
                    button2.setEnabled(true);
                    button2.setText("我是B2，点我啊");
                }
            }
        });

        if (coolDown2.isCoolingDown()) {
            button2.setEnabled(false);
            button2.setText("Remaining: " + coolDown2.getRemainingSeconds());
        } else {
            button2.setEnabled(true);
            button2.setText("我是B2，点我啊");
        }

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.setEnabled(false);
                coolDown2.startCoolDown();
            }
        });


        coolDown3 = new CoolDown(this);
        coolDown3.bind("3", 25, new CoolDown.Listener() {
            @Override
            public void onRemainingSecondsChange(int remainingSeconds) {
                if (remainingSeconds > 0) {
                    button3.setEnabled(false);
                    button3.setText("Remaining: " + remainingSeconds);
                } else {
                    button3.setEnabled(true);
                    button3.setText("我是B3，点我啊");
                }
            }
        });

        if (coolDown3.isCoolingDown()) {
            button3.setEnabled(false);
            button3.setText("Remaining: " + coolDown3.getRemainingSeconds());
        } else {
            button3.setEnabled(true);
            button3.setText("我是B3，点我啊");
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3.setEnabled(false);
                coolDown3.startCoolDown();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coolDown1.unBind();
        coolDown2.unBind();
        coolDown3.unBind();
    }


}
