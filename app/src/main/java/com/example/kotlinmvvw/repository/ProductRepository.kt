package com.example.kotlinmvvw.repository


import android.net.Uri
import com.example.kotlinmvvw.model.ProductModel

interface ProductRepository {

    fun uploadImage(imageName: String,imageUrl : Uri,callback: (Boolean,String?) -> Unit)

    fun addProduct(productModel: ProductModel, callback: (Boolean,String?) -> Unit)

    fun getAllProduct(callback: (List<ProductModel>?, Boolean?, String?) -> Unit)

    fun updateProduct(id:String,data:   MutableMap<String,Any>?,callback: (Boolean, String?) -> Unit)

    fun deleteData(id:String,callback: (Boolean, String?) -> Unit)

    //Boolean for success either true of false
    //String for Message "Your data has been deleted"

    fun deleteImage(imageName: String,callback: (Boolean, String?) -> Unit)


}