package james.crashersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import james.crasher.Crasher;

public class MainActivity extends AppCompatActivity {

    private Crasher crasher;

    private SwitchCompat stackOverflowSwitch;
    private SwitchCompat crashActivitySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crasher = new Crasher(this);
        crasher.setEmail("18jafenn90@gmail.com");

        stackOverflowSwitch = (SwitchCompat) findViewById(R.id.stackOverflow);
        crashActivitySwitch = (SwitchCompat) findViewById(R.id.crashActivity);

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
    }
}
