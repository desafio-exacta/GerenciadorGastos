package br.com.gerenciadorgastos.util

import android.view.View
import android.widget.EditText

class ViewUtil(var mutableList: MutableList<View>) {
    fun enable() = mutableList.forEach {it.isEnabled = true}
    fun disable() = mutableList.forEach {it.isEnabled = false}
    fun gone() = mutableList.forEach {it.visibility = View.GONE}
    fun visible() = mutableList.forEach {it.visibility = View.VISIBLE}
    fun verify(): Boolean {
        mutableList.forEach {
            if(it is EditText && it.text.toString().length == 0) {
                it.setError("Obrigatório!")
                return false
            }
        }
        return true
    }
}