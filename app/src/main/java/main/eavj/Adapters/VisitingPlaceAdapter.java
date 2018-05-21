package main.eavj.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import main.eavj.ObjectClasses.City;
import main.eavj.ObjectClasses.VisitingPlace;
import main.eavj.R;

public class VisitingPlaceAdapter extends ArrayAdapter<VisitingPlace> {
    private Activity context;
    List<VisitingPlace> visitingPlaces;

    public VisitingPlaceAdapter(Activity context, List<VisitingPlace> visitingPlaces) {
        super(context, R.layout.layout_country_list, visitingPlaces);
        this.context = context;
        this.visitingPlaces = visitingPlaces;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_visiting_place_list, parent, false);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewAddress = (TextView) listViewItem.findViewById(R.id.textViewAddress);
        ImageView imageViewCategory = (ImageView) listViewItem.findViewById(R.id.imageCategory);

        VisitingPlace visitingPlace = visitingPlaces.get(position);

        textViewName.setText(visitingPlace.getName());
        textViewAddress.setText(visitingPlace.getAddress());

        Drawable sleep = this.context.getResources().getDrawable(R.drawable.sleep);
        int drawableVisitingId = this.context.getResources().getIdentifier("place", "drawable", context.getPackageName());
        int drawableEatingId = this.context.getResources().getIdentifier("eating", "drawable", context.getPackageName());
        int drawableSleepId = this.context.getResources().getIdentifier("sleep", "drawable", context.getPackageName());
        int otherId = this.context.getResources().getIdentifier("add", "drawable", context.getPackageName());
        imageViewCategory.setImageResource(otherId);

        if(visitingPlace.getCategory().equals("VISITING"))
            imageViewCategory.setImageResource(drawableVisitingId);

        if(visitingPlace.getCategory().equals("EATING"))
            imageViewCategory.setImageResource(drawableEatingId);

        if(visitingPlace.getCategory().equals("SLEEPING"))
            imageViewCategory.setImageResource(drawableSleepId);

        return listViewItem;
    }

}
