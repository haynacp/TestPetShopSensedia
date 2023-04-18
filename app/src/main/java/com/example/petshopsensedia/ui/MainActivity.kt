package com.example.petshopsensedia.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petshopsensedia.*
import com.example.petshopsensedia.data.ApiClient
import com.example.petshopsensedia.ui.adapter.ProductAdapter
import com.example.petshopsensedia.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import java.lang.String
import kotlin.Boolean
import kotlin.Exception
import kotlin.toString


class MainActivity : AppCompatActivity() {

    var textCartItemCount: TextView? = null
    var mCartItemCount = 0
    val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.IO)  {
            try {
                val call: Call<Product> = ApiClient.petShopApi.getProducts()
                val response: Response<Product> = call.execute()

                if (response.isSuccessful) {
                    val productsResponse: Product? = response.body()

                    withContext(Dispatchers.Main) {
                        recycler_view.visibility = View.VISIBLE
                        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                        val productListAdapter = ProductAdapter(this@MainActivity,productsResponse?.productList ?: emptyList())
                        recyclerView.adapter = productListAdapter
                    }
                } else {
                    Log.d("TAG", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("TAG", e.message.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu.findItem(R.id.action_cart)

        val actionView: View = menuItem.actionView
        textCartItemCount = actionView.findViewById(R.id.cart_badge) as TextView

        setupBadge()

        actionView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onOptionsItemSelected(menuItem)
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {

                if (mCartItemCount > 0) {
                    val cartFragment = CartFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, cartFragment)
                        .addToBackStack(null)
                        .commit()
                    recycler_view.visibility = View.GONE
                    fragment_container.visibility = View.VISIBLE
                }
                else {
                    Toast.makeText(this@MainActivity, "Seu carrinho está vazio. Adicione itens para prosseguir.", Toast.LENGTH_LONG).show()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount!!.visibility != View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text =
                    String.valueOf(Math.min(mCartItemCount, 99))
                if (textCartItemCount!!.visibility != View.VISIBLE) {
                    textCartItemCount!!.visibility = View.VISIBLE
                }
            }
        }
    }

    fun addProductToCart(product: Products) {
        cartViewModel.productList.find { it.id == product.id }?.let {
            it.quantitySelected += product.quantitySelected
        } ?: cartViewModel.addProductToCart(product)

        mCartItemCount = cartViewModel.productList.size
        setupBadge()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        recycler_view.visibility = View.VISIBLE
        recycler_view.scrollToPosition(0)
        fragment_container.visibility = View.GONE
        mCartItemCount = cartViewModel.productList.size
        setupBadge()
    }
}