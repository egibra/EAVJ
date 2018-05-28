package main.eavj.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import main.eavj.ObjectClasses.Country;
import main.eavj.ObjectClasses.TripItem;
import main.eavj.R;

public class TripItemAdapter extends ArrayAdapter<TripItem> {
    private Activity context;
    List<TripItem> items;

    public TripItemAdapter(Activity context, List<TripItem> items) {
        super(context, R.layout.layout_trip_item_list, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_trip_item_list, parent, false);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tripItemName);
        TextView textDateFrom = (TextView) listViewItem.findViewById(R.id.tripDateFrom);
        TextView textDateTo = (TextView) listViewItem.findViewById(R.id.tripDateTo);
        TripItem item = items.get(position);

        textViewName.setText(item.getTitle());
        textDateFrom.setText(item.getDateFrom());
        textDateTo.setText(item.getDateTo());
        return listViewItem;
    }
}
