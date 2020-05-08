package com.udacity.sandwichclub;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.Iterator;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mOriginTextView;
	private TextView mKnownAsTextView;
	private TextView mIngredientsTextView;
	private TextView mDescriptionTextView;
	private TextView mKnownAsTextLabel;
	private ImageView imageIv;
	private Context context = DetailActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageIv = findViewById(R.id.image_iv);
		mOriginTextView = (TextView) findViewById(R.id.origin_tv);
		mKnownAsTextView = (TextView) findViewById(R.id.also_known_tv);
		mIngredientsTextView = (TextView) findViewById(R.id.ingredients_tv);
		mDescriptionTextView = (TextView) findViewById(R.id.description_tv);
		mKnownAsTextLabel = (TextView) findViewById(R.id.also_known_label_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

	private void populateUI(Sandwich sandwich) {
		setTitle(sandwich.getMainName());

		Picasso.get()
			.load(sandwich.getImage())
			.into(imageIv);

    	String origin = sandwich.getPlaceOfOrigin();
    	if(origin.equals("")) {
			mOriginTextView.setText("Unknown");
		} else {
			mOriginTextView.setText(sandwich.getPlaceOfOrigin());
		}

		// add also known as names to the view
		Iterator<String> namesIterator = sandwich.getAlsoKnownAs().iterator();
		if(!namesIterator.hasNext()) {
			mKnownAsTextLabel.setVisibility(View.GONE);
			mKnownAsTextView.setVisibility(View.GONE);
		} else {
			while (namesIterator.hasNext()) {
				String name = namesIterator.next();
				// Last item
				if (!namesIterator.hasNext()) {
					mKnownAsTextView.append(name);
				} else {
					mKnownAsTextView.append(name + ", ");
				}
			}
		}

		// add ingredients to the view
		Iterator<String> ingredientsIterator = sandwich.getIngredients().iterator();
		while (ingredientsIterator.hasNext()) {
			String ingredient = ingredientsIterator.next();
			// Last item
			if (!ingredientsIterator.hasNext()) {
				mIngredientsTextView.append(ingredient);
			} else {
				mIngredientsTextView.append(ingredient + ", ");
			}
		}

		mDescriptionTextView.setText(sandwich.getDescription());
    }
}
