package br.com.gerenciadorgastos.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import br.com.gerenciadorgastos.R
import br.com.gerenciadorgastos.model.Gasto
import kotlinx.android.synthetic.main.item_gasto_list.view.*

class GastoAdapter(private val gastos: MutableList<Gasto>
                   , val listener: (Int) -> Unit) : RecyclerView.Adapter<GastoVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GastoVH(LayoutInflater.from(parent.context), parent)
    override fun onBindViewHolder(holder: GastoVH, position: Int) = holder.bind(gastos.elementAt(position), position, listener)
    override fun getItemCount(): Int = gastos.size
    fun remove(g: Gasto) {
        var position = gastos.indexOf(g)
        if (position > -1) {
            gastos.remove(g)
            notifyItemRemoved(position)
        }
    }
}

class GastoVH(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_gasto_list, parent, false)) {
    private var tvPreco: TextView? = null
    private var tvDescricao: TextView? = null
    private var tvData: TextView? = null
    init {
        tvPreco = itemView.gasto_valor
        tvDescricao = itemView.gasto_descricao
        tvData = itemView.gasto_data
    }

    fun bind(gasto: Gasto, pos: Int, listener: (Int) -> Unit) = with(itemView) {
        tvPreco?.text = "Valor: R$ ${"%.2f".format(gasto.valor)}"
        tvDescricao?.text = "Descrição: ${gasto.descricao}"
        tvData?.text = "Data: ${gasto.data}"
        layout_item_gasto_list.setOnClickListener {
            listener(pos)
        }
    }
}