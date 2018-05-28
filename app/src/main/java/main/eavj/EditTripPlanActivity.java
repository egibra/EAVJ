package main.eavj;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import main.eavj.ObjectClasses.Trip;

/**
 * Created by vika on 26-Apr-18.
 */

public class EditTripPlanActivity extends Activity {
    ListView tripListView;
    DatabaseReference db;
    EditText tripTitle;
    EditText txtDateFrom;
    EditText txtDateTo;
    Button updateTrip;
    EditText tripBudget;
    CheckBox checkBoxSearchFriend;
    String tripId;
    Trip trip;
    List<Trip> trips;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit_trip_plan);


        tripId = intent.getStringExtra("TripID");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("trip").child(uid).child(tripId);

        trip = new Trip();

        tripTitle = (EditText)findViewById(R.id.tripTitle);
        txtDateFrom = (EditText)findViewById(R.id.dateFrom);
        txtDateTo = (EditText)findViewById(R.id.dateTo);
        tripBudget = (EditText) findViewById(R.id.tripBudget);
        checkBoxSearchFriend = (CheckBox) findViewById(R.id.searchFriend);

//        title.setText(intent.getStringExtra("TripID"));

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Trip trip = dataSnapshot.getValue(Trip.class);
                tripTitle.setText(trip.getTitle());
                txtDateFrom.setText(trip.getDateFrom());
                txtDateTo.setText(trip.getDateTo());
                tripBudget.setText(trip.getPrice().toString());
                Boolean isCheck = trip.getSearchForFriend();
                checkBoxSearchFriend.setChecked(isCheck);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        updateTrip = (Button) findViewById(R.id.updateTrip);

        updateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTrip();
            }
        });

    }

    public void updateTrip()
    {
        String title = tripTitle.getText().toString().trim();
        String dateFrom = txtDateFrom.getText().toString().trim();
        String dateTo = txtDateTo.getText().toString().trim();
        Boolean checkBox = checkBoxSearchFriend.isChecked();
        Float price;

        // validate empty fields
        if (TextUtils.isEmpty(title)
                || TextUtils.isEmpty(dateFrom)
                || TextUtils.isEmpty(dateTo)) {
            Toast.makeText(this, "All fields should be filled!", Toast.LENGTH_LONG).show();

            return;
        }

        // validate budget
        try {
            price = Float.parseFloat(tripBudget.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this,"Budget was not in correct format", Toast.LENGTH_LONG).show();

            return;
        }

        // validate dates
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        Date dateFromX = null;
        Date dateToX = null;

        // get yesterday date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        try {
            dateFromX = sdf.parse(dateFrom);
            dateToX = sdf.parse(dateTo);
            if (dateToX.before(dateFromX) || yesterday.after(dateFromX)) {
                Toast.makeText(this,"Invalid date order", Toast.LENGTH_LONG).show();

                return;
            }
        } catch (ParseException e) {
            Toast.makeText(this,"Invalid date format", Toast.LENGTH_LONG).show();

            return;
        }
        Trip trip = new Trip(tripId, title, dateFrom, dateTo, price, checkBox);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.setValue(trip);

        Toast.makeText(this, "Trip updated", Toast.LENGTH_LONG).show();

    }

    public void onStart() {
        super.onStart();
        EditText txtDateFrom = (EditText)findViewById(R.id.dateFrom);
        EditText txtDateTo = (EditText)findViewById(R.id.dateTo);

        txtDateFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

        txtDateTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
    }

    public void openEditPlaceWindow(View view)
    {
        Intent intent = new Intent(this, EditPlaceActivity.class);
        startActivity(intent);
    }
}
