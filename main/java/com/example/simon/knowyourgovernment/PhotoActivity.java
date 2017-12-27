package com.example.simon.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by simon on 4/16/2017.
 */

public class PhotoActivity  extends AppCompatActivity {
    TextView location;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        TextView name = (TextView) findViewById(R.id.name);
        TextView office= (TextView) findViewById(R.id.office);
        name.setTextColor(Color.WHITE);
        office.setTextColor(Color.WHITE);
        View n=findViewById(R.id.rectangle_at_the_top);
        n.setBackgroundColor(Color.GRAY);
        location = (TextView) findViewById(R.id.location);
        location.setTextColor(Color.WHITE);
        Intent intent = getIntent();
        location.setText(intent.getExtras().getString("location"));
        name.setText(intent.getExtras().getString("name"));
        office.setText(intent.getExtras().getString("office"));
        String party=intent.getExtras().getString("party");
        final String url=intent.getExtras().getString("url");
        ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.container);
        if((party.equals("Democratic"))||party.equals("Democrat")) {
            bgElement.setBackgroundColor(Color.BLUE);
        }
        else if ((party.equals("Republican"))||party.equals("Republican")) {
            bgElement.setBackgroundColor(Color.RED);
        }
        else {
            bgElement.setBackgroundColor(Color.BLACK);
        }
        //photo
        if(url.equals("")) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = url.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into((ImageView) findViewById(R.id.portrait));
                }
            }).build();
            picasso.load(url)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into((ImageView) findViewById(R.id.portrait));
        }else{
            Picasso.with(this).load(url)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into((ImageView) findViewById(R.id.portrait));
        }
    }
}
