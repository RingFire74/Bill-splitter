package myapplication.vedha.example.com.bill_splitter;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Amrita, Coimbatore.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        final String[] langCode = new String[1];
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Address obj = addresses.get(0);

                    add = obj.getAddressLine(0);
//                    add = add + "\n" + obj.getCountryName();
//                    add = add + "\n" + obj.getCountryCode();
//                    add = add + "\n" + obj.getAdminArea();
//                    add = add + "\n" + obj.getPostalCode();
//                    add = add + "\n" + obj.getSubAdminArea();
//                    add = add + "\n" + obj.getLocality();
//                    add = add + "\n" + obj.getSubThoroughfare();
                    Address address = addresses.get(0);
                    String countryCode = address.getCountryCode();
                    Locale[] locales = Locale.getAvailableLocales();

                    for (Locale localeIn : locales) {
                        if (countryCode.equalsIgnoreCase(localeIn.getCountry())) {
                            //langCode = localeIn.getLanguage(); //This is language code, ex : en
                            langCode[0] = localeIn.toString();// This is language name, ex : English
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent();
                i.putExtra("address",add);
                i.putExtra("locale", langCode[0]);
                setResult(2, i);
                finish();
            }
        });
        // Add a marker in Amrita Coimbatore and move the camera
//        LatLng amrita = new LatLng(10.9027, 76.9006);
//        mMap.addMarker(new MarkerOptions().position(amrita).title("Marker at Amrita Coimbatore"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(amrita));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent();
        i.putExtra("address","null");
        setResult(3, i);
        finish();
    }
}
