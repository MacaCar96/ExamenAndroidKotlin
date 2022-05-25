package com.macacar96.examenandroidkotlin.retrofit

import com.macacar96.examenandroidkotlin.retrofit.response.MoviesListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("3/movie/popular")
    suspend fun getPeliculasPopulares(
        @Query("api_key") api_key:String,
        @Query("language") language:String,
        @Query("page") page:String
    ): Response<MoviesListResponse>
}