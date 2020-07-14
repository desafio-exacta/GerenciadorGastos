package br.com.gerenciadorgastos.model

import com.google.gson.annotations.SerializedName

data class Gasto(
    @SerializedName("id") var id: Long?,
    @SerializedName("data") var data: String?,
    @SerializedName("descricao") var descricao: String,
    @SerializedName("valor") var valor: Double,
    @SerializedName("pessoa") var pessoa: Pessoa,
    @SerializedName("tag") var tag: Tag
)

data class Pessoa (
    @SerializedName("nome") var nome: String
)

data class Tag (
    @SerializedName("nome") var nome: String
)