package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private TextView word;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private MeaningAdapter meaningAdapter;
    private ArrayList<MeaningModel> meaningModelArrayList;
    private String aud="";
    private CardView sound;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.search_view);
        word = findViewById(R.id.word);
        sound = findViewById(R.id.audio_btn);
        recyclerView = findViewById(R.id.list_items);
        progressBar = findViewById(R.id.progress_bar);
        linearLayout = findViewById(R.id.main_layout);
        meaningModelArrayList = new ArrayList<>();
        meaningAdapter = new MeaningAdapter(this, meaningModelArrayList);
        recyclerView.setAdapter(meaningAdapter);
        Toast.makeText(this, "Created by Sandip Basak", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        getMeaning("dream");
        mediaPlayer = new MediaPlayer();
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                AudioPlay();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals("")){
                    Toast.makeText(MainActivity.this, "Enter a word !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    getMeaning(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void getMeaning(String query) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + query;
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                meaningModelArrayList.clear();
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    word.setText(jsonObject.getString("word"));
                    JSONArray phonetics = jsonObject.getJSONArray("phonetics");
                    aud="";

                    for(int i=0;i<phonetics.length();i++){
                        JSONObject aud_object = phonetics.getJSONObject(i);
                        if(aud.equals("")){
                            aud = aud_object.getString("audio");
                        }
                    }
                    Log.d("myTag", aud);
                    JSONArray meanings = jsonObject.getJSONArray("meanings");
                    for(int i=0;i<meanings.length();i++){
                        JSONObject speech = meanings.getJSONObject(i);
                        String Parts_of_Speech = speech.getString("partOfSpeech");
                        JSONArray definitions = speech.getJSONArray("definitions");
                        for(int j=0;j<definitions.length();j++){
                            String m = definitions.getJSONObject(j).getString("definition");
                            meaningModelArrayList.add(new MeaningModel(Parts_of_Speech, m));
                        }
                    }
                    meaningAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                catch (Exception e){
                    Log.d("myTag", "try-catch error");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "Some Error Occurred");
                Toast.makeText(MainActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void AudioPlay(){
        Log.d("myTag","Button pressed");
        try{
            mediaPlayer.setDataSource(aud);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}