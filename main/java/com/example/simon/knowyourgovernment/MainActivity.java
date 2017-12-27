package com.example.simon.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity  extends AppCompatActivity   implements View.OnClickListener, View.OnLongClickListener {

    private List<Official> OfficialList = new ArrayList<>();  // Main content is here
    private List<Official> newOfficialList = new ArrayList<>();
    private RecyclerView recyclerView; // Layout's recyclerview
    private OfficialAdapter mAdapter; // Data to recyclerview adapter
    TextView location;
    String zipcode;
    String add_string;
    TextView no_net;
    TextView no_net2;


    private static final String TAG = "OfficialActivity";
    private Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.official_view);
        no_net=(TextView) findViewById(R.id.no_net);
        no_net2=(TextView) findViewById(R.id.no_net2);
        no_net.setVisibility(View.INVISIBLE);
        no_net2.setVisibility(View.INVISIBLE);
        mAdapter = new OfficialAdapter(OfficialList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        location= (TextView) findViewById(R.id.location);
        View n=findViewById(R.id.rectangle_at_the_top);
        n.setBackgroundColor(Color.GRAY);
        location.setTextColor(Color.WHITE);
        if (isNetworkAvailable() == false) {
            Toast.makeText(this,R.string.no_net, Toast.LENGTH_SHORT).show();
        }
        else
        locator = new Locator(this);
        getOfficialData(zipcode);
        location.setText(add_string);
        setList(newOfficialList);
    }
    //onresume
    public void onResume(){
        super.onResume();
    }
    //create options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;
    }
    //options menu function, switch to info or search new location
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
                return true;
            case R.id.Search:
                LayoutInflater inflater = LayoutInflater.from(this);
                final View view = inflater.inflate(R.layout.dialog_address, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.enter_value);
                builder.setView(view);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et1 = (EditText) view.findViewById(R.id.address_input);
                        String add = doAddressSearch(et1.getText().toString());
                        if (add == null) {
                            Context context = getApplicationContext();
                            Toast.makeText(context, R.string.no_result, Toast.LENGTH_SHORT).show();
                        } else {
                            mAdapter.notifyDataSetChanged();
                            getOfficialData(zipcode);
                            location.setText(add_string);
                            setList(newOfficialList);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return false;
    }
    //onclick, go to ofificial activity
    public void onClick(View v) {
        //Toast.makeText(this,"click",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o =OfficialList.get(pos);
        intent.putExtra("location", add_string);
        intent.putExtra("official", o);
        startActivity(intent);
    }
    //OnLongClick
    public boolean onLongClick(final View v) {
        onClick(v);
        return false;
    }
    //async loader
    public void getOfficialData(String s){
        AsyncOfficialLoader alt=new AsyncOfficialLoader(this);
        try {
            alt.execute(s).get();
        }
        catch (Exception e){}
    }
    //populate list from async task
    public void setOfficialData(List<Official> olist) {
        for (int i = 0; i < olist.size(); i++) {
            newOfficialList.add(olist.get(i));
        }
    }
    public void setList(List<Official> olist){
        OfficialList.clear();
        mAdapter.notifyDataSetChanged();
        for (int i = 0; i < olist.size(); i++) {
            OfficialList.add(olist.get(i));
            mAdapter.notifyDataSetChanged();
        }
        newOfficialList.clear();
    }

    public void setLocation(String city, String state, String zip){
        add_string=city+", "+state+" "+zip;;
        Log.d("new location",city+", "+state+" "+zip);
    }
    //check for internet connectivity, return true for yes
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
/////////////////location code search
public void setData(double lat, double lon) {
    Log.d(TAG, "setData: Lat: " + lat + ", Lon: " + lon);
    String address = doAddress(lat, lon);
    //add_string=address;
    //((TextView) findViewById(R.id.location)).setText(address);
}
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }
    private String doAddress(double latitude, double longitude) {
        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);
        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(
                        latitude, longitude, 1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                return null;
            }
        }
        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {;
            return null;
        }
        else {
           zipcode= addresses.get(0).getPostalCode();
            String result = "";
                result = addresses.get(0).getLocality()+", "+  addresses.get(0).getAdminArea()+" "+ addresses.get(0).getPostalCode();
            return result;
        }
    }

    private String doAddressSearch(String address) {
        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocationName(
                        address, 1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                return null;
            }
        }
        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {;
            return null;
        }
        else {
            zipcode= addresses.get(0).getPostalCode();
            String result = "";
            result = addresses.get(0).getLocality()+", "+  addresses.get(0).getAdminArea()+" "+ addresses.get(0).getPostalCode();
            return result;
        }
    }
    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }
////////////////////////////

}