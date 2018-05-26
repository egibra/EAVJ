package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import main.eavj.Adapters.CountryAdapter;
import main.eavj.Adapters.TripAdapter;
import main.eavj.ObjectClasses.Country;
import main.eavj.ObjectClasses.Trip;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TripListActivity extends AppCompatActivity {
    ListView tripListView;
    DatabaseReference db;
    List<Trip> trips;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_trip_list);
        db = FirebaseDatabase.getInstance().getReference("trip").child(intent.getStringExtra("userID"));
        tripListView = (ListView) findViewById(R.id.tripListView);
        trips = new ArrayList<>();
//        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//            Trip trip = postSnapshot.getValue(Trip.class);
//            trips.add(trip);
//        }
//        TripAdapter tripAdapter = new TripAdapter(TripListActivity.this, trips);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                trips.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Trip trip = postSnapshot.getValue(Trip.class);
                    //adding artist to the list
                    trips.add(trip);
                }
                TripAdapter tripAdapter = new TripAdapter(TripListActivity.this, trips);
                tripListView.setAdapter(tripAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void openMapWindow(View view)
    {
        Intent intent = new Intent(this, TripMapActivity.class);
        startActivity(intent);
    }

    public void openEditTripPlanWindow(View view)
    {
        Intent intent = new Intent(this, EditTripPlanActivity.class);
        startActivity(intent);
    }


}
