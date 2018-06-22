package james.crashersample;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import james.crasher.Crasher;

public class MainActivity extends AppCompatActivity implements Crasher.OnCrashListener {

    private Crasher crasher;

    private SwitchCompat stackOverflowSwitch;
    private SwitchCompat crashActivitySwitch;
    private SwitchCompat backgroundSwitch;
    private AppCompatButton colorButton;

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crasher = new Crasher(this)
                .addListener(this)
                .setEmail("18jafenn90@gmail.com");

        stackOverflowSwitch = findViewById(R.id.stackOverflow);
        crashActivitySwitch = findViewById(R.id.crashActivity);
        backgroundSwitch = findViewById(R.id.background);
        colorButton = findViewById(R.id.color);

        findViewById(R.id.nullPointer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) null).setText("Hi!");
            }
        });

        stackOverflowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crasher.setForceStackOverflow(isChecked);
            }
        });

        crashActivitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crasher.setCrashActivityEnabled(isChecked);
            }
        });

        backgroundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                crasher.setBackgroundCrash(b);
            }
        });

        setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color++;
                switch (color % 5) {
                    case 0:
                        setColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                        break;
                    case 1:
                        setColor(Color.parseColor("#009688"));
                        break;
                    case 2:
                        setColor(Color.parseColor("#43A047"));
                        break;
                    case 3:
                        setColor(Color.parseColor("#FF5722"));
                        break;
                    case 4:
                        setColor(Color.parseColor("#F44336"));
                        break;
                }
            }
        });
    }

    private void setColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorButton.setBackgroundTintList(ColorStateList.valueOf(color));
        } else colorButton.setTextColor(color);
        crasher.setColor(color);
    }

    @Override
    public void onCrash(Thread thread, Throwable throwable) {
        Log.d("MainActivity", "Exception thrown: " + throwable.getClass().getName());
    }
}
