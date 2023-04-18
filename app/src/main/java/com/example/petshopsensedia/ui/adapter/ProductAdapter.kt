package com.example.petshopsensedia.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petshopsensedia.Products
import com.example.petshopsensedia.R
import com.example.petshopsensedia.ui.MainActivity
import java.text.NumberFormat
import java.util.*

class ProductAdapter(private val activity: MainActivity, val items: List<Products>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImageView)
        val productDescription: TextView = itemView.findViewById(R.id.productNameTextView)
        val productQuantity: TextView = itemView.findViewById(R.id.quantityTextView)
        val productPrice: TextView = itemView.findViewById(R.id.priceTextView)
        val productAddToCart:Button = itemView.findViewById(R.id.addToCartButton)
        val incrementButton:Button = itemView.findViewById(R.id.plus_button)
        val decrementButton:Button = itemView.findViewById(R.id.minus_button)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.number_picker)
        var quantity:Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.img_no_image)
            .into(holder.productImage)

        holder.productDescription.text = item.description + " - " + item.weight

        // Formata o valor de amount para a moeda brasileira
        val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val amountValue: Double = item.amount.replace(",", ".").toDouble()
        holder.productPrice.text = formatter.format(amountValue)


        holder.productQuantity.text = "Qtd.: ${item.quantity}"

        holder.productQuantityTextView.text = holder.quantity.toString()

        holder.incrementButton.setOnClickListener {
            if (holder.quantity < 99) {
                holder.quantity++
                holder.productQuantityTextView.text = holder.quantity.toString()

            }
        }

        holder.decrementButton.setOnClickListener {
            if (holder.quantity > 1) {
                holder.quantity--
                holder.productQuantityTextView.text = holder.quantity.toString()
            }
        }

        holder.productAddToCart.setOnClickListener {
            var product = items[position]
            product.quantitySelected = holder.quantity
            Toast.makeText(activity.applicationContext, "Produto adicionado.", Toast.LENGTH_LONG).show()
            activity.apply {
                addProductToCart(product)
            }
        }
    }

    override fun getItemCount() = items.size
}