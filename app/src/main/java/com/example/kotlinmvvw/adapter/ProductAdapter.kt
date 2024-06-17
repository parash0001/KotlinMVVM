package com.example.kotlinmvvw.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvw.R
import com.example.kotlinmvvw.model.ProductModel
import com.example.kotlinmvvw.ui.activity.UpdateProductActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ProductAdapter(var context: android.content.Context, var data:ArrayList<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view:View) : RecyclerView.ViewHolder(view){

        var productName: TextView = view.findViewById(R.id.labelName)
        var productDesc : TextView = view.findViewById(R.id.labelDescription)
        var productPrice : TextView = view.findViewById(R.id.labelPrice)

        var btnSave : TextView = view.findViewById(R.id.btnSave)

        var progressBar: ProgressBar =view.findViewById(R.id.progressBar)
        var imageView: ImageView =view.findViewById(R.id.imageView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var view = LayoutInflater.from(parent.context).
        inflate(R.layout.sample_product,parent,
            false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.productName.text=data[position].name
        holder.productDesc.text=data[position].description
        holder.productPrice.text=data[position].price.toString()

        var image=data[position].url

        Picasso.get().load(image).into(holder.imageView,object :Callback{
            override fun onSuccess() {
                holder.progressBar.visibility=View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context,e?.localizedMessage,Toast.LENGTH_LONG).show()
            }
        })

        holder.btnSave.setOnClickListener {
            var intent = Intent(context, UpdateProductActivity::class.java)
            intent.putExtra("product",data[position])
            context.startActivity(intent)
        }


    }


    fun getProductID(position: Int) : String{
        return data[position].id

    }

    fun getImageName(position: Int) : String{
        return data[position].imageName
    }

    fun updateData(products : List<ProductModel>){
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()
    }
}