package com.example.petshopsensedia.ui

import CartAdapter
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petshopsensedia.CartProduct
import com.example.petshopsensedia.CartProducts
import com.example.petshopsensedia.R
import com.example.petshopsensedia.ui.adapter.ProductAdapter
import com.example.petshopsensedia.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.cart_item.*
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.DecimalFormat

class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cartViewModel = (activity as MainActivity).cartViewModel
        cartAdapter = CartAdapter(cartViewModel.productList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = cartAdapter

        backButton.setOnClickListener { requireActivity().onBackPressed() }
        emailButton.setOnClickListener { sendToEmail() }
        whatsappButton.setOnClickListener { sendToWhatsapp() }
        clearButton.setOnClickListener { clearBasket() }
    }

    fun clearBasket() {
        val cartViewModel = (activity as MainActivity).cartViewModel
        cartViewModel.clearList()
        requireActivity().onBackPressed()
    }

    fun sendToEmail() {
        val cartViewModel = (activity as MainActivity).cartViewModel
        val cartProducts = cartViewModel.productList
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, "PetShopSensedia - Lista de Produtos")
            putExtra(Intent.EXTRA_TEXT, getCartProductsFormattedText(cartProducts))
        }
        startActivity(Intent.createChooser(emailIntent, "Enviar por email"))
    }

    fun sendToWhatsapp() {
        val cartViewModel = (activity as MainActivity).cartViewModel
        val cartProducts = cartViewModel.productList
        val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, getCartProductsFormattedText(cartProducts))
        }
        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "WhatsApp não encontrado", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCartProductsFormattedText(cartProducts: List<CartProducts>): String {
        val stringBuilder = StringBuilder()
        for (product in cartProducts) {
            stringBuilder.append("Produto: ${product.description}\n")
            stringBuilder.append("Quantidade: ${product.quantitySelected}\n")
            stringBuilder.append("Peso: ${product.weight}\n")
            stringBuilder.append("Preço Unitário: ${product.amount}\n")
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    companion object {

        @JvmStatic
        fun newInstance() = CartFragment()
    }
}