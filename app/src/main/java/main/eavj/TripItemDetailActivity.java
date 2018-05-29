package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.Adapters.TripAdapter;
import main.eavj.Adapters.TripItemAdapter;
import main.eavj.Adapters.VisitingPlaceAdapter;
import main.eavj.ObjectClasses.City;
import main.eavj.ObjectClasses.Trip;
import main.eavj.ObjectClasses.TripItem;
import main.eavj.ObjectClasses.VisitingPlace;
import main.eavj.ObjectClasses.VisitingPlaceItem;

/**
 * Created by vika on 25-Apr-18.
 */

public class TripItemDetailActivity extends AppCompatActivity {
    DatabaseReference db;
    DatabaseReference databaseItemsRef;
    DatabaseReference databaseVisitingPlaces;
    DatabaseReference databaseItemsRefer;
    ListView listViewPlaces;
    TripItem tripItem;
    List<TripItem> tripItems;
    List<VisitingPlace> places;
    List<String> placeIds;
    List<VisitingPlaceItem> tripItemPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_trip_item_detail);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseItemsRef = FirebaseDatabase.getInstance().getReference("trip item").child(intent.getStringExtra("TripID")).child(intent.getStringExtra("TripItemID"));
        databaseItemsRefer = FirebaseDatabase.getInstance().getReference("trip item").child(intent.getStringExtra("TripID")).child(intent.getStringExtra("TripItemID")).child("visiting places");
        databaseVisitingPlaces =  FirebaseDatabase.getInstance().getReference("visiting places");

        placeIds = new ArrayList<>();
        tripItemPlaces = new ArrayList<>();
        places = new ArrayList<>();
        tripItem = new TripItem();

        final TextView title = (TextView)findViewById(R.id.tripTitle);
        final TextView dateFrom = (TextView)findViewById(R.id.dateFrom);
        final TextView dateTo = (TextView)findViewById(R.id.dateTo);

        listViewPlaces = (ListView) findViewById(R.id.selectedPlaces);
        Button mapBtn = (Button) findViewById(R.id.viewMap);
//        title.setText(intent.getStringExtra("TripID"));
//        tripItemsListView = (ListView) findViewById(R.id.tripItemsList);
        tripItems = new ArrayList<>();

        databaseItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TripItem trip = dataSnapshot.getValue(TripItem.class);
                title.setText(trip.getTitle());
                dateFrom.setText(trip.getDateFrom());
                dateTo.setText(trip.getDateTo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        databaseVisitingPlaces.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                places.clear();
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
//
//                        VisitingPlace place = postSnapshot2.getValue(VisitingPlace.class);
////                        if (placeIds.contains(place.getVisitingPlaceID())) {
//                        places.add(place);
////                        }
//
//                    }
//                }
//
////                VisitingPlaceAdapter visitingPlaceListAdapter = new VisitingPlaceAdapter(TripItemDetailActivity.this, places);
////                listViewPlaces.setAdapter(visitingPlaceListAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();

                Intent intentt = new Intent(getApplicationContext(), TripMapActivity.class);
                intentt.putExtra("TripItemID", intent.getStringExtra("TripItemID"));
                intentt.putExtra("TripID", intent.getStringExtra("TripID"));
                getApplicationContext().startActivity(intentt);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        getPlacesIds();




        databaseVisitingPlaces.addValueEventListener(new ValueEventListener() {
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
                VisitingPlaceAdapter visitingPlaceListAdapter = new VisitingPlaceAdapter(TripItemDetailActivity.this, places);
                listViewPlaces.setAdapter(visitingPlaceListAdapter);

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

}
