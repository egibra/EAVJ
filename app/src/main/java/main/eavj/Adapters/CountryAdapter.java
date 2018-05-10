package main.eavj.Adapters;

import android.app.Activity;
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

public class CountryAdapter extends ArrayAdapter<Country> {
    private Activity context;
    List<Country> countries;

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
        TextView textViewCities = (TextView) listViewItem.findViewById(R.id.textViewCities);
        Country country = countries.get(position);
        DatabaseReference db;
        db = FirebaseDatabase.getInstance().getReference("cities").child(country.getCountryID());
        final List<City> cities = new ArrayList<>();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    City city = postSnapshot.getValue(City.class);
                    cities.add(city);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        textViewName.setText(country.getCountryName());
        String citiesString = "";
        for (City city: cities
             ) {
            citiesString += city.getCityName() + ",";

        }
        textViewCities.setText(citiesString);

        return listViewItem;
    }
}
