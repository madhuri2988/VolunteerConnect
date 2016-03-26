package edu.scu.volunteerconnect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    Map<Marker,Event> mapMarker;
    final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/events";
    Firebase firebaseRoot;

    double longitude;
    double latitude;
    String category;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
Bundle b=getIntent().getExtras();
        if(b!=null){
           category= b.getString("category");
        }
        Firebase.setAndroidContext(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));

        // Enable MyLocation Layer of Google Map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestMapPermission();
            return;
        }
        mMap.setMyLocationEnabled(true);

            // Get LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);



            latitude=37.349952; // returns latitude
          longitude=  -121.940608;
            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        getfirebaseData();
        mMap.setOnInfoWindowClickListener(this);
        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("").icon(bitmapDescriptor)).showInfoWindow();


        }


    private  void requestMapPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {            // you can show dialog here for grant permission (camera) and handle dialog event according to your need
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }
        else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

public void getfirebaseData(){
    firebaseRoot = new Firebase(FIREBASE_URL);


    mapMarker=new HashMap<Marker,Event>();
    firebaseRoot.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                //Getting the data from snapshot
                Event event = postSnapshot.getValue(Event.class);

                      //Adding it to a string
                    String string = event.getAddress() + "," + event.getCountry() + "," + event.getZipcode();


                    getLatLong(string, event);






            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    });

}
    @Override
    public void onInfoWindowClick(Marker marker) {
        for (Map.Entry<Marker, Event> entry : mapMarker.entrySet()) {

if(marker.equals(entry.getKey())){
    Intent i=new Intent(getApplicationContext(),EventDetailsActivity.class);
    i.putExtra("event", entry.getValue());
    startActivity(i);

}

        }
    }
    public void getLatLong(final String locationAddress,final Event event){
        final GeocoderHandler handler=new GeocoderHandler();


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String result = null;

                try {
                    List addressList = geocoder.getFromLocationName(locationAddress, 1);

                    if (addressList != null && addressList.size() > 0) {

                        Address address = (Address)addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append(address.getLatitude()).append("\n");
                        sb.append(address.getLongitude()).append("\n");
                        result = sb.toString();

                        Location locationA = new Location("point A");

                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("point B");

                        locationB.setLatitude(address.getLatitude());
                        locationB.setLongitude(address.getLongitude());
                        float distanceInMeters=locationA.distanceTo(locationB);
                        float distanceMiles=distanceInMeters*0.00062137f;

                        DecimalFormat df = new DecimalFormat("###.#");



                        BitmapDescriptor bitmapGreen
                                = BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN);
                       Marker m1=  mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(event.getEventTitle()+"    "+ df.format(distanceMiles)+ " mi").snippet(locationAddress).icon(bitmapGreen));
                        m1.showInfoWindow();
                        mapMarker.put(m1, event);

                    }
                } catch (IOException e) {

                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n\nLatitude and Longitude :\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }




    private class GeocoderHandler extends Handler {
    @Override
    public void handleMessage(Message message) {
        String locationAddress;
        switch (message.what) {
            case 1:
                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;
            default:
                locationAddress = null;
        }

    }
}
}
