package com.example.marvel;

import static com.example.marvel.ApiCall.PRIVATE_API_KEY;
import static com.example.marvel.ApiCall.PUBLIC_API_KEY;
import static com.example.marvel.MainActivity.CHARACTER;
import static com.example.marvel.MainActivity.CHARACTER_ID;
import static com.example.marvel.R.*;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterDetailsActivity extends AppCompatActivity {


    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.descriptionTV)
    TextView descriptionTV;
    @BindView(R.id.comicsTV)
    TextView comicsTV;
    @BindView(R.id.eventsTV)
    TextView eventsTV;
    @BindView(R.id.seriesTV)
    TextView seriesTV;
    @BindView(R.id.storiesTV)
    TextView storiesTV;

    @BindView(R.id.storiesRV)
    RecyclerView storiesRV;
    @BindView(R.id.seriesRV)
    RecyclerView seriesRV;
    @BindView(R.id.eventsRV)
    RecyclerView eventsRV;
    @BindView(R.id.comicsRV)
    RecyclerView comicsRV;

    @BindView(R.id.comicsRL)
    RelativeLayout comicsRL;
    @BindView(R.id.eventsRL)
    RelativeLayout eventsRL;
    @BindView(R.id.seriesRL)
    RelativeLayout seriesRL;
    @BindView(R.id.storiesRL)
    RelativeLayout storiesRL;

    @BindView(R.id.comicsArrowIV)
    ImageView comicsArrowIV;
    @BindView(R.id.eventsArrowIV)
    ImageView eventsArrowIV;
    @BindView(R.id.seriesArrowIV)
    ImageView seriesArrowIV;
    @BindView(R.id.storiesArrowIV)
    ImageView storiesArrowIV;

    @BindView(R.id.shimmerLayout)
    ShimmerFrameLayout shimmerLayout;

    private boolean storiesLoaded, comicsLoaded, seriesLoaded, eventsLoaded;

    private long id;
    private Character character;
    Long ts;
    String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_character_details);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            id = getIntent().getLongExtra(CHARACTER_ID, 0L);
            character = getIntent().getParcelableExtra(CHARACTER);
        }
        if (character != null) {
            descriptionTV.setText(character.getDescription());
//                this.setSupportActionBar(this.toolbar);
            this.setTitle(character.getName());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                Drawable backIcon = ContextCompat.getDrawable(this, drawable.ic_arrow_back);
                if (backIcon != null)
                    backIcon.setColorFilter(ContextCompat.getColor(this, R.color.teal_700), PorterDuff.Mode.SRC_IN);
                getSupportActionBar().setHomeAsUpIndicator(backIcon);
            }
        }
        ts = new Date().getTime();
        hash = new HashCreator().createSHAHash(ts + PRIVATE_API_KEY + PUBLIC_API_KEY);

        showLoader();
        getCharacterComics();
        getCharacterSeries();
        getCharacterEvents();
        getCharacterStories();


    }

    @OnClick(R.id.comicsRL)
    void expandComicsView() {
        if (comicsRV.isShown()) {
            comicsArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_down_arrow, getTheme()));
            comicsRV.setVisibility(View.GONE);
        } else {
            comicsArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_up_arrow, getTheme()));
            comicsRV.setVisibility(View.VISIBLE);

        }
    }

    @OnClick(R.id.seriesRL)
    void expandSeriesView() {
        if (seriesRV.isShown()) {
            seriesArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_down_arrow, getTheme()));
            seriesRV.setVisibility(View.GONE);
        } else {
            seriesArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_up_arrow, getTheme()));
            seriesRV.setVisibility(View.VISIBLE);

        }
    }

    @OnClick(R.id.storiesRL)
    void expandStoriesView() {
        if (storiesRV.isShown()) {
            storiesArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_down_arrow, getTheme()));
            storiesRV.setVisibility(View.GONE);
        } else {
            storiesArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_up_arrow, getTheme()));
            storiesRV.setVisibility(View.VISIBLE);

        }

    }

    @OnClick(R.id.eventsRL)
    void expandEventsView() {
        if (eventsRV.isShown()) {
            eventsArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_down_arrow, getTheme()));
            eventsRV.setVisibility(View.GONE);
        } else {
            eventsArrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawable.ic_up_arrow, getTheme()));
            eventsRV.setVisibility(View.VISIBLE);

        }
    }

    private void getCharacterStories() {
        new ApiCall().getMarvelApi().getCharacterStories(id, "id", 3, PUBLIC_API_KEY, hash, String.valueOf(ts)).enqueue(new Callback<CharacterDetailsResponse>() {
            @Override
            public void onResponse(Call<CharacterDetailsResponse> call, Response<CharacterDetailsResponse> response) {
                if (response.isSuccessful()) {
                    storiesLoaded = true;
                    hideLoader();
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().getResults() != null && !response.body().getData().getResults().isEmpty()) {
                            DetailsAdapter detailsAdapter = new DetailsAdapter(response.body().getData().getResults(), CharacterDetailsActivity.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CharacterDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                            storiesRV.setLayoutManager(layoutManager);
                            storiesRV.setAdapter(detailsAdapter);
                        } else {
                            storiesRL.setVisibility(View.GONE);
                            storiesRV.setVisibility(View.GONE);
                        }
                    }
                } else {
                    storiesLoaded = true;
                    hideLoader();
                    JSONObject jsonObj;
                    try {
                        if (response.errorBody() != null) {
                            jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            Toast.makeText(CharacterDetailsActivity.this, jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(CharacterDetailsActivity.this, getString(R.string.errors_occurred), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CharacterDetailsResponse> call, Throwable t) {
                storiesLoaded = true;
                hideLoader();
                Toast.makeText(CharacterDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCharacterEvents() {
        new ApiCall().getMarvelApi().getCharacterEvents(id, "startDate", 3, PUBLIC_API_KEY, hash, String.valueOf(ts)).enqueue(new Callback<CharacterDetailsResponse>() {
            @Override
            public void onResponse(Call<CharacterDetailsResponse> call, Response<CharacterDetailsResponse> response) {
                if (response.isSuccessful()) {
                    eventsLoaded = true;
                    hideLoader();
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().getResults() != null && !response.body().getData().getResults().isEmpty()) {
                            DetailsAdapter detailsAdapter = new DetailsAdapter(response.body().getData().getResults(), CharacterDetailsActivity.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CharacterDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                            eventsRV.setLayoutManager(layoutManager);
                            eventsRV.setAdapter(detailsAdapter);
                        } else {
                            eventsRL.setVisibility(View.GONE);
                            eventsRV.setVisibility(View.GONE);
                        }
                    }
                } else {
                    eventsLoaded = true;
                    hideLoader();
                    JSONObject jsonObj;
                    try {
                        if (response.errorBody() != null) {
                            jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            Toast.makeText(CharacterDetailsActivity.this, jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(CharacterDetailsActivity.this, getString(R.string.errors_occurred), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CharacterDetailsResponse> call, Throwable t) {
                eventsLoaded = true;
                hideLoader();
                Toast.makeText(CharacterDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCharacterSeries() {
        new ApiCall().getMarvelApi().getCharacterSeries(id, "startYear", 3, PUBLIC_API_KEY, hash, String.valueOf(ts)).enqueue(new Callback<CharacterDetailsResponse>() {
            @Override
            public void onResponse(Call<CharacterDetailsResponse> call, Response<CharacterDetailsResponse> response) {
                if (response.isSuccessful()) {
                    seriesLoaded = true;
                    hideLoader();
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().getResults() != null && !response.body().getData().getResults().isEmpty()) {
                            DetailsAdapter detailsAdapter = new DetailsAdapter(response.body().getData().getResults(), CharacterDetailsActivity.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CharacterDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                            seriesRV.setLayoutManager(layoutManager);
                            seriesRV.setAdapter(detailsAdapter);
                        } else {
                            seriesRV.setVisibility(View.GONE);
                            seriesRL.setVisibility(View.GONE);
                        }
                    }
                } else {
                    seriesLoaded = true;
                    hideLoader();
                    JSONObject jsonObj;
                    try {
                        if (response.errorBody() != null) {
                            jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            Toast.makeText(CharacterDetailsActivity.this, jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(CharacterDetailsActivity.this, getString(R.string.errors_occurred), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CharacterDetailsResponse> call, Throwable t) {
                seriesLoaded = true;
                hideLoader();
                Toast.makeText(CharacterDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getCharacterComics() {
        new ApiCall().getMarvelApi().getCharacterComics(id, "onsaleDate", 3, PUBLIC_API_KEY, hash, String.valueOf(ts)).enqueue(new Callback<CharacterDetailsResponse>() {
            @Override
            public void onResponse(Call<CharacterDetailsResponse> call, Response<CharacterDetailsResponse> response) {
                if (response.isSuccessful()) {
                    comicsLoaded = true;
                    hideLoader();
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().getResults() != null && !response.body().getData().getResults().isEmpty()) {
                            DetailsAdapter detailsAdapter = new DetailsAdapter(response.body().getData().getResults(), CharacterDetailsActivity.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CharacterDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                            comicsRV.setLayoutManager(layoutManager);
                            comicsRV.setAdapter(detailsAdapter);
                        }
                    } else {
                        comicsRL.setVisibility(View.GONE);
                        comicsRV.setVisibility(View.GONE);
                    }
                } else {
                    comicsLoaded = true;
                    hideLoader();
                    JSONObject jsonObj;
                    try {
                        if (response.errorBody() != null) {
                            jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            Toast.makeText(CharacterDetailsActivity.this, jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(CharacterDetailsActivity.this, getString(R.string.errors_occurred), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CharacterDetailsResponse> call, Throwable t) {
                comicsLoaded = true;
                hideLoader();
                Toast.makeText(CharacterDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoader() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
    }
    private void hideLoader() {
        if(seriesLoaded && comicsLoaded && storiesLoaded && eventsLoaded) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

        }
    }
}
