package main.eavj;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
//import android.os.Bundle;
//import android.app.Activity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.PopupMenu;
//import android.widget.Toast;

public class MainActivity extends Activity {

//    Button button1;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCreateTripWindow(View view)
    {
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
    }

    public void openVisitingPlaceListWindow(View view)
    {
        Intent intent = new Intent(this, VisitingPlaceCountryListActivity.class);
        startActivity(intent);
    }

    public void openTripListWindow(View view)
    {
        Intent intentt = new Intent(getApplicationContext(), TripListActivity.class);
        Intent intent = getIntent();
        //putting artist name and id to intent
        intentt.putExtra("userID", intent.getStringExtra("userID"));

        //starting the activity with intent
        startActivity(intentt);
    }

    public void openTripIdeasWindow(View view)
    {
        Intent intent = new Intent(this, TripIdeasActivity.class);
        startActivity(intent);
    }

    public void openFriendSearchWindow(View view) {
        Intent intent = new Intent(this, FriendSearchActivity.class);
        startActivity(intent);
    }
//        setContentView(R.layout.activity_main);
//
//        button1 = (Button) findViewById(R.id.button1);
//        button1.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //Creating the instance of PopupMenu
//                PopupMenu popup = new PopupMenu(MainActivity.this, button1);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//
//                popup.show();//showing popup menu
//            }
//        });//closing the setOnClickListener method
//    }
}
