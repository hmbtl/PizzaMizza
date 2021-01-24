package az.nms.pizzamizza;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by anar on 6/3/15.
 */
public class PizzaMizzaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HelveticaNeue-Condensed.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        //....
    }
}
