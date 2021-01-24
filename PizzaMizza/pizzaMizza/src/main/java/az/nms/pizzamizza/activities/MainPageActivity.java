package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;

import az.nms.pizzamizza.PizzaMizzaService;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.SharedData;
import az.nms.pizzamizza.tools.Utilites;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainPageActivity extends Activity {

    Button delivery, carry_out;
    ImageView welcomeimage, localeButtonAZ, localeButtonEN, localeButtonRU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedData.init(this);

        initLayout();

        startService(new Intent(this, PizzaMizzaService.class));

    }

    private void initLayout() {

        welcomeimage = (ImageView) findViewById(R.id.main_page_head);

        localeButtonAZ = (ImageView) findViewById(R.id.locale_az_button);
        localeButtonEN = (ImageView) findViewById(R.id.locale_en_button);
        localeButtonRU = (ImageView) findViewById(R.id.locale_ru_button);

        localeButtonAZ.setOnClickListener(onClickLanguage);
        localeButtonEN.setOnClickListener(onClickLanguage);
        localeButtonRU.setOnClickListener(onClickLanguage);

        if (SharedData.getLanguage().equalsIgnoreCase("az"))
            welcomeimage.setImageResource(R.drawable.welcome_az);
        else if (SharedData.getLanguage().equalsIgnoreCase("ru"))
            welcomeimage.setImageResource(R.drawable.welcome_ru);
        else
            welcomeimage.setImageResource(R.drawable.welcome);

        delivery = (Button) findViewById(R.id.delivery);

        carry_out = (Button) findViewById(R.id.carry_out);

        carry_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(MainPageActivity.this, HomeScreenActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(i);
                finish();

            }
        });

        delivery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(MainPageActivity.this, DeliveryAddressActivity.class);
                startActivity(i);

            }
        });
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        onCreateDialog().show();
    }


    View.OnClickListener onClickLanguage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.locale_az_button:
                    Utilites.setLocale(MainPageActivity.this, Utilites.LANGUAGE_AZ);
                    break;
                case R.id.locale_en_button:
                    Utilites.setLocale(MainPageActivity.this, Utilites.LANGUAGE_EN);
                    break;
                case R.id.locale_ru_button:
                    Utilites.setLocale(MainPageActivity.this, Utilites.LANGUAGE_RU);
                    break;

            }

            Intent i = getIntent();

            finish();
            startActivity(i);
            overridePendingTransition(0, 0);


//            new InitializeMenuAsynctask(MainPageActivity.this, InitializeMenuAsynctask.ACTION_RELOAD).execute();

        }
    };


    private MaterialDialog.Builder onCreateDialog() {


        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this)
                .content(R.string.app_exit_message)
                .titleColorRes(R.color.app_base_color)
                .contentColorRes(R.color.dialog_text_color)
                .positiveColorRes(R.color.app_base_color)
                .negativeColorRes(R.color.app_base_accent)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                });

        return dialog;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
