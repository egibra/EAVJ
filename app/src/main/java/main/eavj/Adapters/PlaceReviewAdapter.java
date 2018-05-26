package main.eavj.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import main.eavj.ObjectClasses.Review;
import main.eavj.R;

public class PlaceReviewAdapter extends ArrayAdapter<Review> {
    private Activity context;
    List<Review> reviews;

    public PlaceReviewAdapter(Activity context, List<Review> reviews) {
        super(context, R.layout.layout_review_list, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_review_list, parent, false);

        TextView textViewAuthor = (TextView) listViewItem.findViewById(R.id.textReviewAuthor);
        TextView textViewComment = (TextView) listViewItem.findViewById(R.id.textReviewComment);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textReviewDate);

        Review review = reviews.get(position);

        textViewAuthor.setText(review.getAuthor() + " reviewed:");
        textViewComment.setText(review.getComment());
        textViewDate.setText(review.getDate());

        return listViewItem;
    }
}
