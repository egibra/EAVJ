package main.eavj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.Adapters.SearchFriendTripsAdapter;
import main.eavj.Adapters.TripAdapter;
import main.eavj.ObjectClasses.Trip;

public class FriendSearchActivity extends AppCompatActivity {
    ListView tripListView;
    DatabaseReference db;
    List<Trip> trips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        db = FirebaseDatabase.getInstance().getReference("trip");
        tripListView = (ListView) findViewById(R.id.tripListView);
        trips = new ArrayList<>();
        tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trip selectedTrip = trips.get(i);
                Intent intent = new Intent( getApplicationContext(), FriendSearchChatActivity.class);
                intent.putExtra("TripID", selectedTrip.getId());
                intent.putExtra("TripTitle", selectedTrip.getTitle());
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                trips.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                     for(DataSnapshot childSnap : postSnapshot.getChildren())
                     {
                         Trip trip = childSnap.getValue(Trip.class);
                         if (trip != null && trip.getSearchForFriend())
                           trips.add(trip);
                     }
                }
                SearchFriendTripsAdapter tripAdapter = new SearchFriendTripsAdapter(FriendSearchActivity.this, trips);
                tripListView.setAdapter(tripAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
