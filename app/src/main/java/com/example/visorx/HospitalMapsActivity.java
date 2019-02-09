package com.example.visorx;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HospitalMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView name;
    TextView address;
    TextView phoneNumber;
    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            pos = extras.getInt("POS");
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkGPSPermission();

    }

    public void checkGPSPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            setHospitalDetails();

        }

    }

    public void setHospitalDetails() {

        Hospital hospital = new Hospital();


        if (pos != -1) {

            LatLng hosMarker = new LatLng(hospital.getLatitude(pos), hospital.getLongitude(pos));

            mMap.addMarker(new MarkerOptions().position(hosMarker).title("Move pin to adjust").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(hosMarker));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

            name.setText(hospital.getName(pos));
            address.setText(hospital.getAddress(pos));

            if (hospital.getPhoneNum(pos) != null) {
                phoneNumber.setText("Phone Number: " + hospital.getPhoneNum(pos));
            } else {
                phoneNumber.setText("Phone Number not available.");
            }

        } else {
            Toast.makeText(getApplicationContext(), "Location not available at the moment", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    setHospitalDetails();

                }


            }
        }
    }
}
