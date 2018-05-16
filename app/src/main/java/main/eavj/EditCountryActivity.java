package main.eavj;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.eavj.Adapters.CountryAdapter;
import main.eavj.ObjectClasses.Country;

public class EditCountryActivity extends AppCompatActivity {

    List<Country> countries;
    DatabaseReference databaseCountries;
    EditText editTextName;
    EditText editTextX;
    EditText editTextY;
    ListView listViewCountries;
    Button addCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_country);
        countries = new ArrayList<>();

        databaseCountries = FirebaseDatabase.getInstance().getReference("countries");
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);
        addCountry = (Button) findViewById(R.id.buttonAddCountry);
        listViewCountries = (ListView) findViewById(R.id.listViewCountries);
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
                Intent intent = new Intent(getApplicationContext(), EditCityActivity.class);

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
        databaseCountries.addListenerForSingleValueEvent(new ValueEventListener() {
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
                CountryAdapter countriesAdapter = new CountryAdapter(EditCountryActivity.this, countries);
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

                //deleteArtist(artistId);
                dialog.dismiss();
            }
        });
    }
    private boolean updateCountry(String id, String name) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("country").child(id);

        //updating artist
        Country artist = new Country(id, name);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "Country Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    public void addCountry()
    {
        String name = editTextName.getText().toString().trim();
        if(!TextUtils.isEmpty(name))
        {
            String id = databaseCountries.push().getKey();
            Country country = new Country(id, name);
            databaseCountries.child(id).setValue(country);
            editTextName.setText("");
            Toast.makeText(this, "Country added", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
            {
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
