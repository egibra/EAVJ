package main.eavj;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.Adapters.CityAdapter;
import main.eavj.Adapters.PlaceAutoCompleteAdapter;
import main.eavj.ObjectClasses.City;

public class EditAdminCityActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    Button buttonAddCity;
    AutoCompleteTextView autoCompleteName;
    TextView textViewCountry;
    ListView listViewCities;
    EditText editTextX;
    EditText editTextY;
    DatabaseReference databaseCities;
    String countryID;
    List<City> cities;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

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
        countryID = intent.getStringExtra("CountryID");
        buttonAddCity = (Button) findViewById(R.id.buttonAddCity);
        autoCompleteName = (AutoCompleteTextView) findViewById(R.id.AutoCompleteName);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        listViewCities = (ListView) findViewById(R.id.listViewCities);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);

        cities = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        PlaceAutoCompleteAdapter placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient,LAT_LNG_BOUNDS, null);
        autoCompleteName.setAdapter(placeAutoCompleteAdapter);



        textViewCountry.setText(intent.getStringExtra("CountryName"));

        listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City city = cities.get(i);
                ShowOnMap(city.getCityName(),city.getX(), city.getY());
            }
        });

        listViewCities.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                City city = cities.get(i);
                showUpdateDeleteDialog(city.getCityID(), city.getCityName());
                return true;
            }
        });

        buttonAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCity();
            }
        });
    }

    private void ShowOnMap(String cityName, String x, String y) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogBuilder.setTitle(cityName);
        final View dialogView = inflater.inflate(R.layout.activity_trip_map, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LatLng sydney = new LatLng(-34, 151);

       // mapFragment

    }

    private void showUpdateDeleteDialog(final String cityID, String cityName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_country_dialogue,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(cityName);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateCountry);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteCountry);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateCity(cityID, name);
                    dialog.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCity(cityID);
                dialog.dismiss();
            }
        });

    }
    private void deleteCity(String cityID) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("cities").child(countryID).child(cityID);
        dr.removeValue();
        Toast.makeText(getApplicationContext(), "City Removed", Toast.LENGTH_LONG).show();

    }

    private boolean updateCity(String id, String name) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("cities").child(countryID).child(id).child("cityName");
        dR.setValue(name);
        Toast.makeText(getApplicationContext(), "City Updated", Toast.LENGTH_LONG).show();
        return true;
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
                CityAdapter citiesListAdapter = new CityAdapter(EditAdminCityActivity.this, cities);
                listViewCities.setAdapter(citiesListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveCity() {
        String cityName = autoCompleteName.getText().toString().trim();
        Float x;
        Float y;
        try {
            x = Float.parseFloat(editTextX.getText().toString());
            y = Float.parseFloat(editTextY.getText().toString());
        }
        catch (NumberFormatException e){
            Toast.makeText(EditAdminCityActivity.this,"Input was not in correct format", Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.isEmpty(cityName)) {
            String id  = databaseCities.push().getKey();
            City city = new City(id, cityName, x.toString(), y.toString());
            databaseCities.child(id).setValue(city);
            Toast.makeText(this, "City saved", Toast.LENGTH_LONG).show();
            autoCompleteName.setText("");
        } else {
            Toast.makeText(this, "Please enter city name", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
