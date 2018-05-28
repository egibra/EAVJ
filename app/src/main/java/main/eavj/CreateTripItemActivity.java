package main.eavj;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.SparseBooleanArray;

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

import main.eavj.Adapters.TripAdapter;
import main.eavj.Adapters.VisitingPlaceCheckListAdapter;
import main.eavj.ObjectClasses.City;
import main.eavj.ObjectClasses.Trip;
import main.eavj.ObjectClasses.TripItem;
import main.eavj.ObjectClasses.VisitingPlace;
import main.eavj.ObjectClasses.VisitingPlaceItem;

public class CreateTripItemActivity extends AppCompatActivity {
    ListView placesListView;
    DatabaseReference databaseTripItem;
    DatabaseReference databaseTripItemPlace;
    DatabaseReference db;
    List<VisitingPlace> places;
    EditText tripTitle;
    EditText txtDateFrom;
    EditText txtDateTo;
    Button createTripItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_create_trip_item);
        databaseTripItem = FirebaseDatabase.getInstance().getReference("trip item");
        databaseTripItemPlace = FirebaseDatabase.getInstance().getReference("visiting place item");
        db = FirebaseDatabase.getInstance().getReference("visiting places");
        placesListView = (ListView) findViewById(R.id.visitingPlacesCheck);
        places = new ArrayList<>();
        tripTitle = (EditText) findViewById(R.id.tripItemName);
        txtDateFrom = (EditText) findViewById(R.id.dateFrom);
        txtDateTo = (EditText) findViewById(R.id.dateTo);
        createTripItem = (Button) findViewById(R.id.createTripItem);

        createTripItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTripItem();
            }
        });

//        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //getting the selected artist
//                VisitingPlace city = places.get(i);
//
//                //creating an intent
////                Intent intent = new Intent(getApplicationContext(), VisitingPlaceListActivity.class);
//
//                //putting artist name and id to intent
////                intent.putExtra("CityID", city.getCityID());
////                intent.putExtra("CityName", city.getCityName());
//
//                //starting the activity with intent
//                startActivity(intent);
//            }
//        });
    }

    public void addTripItem()
    {
        String title = tripTitle.getText().toString().trim();
        String dateFrom = txtDateFrom.getText().toString().trim();
        String dateTo = txtDateTo.getText().toString().trim();
        Float price;

        // validate empty fields
        if (TextUtils.isEmpty(title)||
                TextUtils.isEmpty(dateFrom)
                || TextUtils.isEmpty(dateTo)) {
            Toast.makeText(this, "All fields should be filled!", Toast.LENGTH_LONG).show();

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

        String id = databaseTripItem.push().getKey();
        TripItem tripItem = new TripItem(id, title, dateFrom, dateTo);
        Intent intent = getIntent();
        String tripId = intent.getStringExtra("TripID");
//
////        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseTripItem.child(tripId).child(id).setValue(tripItem);

        clearFields();
        Toast.makeText(this, "Trip item added", Toast.LENGTH_LONG).show();
        addTripItemPlace(id);
    }

    public void addTripItemPlace(String id){
        for (String p : VisitingPlaceCheckListAdapter.getSelectedString()) {
            String placeId = databaseTripItemPlace.push().getKey();
            VisitingPlaceItem item = new VisitingPlaceItem(placeId, p);
            databaseTripItemPlace.child(id).child(placeId).setValue(item);
        }
    }



    @Override
    protected void onStart() {
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


        //attaching value event listener
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                places.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {

                        VisitingPlace place = postSnapshot2.getValue(VisitingPlace.class);
                        places.add(place);
                }
                }
                VisitingPlaceCheckListAdapter placesAdapter = new VisitingPlaceCheckListAdapter(CreateTripItemActivity.this, places);
                placesListView.setAdapter(placesAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void clearFields()
    {
        txtDateFrom.setText("");
        txtDateTo.setText("");
    }
}
