package main.eavj.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import main.eavj.CreateTripActivity;
import main.eavj.CreateTripItemActivity;
import main.eavj.EditTripPlanActivity;
import main.eavj.ObjectClasses.Trip;
import main.eavj.R;
import main.eavj.TripDetailActivity;
import main.eavj.TripListActivity;

public class TripAdapter extends ArrayAdapter<Trip> {
    private Activity context;
    List<Trip> trips;
    private AlertDialog mDialog;
    private int mListRowPosition;
    DatabaseReference databaseTrip;

    public TripAdapter(Activity context, final List<Trip> trips) {
        super(context, R.layout.layout_trip_list, trips);
        this.context = context;
        this.trips = trips;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_trip_list, parent, false);
        final Trip trip = trips.get(position);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tripTitle);
        Button viewBtn = (Button) listViewItem.findViewById(R.id.tripView);
        Button editBtn = (Button) listViewItem.findViewById(R.id.tripEdit);
        Button deleteBtn = (Button) listViewItem.findViewById(R.id.tripDelete);

        viewBtn.setTag(position);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = ((Activity) context).getIntent();
                Intent i= new Intent(getContext(), TripDetailActivity.class);
                i.putExtra("TripID", trip.getId());
                getContext().startActivity(i);
            }
        });

        editBtn.setTag(position);
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = ((Activity) context).getIntent();
                Intent i= new Intent(getContext(), EditTripPlanActivity.class);
                i.putExtra("TripID", trip.getId());
                getContext().startActivity(i);
            }
        });
        deleteBtn.setTag(position);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showDialog(position);
            }
        });



//        String name = trip.getTitle();
        textViewName.setText(trip.getTitle());

        return listViewItem;
    }
    private void showDialog(final int position)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want delete trip?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseTrip = FirebaseDatabase.getInstance().getReference("trip").child(uid);
                        databaseTrip.child(trips.get(position).getId()).removeValue();
//                        trips.remove(i);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        // Create the AlertDialog object

        mDialog = builder.create();
            mDialog.show();
    }

}
