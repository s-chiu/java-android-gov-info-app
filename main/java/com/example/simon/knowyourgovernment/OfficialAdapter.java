package com.example.simon.knowyourgovernment;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by simon on 3/28/2017.
 */

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Official> OfficialList;
    private MainActivity mainAct;

    public OfficialAdapter(List<Official> NList, MainActivity ma) {
        this.OfficialList = NList;
        mainAct = ma;
    }
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.official_list, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Official note = OfficialList.get(position);
        holder.Office.setText(note.getOffice());
        holder.Name.setText(note.getName());
    }


    public int getItemCount() {
        return OfficialList.size();
    }
}
