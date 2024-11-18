package com.example.food_ordering_app

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private val context: Context) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(), Filterable {

    private var foodList: List<Food> = ArrayList()
    private var filteredFoodList: List<Food> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = filteredFoodList[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int {
        return filteredFoodList.size
    }

    fun submitList(list: List<Food>) {
        foodList = list
        filteredFoodList = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence?.toString()?.lowercase() ?: ""

                val filteredList = if (query.isEmpty()) {
                    foodList
                } else {
                    foodList.filter {
                        it.name.lowercase().contains(query)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filteredFoodList = filterResults?.values as List<Food>
                notifyDataSetChanged()
            }
        }
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewFood: ImageView = itemView.findViewById(R.id.imageViewFood)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val food = filteredFoodList[position]
                    openFoodDetailPage(food)
                }
            }
        }

        fun bind(food: Food) {
            textViewName.text = food.name
            textViewPrice.text = "$${food.price}"

            // Hiển thị hình ảnh của món ăn (nếu có)
            food.image?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                imageViewFood.setImageBitmap(bitmap)
            } ?: imageViewFood.setImageResource(R.drawable.background)
        }
    }

    private fun openFoodDetailPage(food: Food) {
        val intent = Intent(context, FoodDetailActivity::class.java)
        intent.putExtra("food_id", food.id)
        context.startActivity(intent)
    }
}
