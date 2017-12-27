package com.example.simon.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by simon on 3/28/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView Office;
    public TextView Name;
    public TextView Party;
    public TextView Photo_url;
    public TextView Office_Address;
    public TextView Phone;
    public TextView Email;
    public TextView Website;
    public TextView Facebook;
    public TextView Twitter;
    public TextView Google;
    public TextView Youtube;


    public MyViewHolder(View view) {
        super(view);
        //StockSymbol=(TextView) view.findViewById(R.id.stock_symbol);
        Office=(TextView) view.findViewById(R.id.office);
        Name=(TextView) view.findViewById((R.id.name));
    }
}
