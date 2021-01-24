package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import az.nms.pizzamizza.Constants;
import az.nms.pizzamizza.InitializeMenuAsynctask;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.SharedData;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.JSONParser;
import az.nms.pizzamizza.tools.UIHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoadingScreenActivity extends Activity {

    private ImageView gyroView, img;
    private TextView gyroText;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        SharedData.init(this);
        Utilites.setLocale(this, SharedData.getLanguage());

        setContentView(R.layout.splash_layout);
        gyroView = (ImageView) findViewById(R.id.gyro);
        gyroText = (TextView) findViewById(R.id.splash_text);
        img = (ImageView) findViewById(R.id.splash);

        StartAnimations();


    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                img.setVisibility(View.GONE);
                gyroText.setVisibility(View.VISIBLE);
                gyroView.setVisibility(View.VISIBLE);
                gyroView.setBackgroundResource(R.drawable.gyro_animation);
                AnimationDrawable gyroAnimation = (AnimationDrawable) gyroView
                        .getBackground();
                gyroAnimation.start();

                if (SharedData.isFirstRun())
                    new InitializeMenuAsynctask(LoadingScreenActivity.this, InitializeMenuAsynctask.ACTION_INIT).execute();
                else {
                    new CheckUpdates().execute();
                }


            }
        });

        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    class CheckUpdates extends AsyncTask<String, String, Integer> {

        private JSONParser jParser = new JSONParser();

        private String updateTime = "";

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                List<NameValuePair> params = new ArrayList<>();

                params.add(new BasicNameValuePair("db", "timestamp"));
                params.add(new BasicNameValuePair("time", SharedData.getLastUpdateDate()));

                JSONObject json = jParser.makeHttpRequest(Constants.SERVER_GET, "GET", params);

                if (json != null) {

                    Log.d("store open", json.toString());
                    int success = json.getInt("success");


                    if (success == 0)
                        return 0;
                    else {
                        updateTime = json.getString("time");
                        return 1;
                    }


                } else
                    return null;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer time) {
            super.onPostExecute(time);

            if (time == null) {
                onCreateDialog().show();
            } else if (time == 1) {
                new InitializeMenuAsynctask(LoadingScreenActivity.this, InitializeMenuAsynctask.ACTION_INIT).execute();
                SharedData.setLastUpdateDate(updateTime);
            } else {
                Intent i = new Intent(LoadingScreenActivity.this, MainPageActivity.class);
                startActivity(i);
                finish();
            }

        }
    }

    private MaterialDialog.Builder onCreateDialog() {


        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this)
                .title(R.string.connection)
                .content(R.string.unable_to_connect_toast)
                .titleColorRes(R.color.app_base_color)
                .contentColorRes(R.color.dialog_text_color)
                .positiveColorRes(R.color.app_base_color)
                .negativeColorRes(R.color.app_base_accent)
                .positiveText(R.string.re_try)
                .negativeText(R.string.exit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        new CheckUpdates().execute();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        UIHelper.killApp(true);
                    }
                });

        return dialog;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
