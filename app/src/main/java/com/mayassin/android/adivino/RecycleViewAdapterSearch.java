package com.mayassin.android.adivino;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamed on 10/21/17.
 */

public class RecycleViewAdapterSearch extends RecyclerView.Adapter<RecycleViewAdapterSearch.CustomViewHolder> {
    private ArrayList<String> searchResults;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public RecycleViewAdapterSearch(ArrayList<String> allPosts) {
        this.searchResults = allPosts;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_results, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecycleViewAdapterSearch.CustomViewHolder holder, int position) {
        String resultText = searchResults.get(position);

        holder.searchText.setText(resultText);
    }

    @Override
    public int getItemCount() {
        return (null != searchResults ? searchResults.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView searchText;
        protected LinearLayout layout;

        public CustomViewHolder(View view) {
            super(view);
            this.searchText = (TextView) view.findViewById(R.id.search_result_row_text_view);
            this.layout = (LinearLayout) view.findViewById(R.id.results_layout) ;
            layout.setOnClickListener(layoutOnClickListener);
        }

        private View.OnClickListener layoutOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = searchResults.get(getAdapterPosition());

                Intent i = new Intent("SEARCHTEXTRECEIVED");
                i.putExtra("player_name", result);
                LocalBroadcastManager.getInstance(searchText.getContext())
                        .sendBroadcast(i);

// locaterInterface.goToExhibit(allExhibits.get(getAdapterPosition()).getTitle());
//                popUpInterface.popUp(post);
            }
        };
    }


}