package james.crashersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import james.crasher.Crasher;

public class MainActivity extends AppCompatActivity {

    private Crasher crasher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crasher = new Crasher(this);
        crasher.setEmail("18jafenn90@gmail.com");

        findViewById(R.id.nullPointer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) null).setText("Hi!");
            }
        });
    }
}
