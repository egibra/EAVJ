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
import main.eavj.ObjectClasses.VisitingPlace;
import main.eavj.R;

public class SearchFriendTripsAdapter extends ArrayAdapter<Trip> {
    private Activity context;
    List<Trip> trips;

    public SearchFriendTripsAdapter(Activity context, List<Trip> trips) {
        super(context, R.layout.layout_friend_search_trip_list_item, trips);
        this.context = context;
        this.trips = trips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_friend_search_trip_list_item, parent, false);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDateFromDateTo = (TextView) listViewItem.findViewById(R.id.textViewDateFromDateTo);
        ImageView imageViewCategory = (ImageView) listViewItem.findViewById(R.id.imageCategory);

        Trip trip = trips.get(position);

        textViewName.setText(trip.getTitle());
        textViewDateFromDateTo.setText("From: " + trip.getDateFrom().toString() + " To: " + trip.getDateTo().toString());

        int drawableFlightId = this.context.getResources().getIdentifier("ic_flight", "drawable", context.getPackageName());
        int drawableEatingId = this.context.getResources().getIdentifier("ic_eating", "drawable", context.getPackageName());

        if(trip.getTripStatus()== 0)
            imageViewCategory.setImageResource(drawableFlightId);
        else
            imageViewCategory.setImageResource(drawableEatingId);

        return listViewItem;
    }

}
