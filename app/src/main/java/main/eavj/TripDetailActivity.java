package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import main.eavj.ObjectClasses.City;
import main.eavj.ObjectClasses.Trip;
import main.eavj.ObjectClasses.TripItem;

/**
 * Created by vika on 25-Apr-18.
 */

public class TripDetailActivity extends Activity {
    DatabaseReference db;
    DatabaseReference databaseItemsRef;
    ListView tripItemsListView;
    Trip trip;
    List<TripItem> tripItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_trip_detail);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("trip").child(uid).child(intent.getStringExtra("TripID"));
        databaseItemsRef = FirebaseDatabase.getInstance().getReference("trip item").child(intent.getStringExtra("TripID"));
        trip = new Trip();

        final TextView title = (TextView)findViewById(R.id.tripTitle);
        final TextView dateFrom = (TextView)findViewById(R.id.dateFrom);
        final TextView dateTo = (TextView)findViewById(R.id.dateTo);
//        title.setText(intent.getStringExtra("TripID"));
        tripItemsListView = (ListView) findViewById(R.id.tripItemsList);
        tripItems = new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Trip trip = dataSnapshot.getValue(Trip.class);
                title.setText(trip.getTitle());
                dateFrom.setText(trip.getDateFrom());
                dateTo.setText(trip.getDateTo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tripItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TripItem item = tripItems.get(i);
                Intent intent = getIntent();

                Intent intentt = new Intent(getApplicationContext(), TripItemDetailActivity.class);

                intentt.putExtra("TripItemID", item.getId());
                intentt.putExtra("TripID", intent.getStringExtra("TripID"));

                getApplicationContext().startActivity(intentt);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tripItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    TripItem item = postSnapshot.getValue(TripItem.class);
                    tripItems.add(item);
                }
                TripItemAdapter tripAdapter = new TripItemAdapter(TripDetailActivity.this, tripItems);
                tripItemsListView.setAdapter(tripAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
