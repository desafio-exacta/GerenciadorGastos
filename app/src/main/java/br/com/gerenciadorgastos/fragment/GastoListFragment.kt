package br.com.gerenciadorgastos.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gerenciadorgastos.R
import br.com.gerenciadorgastos.adapter.GastoAdapter
import br.com.gerenciadorgastos.api.GastoService
import br.com.gerenciadorgastos.api.RetrofitClient
import br.com.gerenciadorgastos.model.Gasto
import br.com.gerenciadorgastos.util.ConfigSingleton.Modos.GASTO_ADD_MODE
import br.com.gerenciadorgastos.util.ConfigSingleton.Modos.GASTO_DETAIL_MODE
import br.com.gerenciadorgastos.util.SwipeToDeleteCallback
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_gasto_list.*
import okhttp3.ResponseBody
import org.jetbrains.anko.design.snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GastoListFragment : Fragment() {
    private var gService: GastoService? = null
    private var gastos: MutableList<Gasto> = mutableListOf()
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        gService = RetrofitClient().getClient(context!!).create(GastoService::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_gasto_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title)
        buscarGastos()

        swipe_refresh_layout.setOnRefreshListener {
            gastos.clear()
            fab_add.hide()
            buscarGastos()
        }
        fab_add.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.container, GastoAddFragment(), GASTO_ADD_MODE)?.addToBackStack(null)?.commit()
        }
    }

    fun buscarGastos(){
        gService?.getAll()?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                pb_gasto.visibility = View.GONE
                layout_gasto.snackbar("erro: ${t.printStackTrace()}")
                swipe_refresh_layout.isRefreshing = false
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                gastos = Gson().fromJson((response.body()?.string()), Array<Gasto>::class.java).toMutableList()
                bind()
            }
        })
    }

    fun deleteGasto(id: Long) {
        gService?.delete(id)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) { }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                layout_gasto.snackbar("erro: ${t.printStackTrace()}")
            }
        })
    }

    fun bind() {
        fab_add.show()
        pb_gasto.visibility = View.GONE
        swipe_refresh_layout.isRefreshing = false

        if(gastos.isEmpty()) {
            tv_gasto_vazio.visibility = View.VISIBLE
        } else {
            tv_gasto_vazio.visibility = View.GONE
            linearLayoutManager = LinearLayoutManager(context)
            rv_gastos.apply {
                layoutManager = linearLayoutManager
                adapter = GastoAdapter(gastos) {
                    val fragment = GastoDetalheFragment()
                    val bundle = Bundle()
                    bundle.putString("gasto", Gson().toJson(gastos.elementAt(it)))
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.container, fragment, GASTO_DETAIL_MODE)?.addToBackStack(null)?.commit()
                }
            }
            val swipeHandler = object : SwipeToDeleteCallback(context!!) {
                override fun onSwiped(holder: RecyclerView.ViewHolder, position: Int) {
                    val adapter = rv_gastos.adapter as GastoAdapter
                    val g = gastos.get(holder.adapterPosition)
                    deleteGasto(g.id!!)
                    adapter.remove(g)
                    if(adapter.itemCount == 0)
                        tv_gasto_vazio.visibility = View.VISIBLE
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rv_gastos)
        }
    }
}