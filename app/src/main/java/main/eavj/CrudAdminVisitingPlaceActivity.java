package main.eavj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class CrudAdminVisitingPlaceActivity extends AppCompatActivity {

    Button buttonAddCity;
    EditText editTextName;
    TextView textViewCity;
    ListView listViewVisitingPlaces;
    EditText editTextX;
    EditText editTextY;
    RadioButton btnEating;
    RadioButton btnSleeping;
    RadioButton btnVisiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_admin_visiting_place);
        btnEating = (RadioButton)findViewById(R.id.radio_eating);
        btnSleeping = (RadioButton) findViewById(R.id.radio_sleeping);
        btnVisiting = (RadioButton) findViewById(R.id.radio_visiting);
        btnEating.setChecked(true);

    }
}
