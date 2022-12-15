package com.example.marvel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelAPI {

    @GET("/v1/public/characters")
    Call<CharactersList> getCharacters(@Query("limit") int limit,
                                       @Query("offset") int offset,
                                       @Query("apikey") String apikey,
                                       @Query("hash") String hash,
                                       @Query("ts") String ts);

    @GET("v1/public/characters/{characterID}/comics")
    Call<CharacterDetailsResponse> getCharacterComics(@Path("characterID") long characterID,
                                                  @Query("orderBy") String orderBy,
                                                  @Query("limit") int limit,
                                                  @Query("apikey") String apikey,
                                                  @Query("hash") String hash,
                                                  @Query("ts") String ts);

    @GET("v1/public/characters/{characterID}/events")
    Call<CharacterDetailsResponse> getCharacterEvents(@Path("characterID") long characterID,
                                                  @Query("orderBy") String orderBy,
                                                  @Query("limit") int limit,
                                                  @Query("apikey") String apikey,
                                                  @Query("hash") String hash,
                                                  @Query("ts") String ts);

    @GET("v1/public/characters/{characterID}/series")
    Call<CharacterDetailsResponse> getCharacterSeries(@Path("characterID") long characterID,
                                                  @Query("orderBy") String orderBy,
                                                  @Query("limit") int limit,
                                                  @Query("apikey") String apikey,
                                                  @Query("hash") String hash,
                                                  @Query("ts") String ts);

    @GET("v1/public/characters/{characterID}/stories")
    Call<CharacterDetailsResponse> getCharacterStories(@Path("characterID") long characterID,
                                                   @Query("orderBy") String orderBy,
                                                   @Query("limit") int limit,
                                                   @Query("apikey") String apikey,
                                                   @Query("hash") String hash,
                                                   @Query("ts") String ts);
}
