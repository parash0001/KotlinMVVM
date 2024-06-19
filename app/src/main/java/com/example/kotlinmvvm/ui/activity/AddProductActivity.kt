package com.example.kotlinmvvm.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.databinding.ActivityAddProductBinding
import com.example.kotlinmvvm.model.ProductModel
import com.example.kotlinmvvm.repository.ProductRepositoryImpl
import com.example.kotlinmvvm.utils.LoadingUtils
import com.example.kotlinmvvm.utils.imageUtils
import com.example.kotlinmvvm.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import java.util.UUID

class AddProductActivity : AppCompatActivity() {

    lateinit var loadingUtils: LoadingUtils

    lateinit var addProductBinding: ActivityAddProductBinding

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var imageUri : Uri? = null

    lateinit var imageUtils: imageUtils
    lateinit var productViewModel: ProductViewModel


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(addProductBinding.root)

        loadingUtils = LoadingUtils(this)

        imageUtils = imageUtils(this)
        imageUtils.registerActivity { url->
            url.let {
                imageUri = url
                Picasso.get().load(url).into(addProductBinding.imageBrowse)
            }
        }

        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)


        addProductBinding.imageBrowse.setOnClickListener{
            imageUtils.launchGallery(this)
        }

        addProductBinding.btnSave.setOnClickListener {
            uploadImage()

        }

        addProductBinding.btnSave.setOnClickListener {
            if(imageUri !=null){
                uploadImage()
            }else{
                Toast.makeText(applicationContext,"Please upload image first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun uploadImage(){
        loadingUtils.showLoading()
        val imageName = UUID.randomUUID().toString()
        imageUri?.let {
            productViewModel.uploadImage(imageName,it){
                    success, imageUrl ->
                if(success){
                    addProduct(imageUrl.toString(),imageName.toString())
                }
            }
        }
    }


    fun addProduct(url: String,imageName: String) {
        var name: String = addProductBinding.editProductName.text.toString()
        var desc: String = addProductBinding.editProductDesc.text.toString()
        var price: Int = addProductBinding.editProductPrice.text.toString().toInt()

        var data = ProductModel("",name,desc,price,url,imageName)
        productViewModel.addProduct(data){
                success,message ->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,
                    Toast.LENGTH_LONG).show()
                finish()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,
                    Toast.LENGTH_LONG).show()
            }
        }

    }



}