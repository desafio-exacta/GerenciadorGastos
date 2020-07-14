package br.com.gerenciadorgastos.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import br.com.gerenciadorgastos.R
import br.com.gerenciadorgastos.api.GastoService
import br.com.gerenciadorgastos.api.RetrofitClient
import br.com.gerenciadorgastos.model.Gasto
import br.com.gerenciadorgastos.model.Pessoa
import br.com.gerenciadorgastos.model.Tag
import br.com.gerenciadorgastos.util.ViewUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_gasto_add.*
import okhttp3.ResponseBody
import org.jetbrains.anko.design.snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GastoAddFragment : Fragment() {

    private var gService: GastoService? = null
    private var gasto: Gasto? = null
    private var views: MutableList<View>? = null

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.action_edit)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save -> {
                if (!ViewUtil(views!!).verify()) {
                    layout_gasto_add.snackbar(getString(R.string.campos_obrigatorios))
                } else {
                    adicionarGasto()
                    ViewUtil(views!!).disable()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        gService = RetrofitClient().getClient(context!!).create(GastoService::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_gasto_add, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.adicionar)
        views = mutableListOf(add_descricao, add_valor, add_pessoa, add_tag)
    }

    fun adicionarGasto() {
        var g = Gasto(null
            , null
            , add_descricao.text.toString()
            , "${add_valor.text}".toDouble()
            , Pessoa(add_pessoa.text.toString())
            , Tag(add_tag.text.toString())
        )

        gService?.add(g)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                gasto = Gson().fromJson((response.body()?.string()), Gasto::class.java)
                fragmentManager?.popBackStack()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                layout_gasto_add.snackbar("erro: ${t.printStackTrace()}")
            }
        })
    }
}