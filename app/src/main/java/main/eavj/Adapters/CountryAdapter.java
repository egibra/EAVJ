package main.eavj.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import main.eavj.ObjectClasses.Country;
import main.eavj.R;

import static android.content.ContentValues.TAG;

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
        View listViewItem = inflater.inflate(R.layout.layout_country_list, parent, false);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        Country country = countries.get(position);

        textViewName.setText(country.getCountryName());
        return listViewItem;
    }

}
