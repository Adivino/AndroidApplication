package com.mayassin.android.adivino;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

public class SearchTextActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText searchBarText;
    RecycleViewAdapterSearch adapterSearch;

    String[] knownPlayers = new String[]{"Messi", "Messy", "Ronaldo" , "Ronald", "Otters", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_text);
        initialize();
    }

    private void initialize() {
        initializeViews();

        initializeSearchKeyboard();

        initializeChangeListeners();

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
