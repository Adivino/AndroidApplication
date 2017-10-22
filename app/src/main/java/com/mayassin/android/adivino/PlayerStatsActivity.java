package com.mayassin.android.adivino;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PlayerStatsActivity extends AppCompatActivity {

    String playerName, homeTeamName, awayTeamName;
    int homeScore, awayScore;
    TextView playerNameTV, homeTeamScoreTV, awayTeamScoreTV,predictionResult, ardivinoTV, performanceResultTV, threatResultTV, extraInfoTV;
    ImageView playerIcon, homeIcon, awayIcon;
    Button backButton;
    Map<String, Object> rawData = new HashMap<String, Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_player_stats);
        intializeViews();
        intializeData();
        setUpBackButton();

    }

    private void setUpBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void intializeViews() {
        playerNameTV = findViewById(R.id.player_name);
        homeTeamScoreTV = findViewById(R.id.home_team_score);
        awayTeamScoreTV = findViewById(R.id.away_team_score);
        ardivinoTV = findViewById(R.id.ardivino_score_result);
        predictionResult  = findViewById(R.id.prediction_result);
        performanceResultTV = findViewById(R.id.recent_performance_Result);
        threatResultTV = findViewById(R.id.threat_result);
        backButton = findViewById(R.id.back_button);
        extraInfoTV = findViewById(R.id.position_nationality_age);

        playerIcon = findViewById(R.id.profile_image);
        homeIcon = findViewById(R.id.home_team_image);
        awayIcon = findViewById(R.id.away_team_image);
    }

    private void intializeData() {
        playerName = getIntent().getStringExtra("player_name");
        String jsonData = getIntent().getStringExtra("json_data");
        Gson gson = new Gson();
        rawData = (Map<String, Object>)gson.fromJson(jsonData, rawData.getClass()).get("player_data");
        fillViewData();
        fillImageData();
    }

    private void fillImageData() {
        int homeTeamID = getResources().getIdentifier(homeTeamName , "drawable", getPackageName());
        homeIcon.setImageResource(homeTeamID);
        int awayTeamId = getResources().getIdentifier(awayTeamName , "drawable", getPackageName());
        awayIcon.setImageResource(awayTeamId);

        pullProfileIcon();
    }

    private void pullProfileIcon() {
        new DownloadImageTask((ImageView) findViewById(R.id.profile_image))
                .execute(rawData.get("Photo_URL").toString());
    }

    private void fillViewData() {
        String nationality = rawData.get("Nationality").toString().toUpperCase();
        String position = rawData.get("Preferred Positions").toString().toUpperCase();
        String age = (int) Double.parseDouble(rawData.get("Age").toString()) + "";

        homeScore = (int) Double.parseDouble(rawData.get("home_score").toString());
        awayScore = (int) Double.parseDouble(rawData.get("away_score").toString());
        homeTeamName = rawData.get("home_team").toString().replace(" ", "").toLowerCase();
        awayTeamName = rawData.get("away_team").toString().replace(" ", "").toLowerCase();
        ardivinoTV.setText(rawData.get("ict_index").toString());
        extraInfoTV.setText(nationality + " " + age + " " +position);


        playerName = toTitleCase(rawData.get("first_name").toString() + " " + rawData.get("last_name").toString());
        playerNameTV.setText(toTitleCase(playerName));
        setGamePredictionText();

        performanceResultTV.setText(((int)Double.parseDouble(rawData.get("form").toString()))*10 + "");
        threatResultTV.setText((int)Double.parseDouble(rawData.get("threat").toString()) + "");
    }

    private void setGamePredictionText() {
        homeTeamScoreTV.setText(homeScore+"");
        awayTeamScoreTV.setText(awayScore+"");

        if(homeScore == awayScore) {
            predictionResult.setText("DRAW");
            predictionResult.setTextColor(getResources().getColor(R.color.md_deep_orange_400));
        } else {
            if(homeScore > awayScore) {
                predictionResult.setText("WIN");

            } else {
                predictionResult.setText("LOSS");
                predictionResult.setTextColor(getResources().getColor(R.color.md_red_700));
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
