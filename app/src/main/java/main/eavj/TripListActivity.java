package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;


public class TripListActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);


    }

    public void openMapWindow(View view)
    {
        Intent intent = new Intent(this, TripMapActivity.class);
        startActivity(intent);
    }

    public void openEditTripPlanWindow(View view)
    {
        Intent intent = new Intent(this, EditTripPlanActivity.class);
        startActivity(intent);
    }


}
