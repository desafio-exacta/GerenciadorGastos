package br.com.gerenciadorgastos.api

import br.com.gerenciadorgastos.model.Gasto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface GastoService {
    @GET("/api/gastos")
    fun getAll() : Call<ResponseBody>

    @GET("/api/gastos/{id}")
    fun getById(@Path("id") id: Long) : Call<ResponseBody>

    @POST("/api/gastos")
    fun add(@Body gasto: Gasto): Call<ResponseBody>

    @PUT("/api/gastos/{id}")
    fun update(@Path("id") id: Long
                     , @Body gasto: Gasto) : Call<ResponseBody>

    @DELETE("/api/gastos/{id}")
    fun delete(@Path("id") id: Long) : Call<ResponseBody>
}