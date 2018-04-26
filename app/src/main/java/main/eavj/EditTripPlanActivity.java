package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by vika on 26-Apr-18.
 */

public class EditTripPlanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip_plan);


    }

    public void openEditPlaceWindow(View view)
    {
        Intent intent = new Intent(this, EditPlaceActivity.class);
        startActivity(intent);
    }
}
