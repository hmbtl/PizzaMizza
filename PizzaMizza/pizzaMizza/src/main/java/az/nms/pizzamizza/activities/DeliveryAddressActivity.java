package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.SharedData;
import az.nms.pizzamizza.models.DeliveryArea;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DeliveryAddressActivity extends Activity {

    private Button save_button;
    private Spinner locations;
    private CheckBox save_details;
    private List<DeliveryArea> deliveryAreas;

    private PizzaMizzaDatabase db = new PizzaMizzaDatabase(this);

    public static final String ACTION_DELIVERY = "delivery";

    private EditText streetName, aptNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_layout);

        initLayout();

        if (SharedData.getSaveDetails()) {
            loadPrefs();
        }
    }

    private void initLayout() {

        Utilites.initAppBarFont(this);
        Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);
        Utilites.hideButton(this, Utilites.ACTION_BAR_HOME);


        locations = (Spinner) findViewById(R.id.location_zone);
        save_button = (Button) findViewById(R.id.deliver_save);
        save_button.setOnClickListener(onClick);

        save_details = (CheckBox) findViewById(R.id.save_details_checkbox);

        streetName = (EditText) findViewById(R.id.location_street);
        aptNumber = (EditText) findViewById(R.id.location_apt);


        deliveryAreas = new LinkedList<>();
        deliveryAreas = db.getDeliveryAreas();

        /* Add selection message to beginning of the list */
        deliveryAreas.add(0, new DeliveryArea(0, this.getResources().getString(R.string.select_zone), 0));


        ArrayAdapter<DeliveryArea> spinnerArrayAdapter =
                new ArrayAdapter<>(DeliveryAddressActivity.this, android.R.layout.simple_spinner_item, deliveryAreas); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locations.setAdapter(spinnerArrayAdapter);

    }

    private void loadPrefs() {
        streetName.setText(SharedData.getStreetName());
        aptNumber.setText(SharedData.getAptNumber());
        save_details.setChecked(SharedData.getSaveDetails());

        locations.setSelection(SharedData.getZoneId());
    }


    private boolean isAllFilled(EditText[] edit) {
        for (int i = 0; i < edit.length; i++) {
            if (edit[i].getText().toString().matches("") || edit[i] == null) {
                switch (edit[i].getId()) {
                    case R.id.location_apt:
                        Utilites.messageDialog(DeliveryAddressActivity.this, R.string.fill_apt_name_message, R.string.apartment, R.string.ok).show();
                        break;

                    case R.id.location_street:
                        Utilites.messageDialog(DeliveryAddressActivity.this, R.string.fill_street_name_message, R.string.street, R.string.ok).show();
                        break;
                }
                edit[i].requestFocus();
                return false;
            }
        }
        return true;

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        public void onClick(View v) {
            // it was the 2nd button
            switch (v.getId()) {
                case R.id.deliver_save:

                    if (locations.getSelectedItemPosition() == 0) {
                        Utilites.messageDialog(DeliveryAddressActivity.this, R.string.select_zone_message, R.string.zone, R.string.ok).show();

                    } else if (isAllFilled(new EditText[]{streetName, aptNumber})) {

                        SharedData.setAptNumber(aptNumber.getText().toString());
                        SharedData.setStreetName(streetName.getText().toString());
                        SharedData.setZoneId(locations.getSelectedItemPosition());

                        SharedData.setSaveDetails(save_details.isChecked());

                        Intent i = new Intent(DeliveryAddressActivity.this, HomeScreenActivity.class);
                        i.putExtra(ACTION_DELIVERY, true);
                        startActivity(i);
                    }
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
