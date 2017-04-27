package james.crasher.activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import james.buttons.Button;
import james.crasher.R;
import james.crasher.utils.ColorUtils;
import james.crasher.utils.ImageUtils;

public class CrashActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_NAME = "james.crasher.EXTRA_NAME";
    public static final String EXTRA_MESSAGE = "james.crasher.EXTRA_MESSAGE";
    public static final String EXTRA_STACK_TRACE = "james.crasher.EXTRA_STACK_TRACE";

    public static final String EXTRA_EMAIL = "james.crasher.EXTRA_EMAIL";
    public static final String EXTRA_COLOR = "james.crasher.EXTRA_COLOR";

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView name;
    private TextView message;
    private TextView description;
    private Button copy;
    private Button share;
    private Button email;
    private View stackTraceHeader;
    private ImageView stackTraceArrow;
    private TextView stackTrace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        message = (TextView) findViewById(R.id.message);
        description = (TextView) findViewById(R.id.description);
        copy = (Button) findViewById(R.id.copy);
        share = (Button) findViewById(R.id.share);
        email = (Button) findViewById(R.id.email);
        stackTraceHeader = findViewById(R.id.stackTraceHeader);
        stackTraceArrow = (ImageView) findViewById(R.id.stackTraceArrow);
        stackTrace = (TextView) findViewById(R.id.stackTrace);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        int color = getIntent().getIntExtra(EXTRA_COLOR, ContextCompat.getColor(this, R.color.colorPrimary));
        int colorDark = ColorUtils.darkColor(color);
        boolean isColorDark = ColorUtils.isColorDark(color);

        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(isColorDark ? Color.WHITE : Color.BLACK);

        copy.setBackgroundColor(colorDark);
        copy.setTextColor(colorDark);
        copy.setOnClickListener(this);

        share.setBackgroundColor(colorDark);
        share.setTextColor(colorDark);
        share.setOnClickListener(this);

        email.setBackgroundColor(colorDark);
        email.setTextColor(colorDark);
        if (getIntent().hasExtra(EXTRA_EMAIL)) {
            email.setOnClickListener(this);
        } else email.setVisibility(View.GONE);

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
        description.setText(String.format(Locale.getDefault(), getString(R.string.msg_crashed), getString(R.string.app_name)));

        stackTrace.setText(getIntent().getStringExtra(EXTRA_STACK_TRACE));
        stackTraceHeader.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.copy) {
            Object service = getSystemService(CLIPBOARD_SERVICE);
            if (service instanceof android.content.ClipboardManager)
                ((android.content.ClipboardManager) service).setPrimaryClip(ClipData.newPlainText(name.getText().toString(), stackTrace.getText().toString()));
            else if (service instanceof android.text.ClipboardManager)
                ((android.text.ClipboardManager) service).setText(stackTrace.getText().toString());
        } else if (v.getId() == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, stackTrace.getText().toString()); //TODO: include device info, app name, build, version, etc

            startActivity(Intent.createChooser(intent, getString(R.string.action_share)));
        } else if (v.getId() == R.id.email) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:" + getIntent().getStringExtra(EXTRA_EMAIL)));
            intent.putExtra(Intent.EXTRA_EMAIL, getIntent().getStringExtra(EXTRA_EMAIL));
            intent.putExtra(Intent.EXTRA_SUBJECT, String.format(Locale.getDefault(), getString(R.string.title_email), name.getText().toString(), getString(R.string.app_name)));
            intent.putExtra(Intent.EXTRA_TEXT, stackTrace.getText().toString()); //TODO: include device info, app name, build, version, etc

            startActivity(Intent.createChooser(intent, getString(R.string.action_send_email)));
        } else if (v.getId() == R.id.stackTraceHeader) {
            if (stackTrace.getVisibility() == View.GONE) {
                stackTrace.setVisibility(View.VISIBLE);
                stackTraceArrow.animate().rotation(180).start();
            } else {
                stackTrace.setVisibility(View.GONE);
                stackTraceArrow.animate().rotation(0).start();
            }
        }
    }
}
