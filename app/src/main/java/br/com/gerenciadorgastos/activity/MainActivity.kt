package br.com.gerenciadorgastos.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.com.gerenciadorgastos.R
import br.com.gerenciadorgastos.fragment.GastoListFragment
import br.com.gerenciadorgastos.util.ConfigSingleton.Modos.GASTO_MODE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_toolbar.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.title)
        supportFragmentManager.beginTransaction().replace(R.id.container, GastoListFragment(), GASTO_MODE).commit()
    }
}
