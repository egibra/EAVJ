package main.eavj.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.ObjectClasses.City;
import main.eavj.ObjectClasses.Country;
import main.eavj.R;

import static android.content.ContentValues.TAG;

public class CountryAdapter extends ArrayAdapter<Country> {
    private Activity context;
    List<Country> countries;
    DatabaseReference db;
    List<String> cities = new ArrayList<>();

    public CountryAdapter(Activity context, List<Country> countries) {
        super(context, R.layout.layout_country_list, countries);
        this.context = context;
        this.countries = countries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_country_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        final TextView textViewCities = (TextView) listViewItem.findViewById(R.id.textViewCities);
        Country country = countries.get(position);
        db = FirebaseDatabase.getInstance().getReference("cities").child(country.getCountryID());
        cities = new ArrayList<>();
        String citiesString = "";
        readData(new FirebaseCallback() {
            @Override
            public void OnCallback(List<String> list) {
                String citiesString = "";
                for (String city: cities
                        ) {
                    citiesString += city + ",";

                }
                textViewCities.setText(citiesString);
            }
        });
        textViewName.setText(country.getCountryName());
        for (String city: cities
             ) {
            citiesString += city + " ";

        }
        textViewCities.setText(citiesString);

        return listViewItem;
    }

    private void readData(final FirebaseCallback firebaseCallback)
    {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String cityName = ds.child("cityName").getValue(String.class);
                    cities.add(cityName);
                }
                firebaseCallback.OnCallback(cities);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }

    private interface FirebaseCallback {
         void OnCallback(List<String> list);

    }
}
