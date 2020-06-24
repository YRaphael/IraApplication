package com.example.iraapplication;

import com.example.iraapplication.pojo.ValCurs;
import com.example.iraapplication.pojo.Valuta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CBRAPI {
    @GET("XML_val.asp")
    Call<Valuta> loadValuta(@Query("d") String d);

    @GET("XML_dynamic.asp")
    Call<ValCurs> loadValCurs(@Query("date_req1") String date1, @Query("date_req2") String date2, @Query("VAL_NM_RQ") String id);
}
