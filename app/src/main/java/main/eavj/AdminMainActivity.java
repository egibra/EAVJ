package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Button countries = (Button) findViewById(R.id.adminCountries);
        Button visitingPlaces = (Button) findViewById(R.id.adminVisitingPlace);
        Button users = (Button) findViewById(R.id.adminUsers);
        Button comments = (Button) findViewById(R.id.adminComments);
        countries.setOnClickListener(this);
        visitingPlaces.setOnClickListener(this);
        users.setOnClickListener(this);
        comments.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.adminCountries:
                startActivity(new Intent(AdminMainActivity.this, EditAdminCountryActivity.class));
                break;
            case R.id.adminVisitingPlace:
                startActivity(new Intent( AdminMainActivity.this, EditAdminVisitingPlaceActivity.class));
                break;
            case R.id.adminUsers:
                break;
            case R.id.adminComments:
                break;

        }
    }
}
