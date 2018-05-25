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

import main.eavj.Adapters.CountryAdapter;
import main.eavj.ObjectClasses.Country;

public class VisitingPlaceCountryListActivity extends Activity {

    List<Country> countries;
    DatabaseReference databaseCountries;
    ListView listViewCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_visiting_place_country_list);

        countries = new ArrayList<>();
        databaseCountries = FirebaseDatabase.getInstance().getReference("countries");
        listViewCountries = (ListView) findViewById(R.id.listViewCountries);

        listViewCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Country country = countries.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), VisitingPlaceCityListActivity.class);

                //putting artist name and id to intent
                intent.putExtra("CountryID", country.getCountryID());
                intent.putExtra("CountryName", country.getCountryName());

                //starting the activity with intent
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseCountries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                countries.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Country country = postSnapshot.getValue(Country.class);
                    //adding artist to the list
                    countries.add(country);
                }
                CountryAdapter countriesAdapter = new CountryAdapter(VisitingPlaceCountryListActivity.this, countries);
                listViewCountries.setAdapter(countriesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}