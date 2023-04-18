import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petshopsensedia.CartProducts
import com.example.petshopsensedia.R
import java.text.DecimalFormat

class CartAdapter(private val productList: List<CartProducts>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productNameTextView: TextView = view.findViewById(R.id.productNameTextView)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
        val totalPriceTextView: TextView = view.findViewById(R.id.totalPriceTextView)
        val productImageView: ImageView = view.findViewById(R.id.productImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartProduct = productList[position]

        holder.productNameTextView.text = cartProduct.description
        holder.quantityTextView.text = "Qtd.: ${cartProduct.quantitySelected}"

        val amountValue: Double = cartProduct.amount.replace(",", ".").toDouble()
        val totalAmount = amountValue * cartProduct.quantitySelected

        holder.priceTextView.text = String.format("R$ %.2f", amountValue)
        holder.totalPriceTextView.text = String.format("Valor Total - R$ %.2f", totalAmount)

        cartProduct.totalAmount = String.format("R$ %.2f", totalAmount)

        Glide.with(holder.productImageView)
            .load(cartProduct.imageUrl)
            .into(holder.productImageView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
