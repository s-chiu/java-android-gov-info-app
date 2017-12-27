package com.example.simon.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by simon on 4/16/2017.
 */

public class OfficialActivity extends AppCompatActivity {

    TextView location;
    String loc;
    String photourl;
    String name1;
    String office1;
    String party1;
    Official off;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        location = (TextView) findViewById(R.id.location);
        View n=findViewById(R.id.rectangle_at_the_top);
        n.setBackgroundColor(Color.GRAY);
        TextView name = (TextView) findViewById(R.id.name);
        TextView office= (TextView) findViewById(R.id.office);
        TextView party = (TextView) findViewById(R.id.party);
        TextView address = (TextView) findViewById(R.id.address);
        TextView address_l = (TextView) findViewById(R.id.address_l);
        TextView phone =(TextView) findViewById(R.id.phone);
        TextView phone_l = (TextView) findViewById(R.id.phone_l);
        TextView email= (TextView) findViewById(R.id.email);
        TextView email_l = (TextView) findViewById(R.id.email_l);
        TextView website=(TextView) findViewById(R.id.website);
        TextView website_l= (TextView) findViewById(R.id.website_l);
        Intent intent = getIntent();
        //get data from mainactivity
        final Official official = (Official) intent.getSerializableExtra("official");
        off=official;
        String k = intent.getExtras().getString("location");
        //set element colors
        name.setTextColor(Color.WHITE);
        office.setTextColor(Color.WHITE);
        party.setTextColor(Color.WHITE);
        address.setTextColor(Color.WHITE);
        address_l.setTextColor(Color.WHITE);
        phone.setTextColor(Color.WHITE);
        phone_l.setTextColor(Color.WHITE);
        email.setTextColor(Color.WHITE);
        email_l.setTextColor(Color.WHITE);
        website.setTextColor(Color.WHITE);
        website_l.setTextColor(Color.WHITE);
        location.setTextColor(Color.WHITE);
        //set elements
        name.setText(official.getName());
        name1=official.getName();
        office1=official.getOffice();
        party1=official.getParty();
        office.setText(official.getOffice());
        party.setText(official.getParty());
        address.setText(official.getOffice_Address());
        phone.setText(official.getPhone());
        email.setText(official.getEmail());
        website.setText(official.getWebsite());
        location.setText(k);
        loc=k;
        //set background color
        ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.container);
        if((official.getParty().equals("Democratic"))||official.getParty().equals("Democrat")) {
            bgElement.setBackgroundColor(Color.BLUE);
        }
        else if ((official.getParty().equals("Republican"))||official.getParty().equals("Republican")) {
            bgElement.setBackgroundColor(Color.RED);
        }
        else {
            bgElement.setBackgroundColor(Color.BLACK);
        }
        ImageButton imgfb=(ImageButton) findViewById(R.id.facebook);
        ImageButton imgtw=(ImageButton) findViewById(R.id.twitter);
        ImageButton imggg=(ImageButton) findViewById(R.id.google);
        ImageButton imgyt=(ImageButton) findViewById(R.id.youtube);
        imgfb.setVisibility(View.VISIBLE);
        imgtw.setVisibility(View.VISIBLE);
        imggg.setVisibility(View.VISIBLE);
        imgyt.setVisibility(View.VISIBLE);
        if(official.getFacebook().equals("")){
            imgfb=(ImageButton) findViewById(R.id.facebook);
            imgfb.setVisibility(View.INVISIBLE);
        }
        if(official.getTwitter().equals("")){
            imgtw=(ImageButton) findViewById(R.id.twitter);
            imgtw.setVisibility(View.INVISIBLE);
        }
        if(official.getGoogle().equals("")){
            imggg=(ImageButton) findViewById(R.id.google);
            imggg.setVisibility(View.INVISIBLE);
        }
        if(official.getYoutube().equals("")){
            imgyt=(ImageButton) findViewById(R.id.youtube);
            imgyt.setVisibility(View.INVISIBLE);
        }
        //photoactivity
        photourl=official.getPhoto_url();
        if(!(official.getPhoto_url().equals(""))) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = official.getPhoto_url().replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into((ImageButton) findViewById(R.id.picture));
                }
            }).build();
            picasso.load(official.getPhoto_url())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into((ImageButton) findViewById(R.id.picture));
        }else{
                Picasso.with(this).load(official.getPhoto_url())
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.missingimage)
                        .into((ImageButton) findViewById(R.id.picture));
            }

        }

    public void photoActivity(View v){
        Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
        intent.putExtra("location", loc);
        intent.putExtra("url", photourl);
        intent.putExtra("name", name1);
        intent.putExtra("office", office1);
        intent.putExtra("party", party1);
        startActivity(intent);
    }
    public void facebookClicked(View v){
        String FACEBOOK_URL="https://www.facebook.com/"+off.getFacebook();
        String urlToUse;
        PackageManager packageManager=getPackageManager();
        try{
            int versionCode=packageManager.getPackageInfo("com.facebook.katana",0).versionCode;
            if(versionCode>=3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }
            else{
                urlToUse="fb://page/"+off.getFacebook();
            }
        }
        catch(PackageManager.NameNotFoundException e){
            urlToUse=FACEBOOK_URL;
        }
        Intent facebookIntent=new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);

    }
    public void youtubeClicked(View v){
        String name=off.getYoutube();
        Intent intent=null;
        try{
            intent=new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/"+name));
            startActivity(intent);
        }
        catch(ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/"+name)));
        }


    }
    public void googleClicked(View v){
        String name=off.getGoogle();
        Intent intent=null;
        try{
            intent=new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus","com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri",name);
            startActivity(intent);
        }
        catch(ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://plus.google.com/"+name)));

        }

    }
    public void twitterClicked(View v){
        Intent intent=null;
        String name=off.getTwitter();
        try{
            getPackageManager().getPackageInfo("com.twitter.android",0);
            intent= new Intent(Intent.ACTION_VIEW,Uri.parse("twitter://user?screen_name="+name));
        }
        catch(Exception e){
            intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/"+name));
        }
    startActivity(intent);
    }
}

    //////////////////////////////
