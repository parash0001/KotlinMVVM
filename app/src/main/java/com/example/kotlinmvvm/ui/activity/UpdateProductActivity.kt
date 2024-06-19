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
import com.example.kotlinmvvm.databinding.ActivityUpdateProductBinding
import com.example.kotlinmvvm.model.ProductModel
import com.example.kotlinmvvm.repository.ProductRepositoryImpl
import com.example.kotlinmvvm.utils.imageUtils
import com.example.kotlinmvvm.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class UpdateProductActivity : AppCompatActivity() {


    lateinit var updateProductBinding: ActivityUpdateProductBinding
    var id = ""
    var imageName = ""


    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var imageUri : Uri? = null

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

    fun uploadImage(){
        imageUri?.let {
            productViewModel.uploadImage(imageName, it){
                    success, imageUrl ->
                if (success){
                    updateProduct(imageUrl.toString())
                }
            }
        }
    }

    lateinit var imageUtils: imageUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        updateProductBinding = ActivityUpdateProductBinding.inflate(layoutInflater)

        setContentView(updateProductBinding.root)

        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)

        imageUtils = imageUtils(this)

        imageUtils.registerActivity {
            imageUri = it
            Picasso.get().load(it).into(updateProductBinding.imageUpdate)
        }

        var product : ProductModel? = intent.getParcelableExtra("product")
        id = product?.id.toString()
        imageName = product?.imageName.toString()

        updateProductBinding.ProductNameUpdate.setText(product?.name)
        updateProductBinding.ProductDescUpdate.setText(product?.description)
        updateProductBinding.ProductPriceUpdate.setText(product?.price.toString())

        Picasso.get().load(product?.url).into(updateProductBinding.imageUpdate)

        updateProductBinding.btnUpdate.setOnClickListener {

            uploadImage()
        }

        updateProductBinding.imageUpdate.setOnClickListener {
            imageUtils.launchGallery(this@UpdateProductActivity)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun updateProduct(url: String){
        var updatedName : String = updateProductBinding.ProductNameUpdate.text.toString()
        var updatedDesc : String = updateProductBinding.ProductDescUpdate.text.toString()
        var updatedPrice : Int = updateProductBinding.ProductPriceUpdate.text.toString().toInt()

        var data = mutableMapOf<String,Any>()
        data["name"] = updatedName
        data["description"] = updatedDesc
        data["price"] = updatedPrice
        data["url"] = url

        productViewModel.updateProduct(id,data){
                success, message ->
            if (success){
                Toast.makeText(applicationContext,message,
                    Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }

}
