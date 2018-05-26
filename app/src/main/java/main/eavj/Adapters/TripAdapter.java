package main.eavj.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import main.eavj.ObjectClasses.Trip;
import main.eavj.R;

public class TripAdapter extends ArrayAdapter<Trip> {
    private Activity context;
    List<Trip> trips;

    public TripAdapter(Activity context, List<Trip> trips) {
        super(context, R.layout.layout_trip_list, trips);
        this.context = context;
        this.trips = trips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_trip_list, parent, false);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tripTitle);

        Trip trip = trips.get(position);
//        String name = trip.getTitle();
        textViewName.setText(trip.getTitle());

        return listViewItem;
    }

}
