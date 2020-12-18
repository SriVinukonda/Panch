package com.example.panch;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.Inflater;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
        private static final String TAG = MapsActivity.class.getSimpleName();

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
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.string.style_json));
//
//            if (!success) {
//                Log.e(TAG, "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e(TAG, "Can't find style. Error: ", e);
//        }
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));


        // Add a marker in Sydney and move the camera
        LatLng center = new LatLng(0, 0);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Demographics")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getData();
//                                continue;
                                populateMap(document.getData());
                            }
                        }
                    }

                });

    }

    public void populateMap(Map<String, Object> data) {
        TreeMap<String,Object> sortedData = new TreeMap<>(data);
        Object[] keys = sortedData.keySet().toArray();

        System.out.println((String) keys[0]);
        try {
            for (int i = 0; i <keys.length; i++) {
                String country = (String) keys[i];
                ArrayList<String> information = (ArrayList<String>) data.get(country);
                String info = "";
                Geocoder geocoder = new Geocoder(this);
//                System.out.println(geocoder.getFromLocationName("Earth",178));
                List<Address> listOfAddresses = geocoder.getFromLocationName(country, 1);

                for(int j =0; j < information.size(); j++) {
                    info += information.get(j);
                    if(j+1 < information.size())
                        info += "-";
                }
                if (listOfAddresses.size() > 0) {
//                    CustomInfoWindow customInfoWindow = new CustomInfoWindow(this);
//                    mMap.setInfoWindowAdapter(customInfoWindow);


                    LatLng currentLoc = new LatLng(listOfAddresses.get(0).getLatitude(), listOfAddresses.get(0).getLongitude());

                    MarkerOptions options = new MarkerOptions().position(currentLoc).title(country).snippet(info);
                    mMap.addMarker(options);
                }

            }

        } catch(Exception e){
            e.printStackTrace();
        }

        Log.d("DONE POPULATING","inside MapsActivity");
    }

}