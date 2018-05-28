package main.eavj;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.AlertDialog;
import android.app.Dialog;

import main.eavj.ObjectClasses.Trip;

public class CreateTripActivity extends Activity {

    DatabaseReference databaseTrip;
    EditText tripTitle;
    EditText txtDateFrom;
    CheckBox checBoxSearchFriend;
    EditText txtDateTo;
    EditText tripBudget;
    Button createTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Intent intent = getIntent();

        databaseTrip = FirebaseDatabase.getInstance().getReference("trip");
        tripTitle = (EditText) findViewById(R.id.tripTitle);
        txtDateFrom = (EditText) findViewById(R.id.txtDateFrom);
        txtDateTo = (EditText) findViewById(R.id.txtDateTo);
        tripBudget = (EditText) findViewById(R.id.tripBudget);
        createTrip = (Button) findViewById(R.id.createTrip);
        checBoxSearchFriend = (CheckBox) findViewById(R.id.searchFriend);

        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTrip();
            }
        });
    }

    public void onStart() {
        super.onStart();
        EditText txtDateFrom = (EditText)findViewById(R.id.txtDateFrom);
        EditText txtDateTo = (EditText)findViewById(R.id.txtDateTo);

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

    public void addTrip()
    {
        String title = tripTitle.getText().toString().trim();
        String dateFrom = txtDateFrom.getText().toString().trim();
        String dateTo = txtDateTo.getText().toString().trim();
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

        String id = databaseTrip.push().getKey();
        Trip trip = new Trip(id, title, dateFrom, dateTo, price, checBoxSearchFriend.isChecked());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseTrip.child(uid).child(id).setValue(trip);
        clearFields();
        Toast.makeText(this, "Trip added", Toast.LENGTH_LONG).show();
        confirmDialog(id);
    }
    private void confirmDialog(final String id) {
        final AlertDialog.Builder tripAsk = new AlertDialog.Builder(CreateTripActivity.this);
        tripAsk.setMessage("Do you want add trip item?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentt = new Intent(getApplicationContext(), CreateTripItemActivity.class);

                        intentt.putExtra("TripID", id);

                        getApplicationContext().startActivity(intentt);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });
        AlertDialog alert = tripAsk.create();
        alert.setTitle("Dialog");
        alert.show();

    }


    private void clearFields()
    {
        tripTitle.setText("");
        txtDateFrom.setText("");
        txtDateTo.setText("");
        tripBudget.setText("");
    }
}
