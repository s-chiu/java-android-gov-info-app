package com.example.simon.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by simon on 4/23/2017.
 */

public class AsyncOfficialLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    public String apikey="AIzaSyBSgYnGxgoozKr0UPg5Mw8UV2Fv_1QpmVo";
    public String url1= "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    public String url2= "&address=";
    public AsyncOfficialLoader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPostExecute(String s) {
    }
    public String getURL(String arg){
        Log.d("url", url1+apikey+url2+arg);
        return url1+apikey+url2+arg;
    }
    @Override
    protected String doInBackground(String... params){
        String k=params[0];
        StringBuilder sb = new StringBuilder();
        Uri dataUri = Uri.parse(getURL(k));
        String urlToUse = dataUri.toString();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
        }
        parseJSON(sb.toString());
        return null;
    }
    private void parseJSON(String s) {
        try{
            JSONObject array=new JSONObject(s);
            //get location data
            JSONObject ninput=array.getJSONObject("normalizedInput");
            mainActivity.setLocation(ninput.getString("city"),ninput.getString("state"),ninput.getString("zip"));
            List<Official> olist=new ArrayList<>();
            // check if exists if (ninput.has("city"))
            //get office and indices
            JSONArray offices=array.getJSONArray("offices");
            List<String> office=new ArrayList<>();
            List<String> indice=new ArrayList<>();
            for (int i=0;i<offices.length();i++){
                JSONArray y=offices.getJSONObject(i).getJSONArray("officialIndices");
                List<String> ind=new ArrayList<>();
                for(int k=0;k<y.length();k++){
                    ind.add(y.get(k).toString());
                }
                for (int z=0;z<ind.size();z++){
                    office.add(offices.getJSONObject(i).getString("name"));
                    indice.add(ind.get(z));
                }
            }
            //create official list
            JSONArray officials=array.getJSONArray("officials");
            for(int i=0;i<office.size();i++){
                //for(int k=0;i<indice.get(i).size();k++){
                JSONObject oj=officials.getJSONObject(Integer.parseInt(indice.get(i)));
                    Official t=new Official();
                    t.setOffice(office.get(i));
                    t.setName(oj.getString("name"));
                    if(!(oj.has("address"))){
                        t.setOffice_Address("No Data Provided");
                    }
                else{
                        JSONObject qw=oj.getJSONArray("address").getJSONObject(0);
                        t.setOffice_Address(qw.getString("city")+" "+qw.getString("state")+" "+qw.getString("zip"));
                    }
                   if(!(oj.has("party"))){
                        t.setParty("Unknown");
                    }
                    else{
                        t.setParty(oj.getString("party"));
                    }
                if(!(oj.has("phones"))){
                    t.setPhone("No Data Provided");
                }
                else{
                    t.setPhone(oj.getJSONArray("phones").get(0).toString());
                }
                if(!(oj.has("urls"))){
                    t.setWebsite("No Data Provided");
                }
                else{
                    t.setWebsite(oj.getJSONArray("urls").get(0).toString());;
                }
                if(!(oj.has("photoUrl"))){
                    t.setPhoto_url("No Data Provided");
                }

                else{
                    t.setPhoto_url(oj.getString("photoUrl"));
                }
                if(!(oj.has("emails"))){
                    t.setEmail("No Data Provided");
                }
                else{
                    t.setEmail(oj.getJSONArray("emails").get(0).toString());
                }
                if(oj.has("channels")) {
                    JSONArray ty = oj.getJSONArray("channels");
                    for(int r=0;r<ty.length();r++){
                        if(ty.getJSONObject(r).get("type").toString().equals("GooglePlus")){
                            t.setGoogle(ty.getJSONObject(r).get("id").toString());
                        }
                        if(ty.getJSONObject(r).get("type").toString().equals("Facebook")){
                            t.setFacebook(ty.getJSONObject(r).get("id").toString());
                        }
                        if(ty.getJSONObject(r).get("type").toString().equals("Twitter")){
                            t.setTwitter(ty.getJSONObject(r).get("id").toString());
                        }
                        if(ty.getJSONObject(r).get("type").toString().equals("YouTube")){
                            t.setYoutube(ty.getJSONObject(r).get("id").toString());
                        }
                    }
                }
                    olist.add(t);
           //     }
            }
            mainActivity.setOfficialData(olist);
        }
        catch (Exception e){
            Log.d("error",e.toString());
        }

    }
}
