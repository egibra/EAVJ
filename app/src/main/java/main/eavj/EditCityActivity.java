package main.eavj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.Adapters.CityAdapter;
import main.eavj.ObjectClasses.City;

public class EditCityActivity extends AppCompatActivity {
    Button buttonAddCity;
    EditText editTextName;
    SeekBar seekBarRating;
    TextView textViewCountry;
    ListView listViewCities;
    EditText editTextX;
    EditText editTextY;
    DatabaseReference databaseCities;

    List<City> cities;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_city);

        Intent intent = getIntent();

        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node track we are creating a new child with the artist id
         * and inside that node we will store all the tracks with unique ids
         * */
        databaseCities = FirebaseDatabase.getInstance().getReference("cities").child(intent.getStringExtra("CountryID"));

        buttonAddCity = (Button) findViewById(R.id.buttonAddCity);
        editTextName = (EditText) findViewById(R.id.editTextName);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        listViewCities = (ListView) findViewById(R.id.listViewCities);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);

        cities = new ArrayList<>();

        textViewCountry.setText(intent.getStringExtra("CountryName"));

        buttonAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCity();
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
                CityAdapter citiesListAdapter = new CityAdapter(EditCityActivity.this, cities);
                listViewCities.setAdapter(citiesListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveCity() {
        String cityName = editTextName.getText().toString().trim();
        int rating = seekBarRating.getProgress();
        Float x;
        Float y;
        try {
            x = Float.parseFloat(editTextX.getText().toString());
            y = Float.parseFloat(editTextY.getText().toString());
        }
        catch (NumberFormatException e){
            Toast.makeText(EditCityActivity.this,"Input was not in correct format", Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.isEmpty(cityName)) {
            String id  = databaseCities.push().getKey();
            City city = new City(id, cityName, x.toString(), y.toString());
            databaseCities.child(id).setValue(city);
            Toast.makeText(this, "City saved", Toast.LENGTH_LONG).show();
            editTextName.setText("");
        } else {
            Toast.makeText(this, "Please enter city name", Toast.LENGTH_LONG).show();
        }
    }
}
