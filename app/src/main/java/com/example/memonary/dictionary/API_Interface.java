package com.example.memonary.dictionary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_Interface {
    @GET("en_US/{word}")
    Call<List<WordModel>> getResults(@Path("word") String word);
}
