package main.eavj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        db = FirebaseDatabase.getInstance().getReference("visiting places").child(intent.getStringExtra("CityID"));
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
