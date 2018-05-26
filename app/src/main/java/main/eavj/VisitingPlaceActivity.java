package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.eavj.Adapters.CityAdapter;
import main.eavj.Adapters.PlaceReviewAdapter;
import main.eavj.Adapters.VisitingPlaceAdapter;
import main.eavj.ObjectClasses.Review;
import main.eavj.ObjectClasses.Trip;
import main.eavj.ObjectClasses.VisitingPlace;

public class VisitingPlaceActivity extends Activity {

    DatabaseReference databasePersonalPlaces;
    DatabaseReference databasePlaceReviews;
    DatabaseReference databaseUsers;

    TextView placeTitle;
    TextView placeAddress;
    TextView placeCategory;
    Button addToPersonalList;
    String visitingPlaceId;

    List<Review> reviews;
    ListView listViewPlaceReviews;

    EditText editTextComment;
    Button addReviewComment;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_visiting_place);

        Intent intent = getIntent();

        databasePersonalPlaces = FirebaseDatabase.getInstance().getReference("personal visiting places");
        placeTitle = (TextView) findViewById(R.id.placeTitle);
        placeAddress = (TextView) findViewById(R.id.placeAddress);
        placeCategory = (TextView) findViewById(R.id.placeCategory);
        addToPersonalList = (Button) findViewById(R.id.addToPersonalList);

        addToPersonalList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVisitingPlace();
            }
        });

        visitingPlaceId = intent.getStringExtra("VisitingPlaceID");
        placeTitle.setText(intent.getStringExtra("VisitingPlaceTitle"));
        placeAddress.setText(intent.getStringExtra("VisitingPlaceAddress"));
        placeCategory.setText(intent.getStringExtra("VisitingPlaceCategory"));

        databasePlaceReviews = FirebaseDatabase.getInstance().getReference("review").child(visitingPlaceId);
        reviews = new ArrayList<>();
        listViewPlaceReviews = (ListView) findViewById(R.id.listViewPlaceReviews);

        editTextComment = (EditText) findViewById(R.id.editTextComment);
        addReviewComment = (Button) findViewById(R.id.addReviewComment);

        addReviewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReviewComment();
            }
        });

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    protected void onStart() {
        super.onStart();

        databasePlaceReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviews.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Review review = postSnapshot.getValue(Review.class);
                    reviews.add(review);
                }
                PlaceReviewAdapter placeReviewAdapter = new PlaceReviewAdapter(VisitingPlaceActivity.this, reviews);
                listViewPlaceReviews.setAdapter(placeReviewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void addVisitingPlace() {
        databasePersonalPlaces.addListenerForSingleValueEvent(new ValueEventListener() {
            String id = databasePersonalPlaces.push().getKey();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).child(visitingPlaceId).exists()) {
                    Toast.makeText(
                            VisitingPlaceActivity.this,
                            "You have already added this place to personal list",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    databasePersonalPlaces.child(uid).child(visitingPlaceId).setValue(visitingPlaceId);
                    Toast.makeText(
                            VisitingPlaceActivity.this,
                            "Visiting place added successfully",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void addReviewComment() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userName = databaseUsers.child(uid).child("userName");

        userName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String author = dataSnapshot.getValue(String.class);
                String comment = editTextComment.getText().toString().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String date = sdf.format(now);

                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(
                            VisitingPlaceActivity.this,
                            "Your review comment is empty!",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                String id = databasePlaceReviews.push().getKey();
                Review review = new Review(id, author, comment, date);
                databasePlaceReviews.child(id).setValue(review);
                editTextComment.setText("");
                Toast.makeText(VisitingPlaceActivity.this, "Comment added", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}