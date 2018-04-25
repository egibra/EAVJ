package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by vika on 14-Mar-18.
 */

public class TripIdeasActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_ideas);


    }

    public void openTripDetail(View view)
    {
        Intent intent = new Intent(this, TripDetailActivity.class);
        startActivity(intent);
    }
}
