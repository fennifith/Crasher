package james.crasher.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import james.buttons.Button;
import james.crasher.R;
import james.crasher.utils.ColorUtils;
import james.crasher.utils.ImageUtils;

public class CrashActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "james.crasher.EXTRA_NAME";
    public static final String EXTRA_MESSAGE = "james.crasher.EXTRA_MESSAGE";
    public static final String EXTRA_STACK_TRACE = "james.crasher.EXTRA_STACK_TRACE";

    public static final String EXTRA_EMAIL = "james.crasher.EXTRA_EMAIL";
    public static final String EXTRA_COLOR = "james.crasher.EXTRA_COLOR";

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView name;
    private TextView message;
    private TextView textView;
    private Button email;
    private Button share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        message = (TextView) findViewById(R.id.message);
        email = (Button) findViewById(R.id.email);
        share = (Button) findViewById(R.id.share);
        textView = (TextView) findViewById(R.id.stackTrace);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        int color = getIntent().getIntExtra(EXTRA_COLOR, ContextCompat.getColor(this, R.color.colorPrimary));
        int colorDark = ColorUtils.darkColor(color);
        boolean isColorDark = ColorUtils.isColorDark(color);

        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(isColorDark ? Color.WHITE : Color.BLACK);

        share.setBackgroundColor(colorDark);
        share.setTextColor(colorDark);

        email.setBackgroundColor(colorDark);
        email.setTextColor(colorDark);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(String.format(Locale.getDefault(), getString(R.string.title_crashed), getString(R.string.app_name)));
            actionBar.setHomeAsUpIndicator(ImageUtils.getVectorDrawable(this, R.drawable.ic_back, isColorDark ? Color.WHITE : Color.BLACK));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(colorDark);
            getWindow().setNavigationBarColor(colorDark);
        }

        name.setText(getIntent().getStringExtra(EXTRA_NAME));
        message.setText(getIntent().getStringExtra(EXTRA_MESSAGE));
        textView.setText(getIntent().getStringExtra(EXTRA_STACK_TRACE));

        if (getIntent().hasExtra(EXTRA_EMAIL)) {
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: send email
                    Toast.makeText(v.getContext(), getIntent().getStringExtra(EXTRA_EMAIL), Toast.LENGTH_SHORT).show();
                }
            });
        } else email.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
