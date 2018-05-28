package main.eavj.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.List;
import java.util.ArrayList;

import main.eavj.ObjectClasses.VisitingPlace;
import main.eavj.R;

public class VisitingPlaceCheckListAdapter extends ArrayAdapter<VisitingPlace> {
    private Activity context;
    List<VisitingPlace> visitingPlaces;
   public static ArrayList<String> selectedStrings = new ArrayList<String>();
    public VisitingPlaceCheckListAdapter(Activity context, List<VisitingPlace> visitingPlaces) {
        super(context, R.layout.layout_visiting_place_checklist, visitingPlaces);
        this.context = context;
        this.visitingPlaces = visitingPlaces;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_visiting_place_checklist, parent, false);

        CheckBox checkBox = (CheckBox) listViewItem.findViewById(R.id.tripPlaceCheck);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewAddress = (TextView) listViewItem.findViewById(R.id.textViewAddress);
        ImageView imageViewCategory = (ImageView) listViewItem.findViewById(R.id.imageCategory);

        final VisitingPlace visitingPlace = visitingPlaces.get(position);

        textViewName.setText(visitingPlace.getName());
        textViewAddress.setText(visitingPlace.getAddress());

        Drawable sleep = this.context.getResources().getDrawable(R.drawable.sleep);
        int drawableVisitingId = this.context.getResources().getIdentifier("ic_visiting", "drawable", context.getPackageName());
        int drawableEatingId = this.context.getResources().getIdentifier("ic_eating", "drawable", context.getPackageName());
        int drawableSleepId = this.context.getResources().getIdentifier("ic_sleep", "drawable", context.getPackageName());
        int otherId = this.context.getResources().getIdentifier("add", "drawable", context.getPackageName());
        imageViewCategory.setImageResource(otherId);

        if(visitingPlace.getCategory().equals("VISITING"))
            imageViewCategory.setImageResource(drawableVisitingId);

        if(visitingPlace.getCategory().equals("EATING"))
            imageViewCategory.setImageResource(drawableEatingId);

        if(visitingPlace.getCategory().equals("SLEEPING"))
            imageViewCategory.setImageResource(drawableSleepId);

        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedStrings.add(visitingPlace.getVisitingPlaceID());
                }else{
                    selectedStrings.remove(visitingPlace.getVisitingPlaceID());
                }

            }
        });


        return listViewItem;
    }
    public static ArrayList<String> getSelectedString(){
        return selectedStrings;
    }

}
