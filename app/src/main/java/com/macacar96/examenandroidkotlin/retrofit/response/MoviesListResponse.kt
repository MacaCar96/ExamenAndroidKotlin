package com.macacar96.examenandroidkotlin.retrofit.response

import com.google.gson.annotations.SerializedName

data class MoviesListResponse(
    @SerializedName("page") var page: Int,
    @SerializedName("results") var results: List<MovieResponse>,
    @SerializedName("total_pages") var total_pages: Int,
    @SerializedName("total_results") var total_results: Int,
)