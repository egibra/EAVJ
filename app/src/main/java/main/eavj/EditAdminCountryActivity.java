package main.eavj;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.Adapters.CountryAdapter;
import main.eavj.Adapters.PlaceAutoCompleteAdapter;
import main.eavj.ObjectClasses.Country;

public class EditAdminCountryActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    List<Country> countries;
    DatabaseReference databaseCountries;
    AutoCompleteTextView autoCompleteName;
    EditText editTextX;
    EditText editTextY;
    ListView listViewCountries;
    Button addCountry;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_country);
        countries = new ArrayList<>();

        databaseCountries = FirebaseDatabase.getInstance().getReference("countries");
        autoCompleteName = (AutoCompleteTextView) findViewById(R.id.AutoCompleteName);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);
        addCountry = (Button) findViewById(R.id.buttonAddCountry);
        listViewCountries = (ListView) findViewById(R.id.listViewCountries);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        PlaceAutoCompleteAdapter placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient,LAT_LNG_BOUNDS, null);
        autoCompleteName.setAdapter(placeAutoCompleteAdapter);
        addCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCountry();
            }
        });
        listViewCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Country country = countries.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), EditAdminCityActivity.class);

                //putting artist name and id to intent
                intent.putExtra("CountryID", country.getCountryID());
                intent.putExtra("CountryName", country.getCountryName());

                //starting the activity with intent
                startActivity(intent);
            }
        });
        listViewCountries.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = countries.get(i);
                showUpdateDeleteDialog(country.getCountryID(), country.getCountryName());
                return true;
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
                CountryAdapter countriesAdapter = new CountryAdapter(EditAdminCountryActivity.this, countries);
                listViewCountries.setAdapter(countriesAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void showUpdateDeleteDialog(final String countryID, String countryName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_country_dialogue,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(countryName);

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
                    updateCountry(countryID, name);
                    dialog.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCountry(countryID);
                dialog.dismiss();
            }
        });
    }

    private void deleteCountry(String countryID) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("countries").child(countryID);
        dr.removeValue();
        DatabaseReference drCities = FirebaseDatabase.getInstance().getReference("cities").child(countryID);
        drCities.removeValue();
    }

    private boolean updateCountry(String id, String name) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("countries").child(id);

        //updating artist
        Country country = new Country(id, name);
        dR.setValue(country);
        Toast.makeText(getApplicationContext(), "Country Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    public void addCountry()
    {
        String name = autoCompleteName.getText().toString().trim();
        if(!TextUtils.isEmpty(name))
        {
            String id = databaseCountries.push().getKey();
            Country country = new Country(id, name);
            databaseCountries.child(id).setValue(country);
            autoCompleteName.setText("");
            Toast.makeText(this, "Country added", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
