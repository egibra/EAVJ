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
import main.eavj.ObjectClasses.City;

public class VisitingPlaceCityListActivity extends Activity {

    List<City> cities;
    DatabaseReference databaseCities;
    ListView listViewCities;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_visiting_place_city_list);

        Intent intent = getIntent();

        cities = new ArrayList<>();
        databaseCities = FirebaseDatabase.getInstance().getReference("cities").child(intent.getStringExtra("CountryID"));
        listViewCities = (ListView) findViewById(R.id.listViewCities);

        listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                City city = cities.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), VisitingPlaceListActivity.class);

                //putting artist name and id to intent
                intent.putExtra("CityID", city.getCityID());
                intent.putExtra("CityName", city.getCityName());

                //starting the activity with intent
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseCities.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cities.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    City city = postSnapshot.getValue(City.class);
                    cities.add(city);
                }
                CityAdapter citiesListAdapter = new CityAdapter(VisitingPlaceCityListActivity.this, cities);
                listViewCities.setAdapter(citiesListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}