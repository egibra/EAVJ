package main.eavj;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.eavj.Adapters.VisitingPlaceAdapter;
import main.eavj.ObjectClasses.VisitingPlace;

public class CrudAdminVisitingPlaceActivity extends AppCompatActivity {

    Button buttonAddVisitingPlace;
    EditText editTextName;
    EditText editTextAddress;
    ListView listViewVisitingPlaces;
    EditText editTextX;
    EditText editTextY;
    RadioGroup radioGroupType;
    RadioButton btnEating;
    String category;
    DatabaseReference db;
    List<VisitingPlace> visitingPlaces;
    private String cityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cityID = intent.getStringExtra("CityID");
        db = FirebaseDatabase.getInstance().getReference("visiting places").child(cityID);
        setContentView(R.layout.activity_crud_admin_visiting_place);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);
        btnEating = (RadioButton)findViewById(R.id.radio_eating);
        radioGroupType = (RadioGroup) findViewById(R.id.radioGroup_type);
        buttonAddVisitingPlace = (Button)findViewById(R.id.buttonAddVisitingPlace);
        listViewVisitingPlaces = (ListView) findViewById(R.id.listViewVisitingPlaces);
        visitingPlaces = new ArrayList<>();
        btnEating.setChecked(true);
        category = "EATING";
        listViewVisitingPlaces.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                VisitingPlace visitingPlace = visitingPlaces.get(i);
                showUpdateDeleteDialog(visitingPlace.getVisitingPlaceID(),visitingPlace.getName());
                return true;
            }
        });
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                if(checkedID == R.id.radio_eating)
                {
                    category = "EATING";
                }
                if(checkedID == R.id.radio_sleeping)
                {
                    category = "SLEEPING";
                }
                if(checkedID == R.id.radio_visiting)
                {
                    category = "VISITING";
                }
            }
        });
        buttonAddVisitingPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertVisitingPlace();
            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                visitingPlaces.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    VisitingPlace visitingPlace = postSnapshot.getValue(VisitingPlace.class);
                    visitingPlaces.add(visitingPlace);
                }
                VisitingPlaceAdapter citiesListAdapter = new VisitingPlaceAdapter(CrudAdminVisitingPlaceActivity.this, visitingPlaces);
                listViewVisitingPlaces.setAdapter(citiesListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void showUpdateDeleteDialog(final String visitingPlaceID, String visitingPlaceName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_country_dialogue,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(visitingPlaceName);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateCountry);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteCountry);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateVisitingPlace(visitingPlaceID, name);
                    dialog.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteVisitingPlace(visitingPlaceID);
                dialog.dismiss();
            }
        });
    }
    private void deleteVisitingPlace(String visitingPlaceID) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("visiting places").child(cityID).child(visitingPlaceID);
        dr.removeValue();
    }

    private boolean updateVisitingPlace(String id, String name) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("visiting places").child(cityID).child(id).child("name");
        dR.setValue(name);
        Toast.makeText(getApplicationContext(), "Country Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    private void insertVisitingPlace() {
        String name = editTextName.getText().toString().trim();
        String address =  editTextAddress.getText().toString().trim();
        Float x;
        Float y;
        try {
            x = Float.parseFloat(editTextX.getText().toString());
            y = Float.parseFloat(editTextY.getText().toString());
        }
        catch (NumberFormatException e){
            Toast.makeText(CrudAdminVisitingPlaceActivity.this,"Input was not in correct format", Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)) {
            String id  = db.push().getKey();
            VisitingPlace visitingPlace = new VisitingPlace(id,name, category, address, x.toString(), y.toString());
            db.child(id).setValue(visitingPlace);
            Toast.makeText(this, "Visiting place saved", Toast.LENGTH_LONG).show();
            editTextName.setText("");
            editTextAddress.setText("");
        }
        else {
            Toast.makeText(this, "Please enter name and address", Toast.LENGTH_LONG).show();
        }
    }

}
