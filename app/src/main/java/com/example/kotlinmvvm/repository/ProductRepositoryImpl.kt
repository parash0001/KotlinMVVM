package com.example.kotlinmvvm.repository

import android.net.Uri
import android.util.Log
import com.example.kotlinmvvm.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductRepositoryImpl :  ProductRepository {

    var firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("products")

    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = firebaseStorage.reference.child("products")

    override fun uploadImage(imageName: String,imageUrl: Uri, callback: (Boolean,String?) -> Unit) {

        //ram
        var imageReference = storageRef.child(imageName)

        imageUrl?.let {url->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { downloadUrl->
                    var imagesUrl = downloadUrl.toString()
                    callback(true,imagesUrl)

                }
            }.addOnFailureListener {
                callback(false,"")
            }
        }
    }

    override fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit) {

        var id = ref.push().key.toString()

        productModel.id = ref.push().key.toString()

        ref.child(id).setValue(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true,"Data uploaded successfully")
            } else {
                callback(false,"Unable to upload data")
            }
        }
    }

    override fun getAllProduct(callback: (List<ProductModel>?, Boolean?, String?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var productList = mutableListOf<ProductModel>()
                Log.d("snapshot data",snapshot.children.toString())
                for(eachData in snapshot.children) {
                    var product = eachData.getValue(ProductModel::class.java)
                    if(product!=null){
                        Log.d("data from firebase",product.name)
                        Log.d("data from firebase",product.description)
                        Log.d("data from firebase",product.price.toString())
                        Log.d("data from firebase",product.id)

                        productList.add(product)
                    }
                }
                callback(productList,true,"Data successfully retrieved")

            }

            override fun onCancelled(error: DatabaseError) {

                callback(null,false,"Unable to fetch ${error.message}")

            }
        })
    }

    override fun updateProduct(id: String,data:MutableMap<String,Any>?, callback: (Boolean, String?) -> Unit) {

        data?.let {
            ref.child(id).updateChildren(it).addOnCompleteListener {
                if (it.isSuccessful) {
                    if(it.isSuccessful){
                        callback(true,"Your data has been updated")
                    }
                } else {
                    callback(false,"Unable to update data")
                }
            }
        }
    }

    override fun deleteData(id: String, callback: (Boolean, String?) -> Unit) {
        ref.child(id).removeValue().addOnCompleteListener{
            if (it.isSuccessful) {
                callback(true,"Data has been deleted")
            } else {
                callback(false,"Unable to delete data")
            }
        }
    }

    override fun deleteImage(imageName: String, callback: (Boolean, String?) -> Unit) {
        storageRef.child("products").child(imageName).delete()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Image deleted")
                }else{
                    callback(false,"Unable to delete image")
                }
            }
    }
}