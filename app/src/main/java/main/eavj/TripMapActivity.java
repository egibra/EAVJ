package main.eavj;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.Adapters.VisitingPlaceAdapter;
import main.eavj.ObjectClasses.Trip;
import main.eavj.ObjectClasses.TripItem;
import main.eavj.ObjectClasses.VisitingPlace;
import main.eavj.ObjectClasses.VisitingPlaceItem;

public class TripMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference db;
    DatabaseReference databaseItemsRef;
    DatabaseReference databaseItemsRefer;
    ListView tripItemsListView;
    List<VisitingPlaceItem> tripItemPlaces;
    List<VisitingPlace> places;
    List<String> placeIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent = getIntent();
        databaseItemsRef = FirebaseDatabase.getInstance().getReference("visiting place item").child(intent.getStringExtra("TripItemID"));
        databaseItemsRefer = FirebaseDatabase.getInstance().getReference("trip item").child(intent.getStringExtra("TripID")).child(intent.getStringExtra("TripItemID")).child("visiting places");

        db = FirebaseDatabase.getInstance().getReference("visiting places");
        tripItemPlaces = new ArrayList<>();
        placeIds = new ArrayList<>();
        places =new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getPlacesIds();




        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                places.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {

                        VisitingPlace place = postSnapshot2.getValue(VisitingPlace.class);
//                        placesNames.add(place.getName());
                        if (placeIds.contains(place.getVisitingPlaceID())) {
                            places.add(place);
                        }
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPlacesIds(){
        databaseItemsRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                placeIds.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String item = postSnapshot.getValue(String.class);
                    placeIds.add(item);
//                    tripItemPlaces.add(item);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        getPlacesIds();




        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                places.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {

                        VisitingPlace place = postSnapshot2.getValue(VisitingPlace.class);
//                        placesNames.add(place.getName());
                        if (placeIds.contains(place.getVisitingPlaceID())) {
                            places.add(place);
                        }
                    }
                }
                LatLng plc = new LatLng(0.0,0.0);
                for (VisitingPlace place: places ) {
                    double coordX = Double.parseDouble(place.getX());
                    double coordY = Double.parseDouble(place.getY());
                    plc = new LatLng(coordX, coordY);
                    mMap.addMarker(new MarkerOptions().position(plc).title(place.getName()));
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLng(plc));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
