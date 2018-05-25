package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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

import main.eavj.Adapters.CityAdapter;
import main.eavj.Adapters.VisitingPlaceAdapter;
import main.eavj.ObjectClasses.VisitingPlace;

public class VisitingPlaceListActivity extends Activity {

    List<VisitingPlace> visitingPlaces;
    DatabaseReference databaseVisitingPlaces;
    ListView listViewVisitingPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_visiting_place_list);

        Intent intent = getIntent();

        visitingPlaces = new ArrayList<>();
        databaseVisitingPlaces = FirebaseDatabase.getInstance().getReference("visiting places").child(intent.getStringExtra("CityID"));
        listViewVisitingPlaces = (ListView) findViewById(R.id.listViewVisitingPlaces);

        listViewVisitingPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                VisitingPlace visitingPlace = visitingPlaces.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), VisitingPlaceActivity.class);

                //putting artist name and id to intent
                intent.putExtra("VisitingPlaceID", visitingPlace.getVisitingPlaceID());
                intent.putExtra("VisitingPlaceName", visitingPlace.getName());

                //starting the activity with intent
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseVisitingPlaces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                visitingPlaces.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    VisitingPlace visitingPlace = postSnapshot.getValue(VisitingPlace.class);
                    visitingPlaces.add(visitingPlace);
                }
                VisitingPlaceAdapter visitingPlaceListAdapter = new VisitingPlaceAdapter(VisitingPlaceListActivity.this, visitingPlaces);
                listViewVisitingPlaces.setAdapter(visitingPlaceListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}