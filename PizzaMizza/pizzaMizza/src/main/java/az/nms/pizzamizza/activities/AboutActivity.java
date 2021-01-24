package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.tools.Utilites;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends Activity implements OnMapReadyCallback {
	
	ViewPager photo_pager;

    private ImageButton home;
	private ImageView callButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		
		Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);
		Utilites.initAppBarFont(this);
		
//		PhotoChangeAdapter adapter = new PhotoChangeAdapter(getSupportFragmentManager());
		
//		photo_pager = (ViewPager) findViewById(R.id.pager_photo);
//		photo_pager.setAdapter(adapter);

		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		callButton = (ImageView) findViewById(R.id.call_button);

		callButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:*1415"));
				startActivity(callIntent);

			}
		});

        home = (ImageButton) findViewById(R.id.home_button);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBackPressed();

            }
        });


	}

	@Override
	public void onMapReady(GoogleMap map) {
		LatLng pizzamizza = new LatLng(40.383559, 49.823351);

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(pizzamizza, 13));


		map.addMarker(new MarkerOptions()
						.title("Pizza Mizza")
						.snippet("The best pizza ever")
						.position(pizzamizza)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.pizzamizzamarker))
						.snippet("The best pizza experience")
						.visible(true)
		);

	}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
//	private class PhotoChangeAdapter extends FragmentStatePagerAdapter {
//		public PhotoChangeAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int i) {
//			PhotoViewFragment fragment = new PhotoViewFragment();
//			Bundle args = new Bundle();
//			args.putInt(PhotoViewFragment.SECTION_TYPE, i);
//			// Our object is just an integer :-P
//			fragment.setArguments(args);
//			return fragment;
//		}
//
//		@Override
//		public int getCount() {
//			return 4;
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return "OBJECT " + (position + 1);
//		}
//	}



}
