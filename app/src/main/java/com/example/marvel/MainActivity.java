package com.example.marvel;

import static com.example.marvel.ApiCall.PRIVATE_API_KEY;
import static com.example.marvel.ApiCall.PUBLIC_API_KEY;
import static javax.crypto.Cipher.PRIVATE_KEY;
import static javax.crypto.Cipher.PUBLIC_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CharactersListAdapter.OnItemSelected {

    public static final String CHARACTER_ID = "CHARACTER_ID";
    public static final String CHARACTER = "CHARACTER";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loadMoreBTN)
    Button loadMoreBTN;

    private CharactersListAdapter charactersListAdapter;
    private ArrayList<Character> results = new ArrayList<>();
    private int limit = 10, offset = 0, total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupAdapter(results);
        getCharacterList();
    }

    private void getCharacterList() {
        long ts = new Date().getTime();
        String stringToHash = ts + PRIVATE_API_KEY + PUBLIC_API_KEY;
        String hash = new HashCreator().createSHAHash(stringToHash);
        new ApiCall().getMarvelApi().getCharacters(limit, offset, PUBLIC_API_KEY, hash, String.valueOf(ts)).enqueue(new Callback<CharactersList>() {
            @Override
            public void onResponse(Call<CharactersList> call, Response<CharactersList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData()!=null && response.body().getData().getResults()!=null) {
                        total = response.body().getData().getTotal();
                        results.addAll(response.body().getData().getResults());
                        charactersListAdapter.notifyDataSetChanged();
                        if(results.size() < total) {
                            loadMoreBTN.setVisibility(View.VISIBLE);
                        } else {
                            loadMoreBTN.setVisibility(View.GONE);
                        }
                    }
                }  else {
                    JSONObject jsonObj;
                    try {
                        if(response.errorBody() != null) {
                            jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            Toast.makeText(MainActivity.this, jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(MainActivity.this, getString(R.string.errors_occurred), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CharactersList> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupAdapter(ArrayList<Character> results) {
        charactersListAdapter = new CharactersListAdapter(this, results, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(charactersListAdapter);
    }

    @OnClick(R.id.loadMoreBTN)
    void loadMoreCharacters() {
            offset = offset + limit;
            getCharacterList();
    }

    @Override
    public void onCharacterSelected(Long characterId, Character character) {
        Intent intent = new Intent(this, CharacterDetailsActivity.class);
        intent.putExtra(CHARACTER_ID, characterId);
        intent.putExtra(CHARACTER, character);
        startActivity(intent);
    }
}