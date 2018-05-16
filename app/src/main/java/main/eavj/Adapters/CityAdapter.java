package main.eavj.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import main.eavj.ObjectClasses.City;
import main.eavj.ObjectClasses.Country;
import main.eavj.R;

public class CityAdapter extends ArrayAdapter<City> {
    private Activity context;
    List<City> cities;

    public CityAdapter(Activity context, List<City> cities) {
        super(context, R.layout.layout_country_list, cities);
        this.context = context;
        this.cities = cities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_country_list, parent, false);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        City city = cities.get(position);

        textViewName.setText(city.getCityName());
        return listViewItem;
    }

}
