package com.mayassin.android.adivino;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

public class SearchTextActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText searchBarText;
    RecycleViewAdapterSearch adapterSearch;

    private BroadcastReceiver searchTextSelectedReceiver;


    String[] knownPlayers = new String[]{"Messi", "Messy", "Ronaldo" , "Ronald", "Otters", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_text);
        initialize();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void initialize() {
        initializeViews();
        knownPlayers = this.getResources().getStringArray(R.array.player_names);

        initializeSearchKeyboard();

        initializeChangeListeners();

        searchTextSelectedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent broadCastIntent) {
                final String playerName = broadCastIntent.getStringExtra("player_name");

                new Thread(new Runnable() {
                    public void run() {
                        try {


                            Log.e("OPENING: ", "OPENING: "+playerName);
                            Server server = new Server();
                            String jsonData = server.post("http://40.114.51.138:9612/get_score", "{\"detected_text\" : \""+playerName+"\"}");
                            Intent intent = new Intent(getBaseContext(), PlayerStatsActivity.class);
                            intent.putExtra("json_data", jsonData);
                            intent.putExtra("player_id", playerName);
                            startActivity(intent);

                        } catch (final Exception e) {
                            Log.e("ERRORR SERVER", e.getMessage());
                        }
                    }
                }).start();

                try {

                } catch (Exception e)
                {
                    Log.e("ERRORR SERVER", e.getMessage() + " ");
                }
            }
        };


        LocalBroadcastManager.getInstance(this)
                .registerReceiver(searchTextSelectedReceiver, new IntentFilter("SEARCHTEXTRECEIVED"));

    }

    private void initializeChangeListeners() {
        searchBarText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                if(newText.length() > 0) {
                    intializeSearch(newText);
                }
            }
        });
    }

    private void initializeSearchKeyboard() {
        searchBarText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBarText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void initializeViews() {
        searchBarText = (EditText) findViewById(R.id.search_activity_edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.search_results_recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void intializeSearch(String searchText){
        ArrayList<String> matchingStrings = new ArrayList<>();

        for(String player : knownPlayers) {
            if(player.toLowerCase().contains(searchText.toLowerCase())) {
                matchingStrings.add(player);
            }
        }

        adapterSearch = new RecycleViewAdapterSearch(matchingStrings);
        recyclerView.setAdapter(adapterSearch);

    }
}
