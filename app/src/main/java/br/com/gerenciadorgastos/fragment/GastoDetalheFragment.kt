package br.com.gerenciadorgastos.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gerenciadorgastos.R
import br.com.gerenciadorgastos.api.GastoService
import br.com.gerenciadorgastos.api.RetrofitClient
import br.com.gerenciadorgastos.model.Gasto
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_gasto_detalhe.*
import okhttp3.ResponseBody
import org.jetbrains.anko.design.snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GastoDetalheFragment : Fragment() {

    private var gService: GastoService? = null
    private var gasto: Gasto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gService = RetrofitClient().getClient(context!!).create(GastoService::class.java)
        gasto = Gson().fromJson(arguments!!.getString("gasto"), Gasto::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_gasto_detalhe, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.detalhe)
        buscarGasto(gasto?.id!!)
    }

    fun buscarGasto(id: Long){
        gService?.getById(id)?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                layout_gasto_detalhe.snackbar("erro: ${t.printStackTrace()}")
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                gasto = Gson().fromJson((response.body()?.string()), Gasto::class.java)
                bind()
            }
        })
    }

    fun bind() {
        pb_gasto.visibility = View.INVISIBLE
        pb_pessoa.visibility = View.INVISIBLE
        pb_tag.visibility = View.INVISIBLE
        detalhe_descricao.text = gasto?.descricao
        detalhe_data.text = gasto?.data
        detalhe_valor.text = "R$ ${gasto?.valor.toString().replace(".",",0")}"
        detalhe_pessoa.text = gasto?.pessoa?.nome
        detalhe_tag.text = gasto?.tag?.nome
    }
}