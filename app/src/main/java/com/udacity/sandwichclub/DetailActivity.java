package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import static java.lang.System.load;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private ImageView sandwichImagen;
      private TextView originTx;
    private TextView originLabel;
    private TextView descriptionTx;
    private TextView ingredientTx;
    private TextView alsoKnownTx;
    private TextView alsoKnownLabelTx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        sandwichImagen = findViewById(R.id.image_iv);
        originTx = findViewById(R.id.origin_tv);
        originLabel = findViewById(R.id.placeOfOrigin_label);
        descriptionTx = findViewById(R.id.description_tv);
        ingredientTx = findViewById(R.id.ingredients_tv);
        alsoKnownTx = findViewById(R.id.also_known_tv);
        alsoKnownLabelTx = findViewById(R.id.alsoKnownAs_label);

        Intent intent = getIntent();
        if (intent == null) {
            dataError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            dataError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
             dataError();
            return;
        }

        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void dataError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getAlsoKnownAs().get(0));

            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); i++) {
                //stringBuilder.append(", ");
                stringBuilder.append(sandwich.getAlsoKnownAs().get(i)).append(", ");
            }
            alsoKnownTx.setText(stringBuilder.toString());
        } else {
            alsoKnownTx.setVisibility(View.GONE);
            alsoKnownLabelTx.setVisibility(View.GONE);
        }


        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            originTx.setVisibility(View.GONE);
            originLabel.setVisibility(View.GONE);
        } else {
            originTx.setText(sandwich.getPlaceOfOrigin());
        }

           descriptionTx.setText(sandwich.getDescription());

        if (sandwich.getIngredients() != null && sandwich.getIngredients().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < sandwich.getIngredients().size(); i++) {

                stringBuilder.append("\n");
                stringBuilder.append("\u2043"+" ");
                stringBuilder.append(sandwich.getIngredients().get(i));


            }
            ingredientTx.setText(stringBuilder.toString());
        }
        Picasso.with(this)

                .load(sandwich.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(sandwichImagen)
        ;


    }
}
