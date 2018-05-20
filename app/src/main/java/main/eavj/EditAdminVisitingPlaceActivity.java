package main.eavj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import main.eavj.Adapters.CityAdapter;
import main.eavj.ObjectClasses.City;

public class EditAdminVisitingPlaceActivity extends AppCompatActivity {

    DatabaseReference db;
    List<City> cities;
    ListView listViewCities;
    TextView textViewCitiesCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin_visiting_place);
        cities = new ArrayList<>();
        textViewCitiesCount = (TextView) findViewById(R.id.textView2);
        listViewCities = (ListView) findViewById(R.id.listViewCities);
        db = FirebaseDatabase.getInstance().getReference("cities");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cities.clear();
                String countriesCount;
                String citiesCount;
                countriesCount = Long.toString(dataSnapshot.getChildrenCount());
                for (DataSnapshot snap: dataSnapshot.getChildren()
                     ) {
                      for(DataSnapshot childinSnap: snap.getChildren())
                      {
                          City city = childinSnap.getValue(City.class);
                          cities.add(city);
                      }
                }
                if (cities.size() > 0)
                {
                    ObjectComparator comparator = new ObjectComparator();
                    Collections.sort(cities, comparator);

                }
                citiesCount = Integer.toString(cities.size());

                textViewCitiesCount.setText("Displaying " + citiesCount + " cities from " + countriesCount +" countries." );
                CityAdapter citiesListAdapter = new CityAdapter(EditAdminVisitingPlaceActivity.this, cities);
                listViewCities.setAdapter(citiesListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City city = cities.get(i);
                Intent intent = new Intent(getApplicationContext(), CrudAdminVisitingPlaceActivity.class);

                //putting artist name and id to intent
                intent.putExtra("CityID", city.getCityID());
                intent.putExtra("CityName", city.getCityName());

                //starting the activity with intent
                startActivity(intent);
            }
        });

    }
    public class ObjectComparator implements Comparator<City> {

        public int compare(City obj1, City obj2) {
            return obj1.getCityName().compareTo(obj2.getCityName());
        }

    }
}
