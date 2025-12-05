package com.school.projettm

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import com.school.projettm.dtos.CreateArticleCommand
import com.school.projettm.repositories.IArticleRepository
import com.school.projettm.utils.FileManager
import com.school.projettm.utils.RetrofitFactory
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest.permission.CAMERA

class SellArticleActivity : AppCompatActivity() {
    private lateinit var selectedImage : File
    private lateinit var photoFile: File

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = FileManager.getFileFromUri(this, uri) // Convert URI to File
            selectedImage = file
            Toast.makeText(this, "Image Selected: ${file.name}", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            selectedImage = photoFile
            Toast.makeText(this, "Photo captured: ${photoFile.name}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to capture photo", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera() // Permission déjà accordée, on peut ouvrir la caméra
            }
            else -> {
                cameraPermissionRequest.launch(CAMERA) // Demander la permission
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take a Photo", "Choose from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermission()
                    1 -> openImagePicker()
                }
            }
            .show()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_article)
        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)
        val stateSpinner = findViewById<Spinner>(R.id.spinner_state)

        ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        // Configure State Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            stateSpinner.adapter = adapter
        }

        findViewById<Button>(R.id.btn_submit_article).setOnClickListener {
            submitArticle(categorySpinner.selectedItem.toString(), stateSpinner.selectedItem.toString())
        }

        findViewById<Button>(R.id.btn_choose_image).setOnClickListener {
            showImageSourceDialog()
        }


    }

    private fun openCamera() {
        // Créez un fichier temporaire
        photoFile = createImageFile()
        val photoUri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)
        cameraLauncher.launch(photoUri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_${timestamp}_", ".jpg", storageDir)
    }

    private fun openImagePicker() {
        // Use the launcher to open the image picker
        imagePickerLauncher.launch("image/*")
    }

    private fun submitArticle(selectedCategory: String, selectedState: String) {
        val title = findViewById<EditText>(R.id.et_title).text.toString()
        val description = findViewById<EditText>(R.id.et_description).text.toString()
        val price = findViewById<EditText>(R.id.et_price).text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO
        val quantity = findViewById<EditText>(R.id.et_quantity).text.toString().toInt()


        if (title.isBlank() || description.isBlank() || !::selectedImage.isInitialized) {
            Toast.makeText(this, "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = getUserIdFromToken()?.toInt() ?: run {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Create article command
        val command = CreateArticleCommand(
            title = title,
            description = description,
            price = price,
            category = selectedCategory,
            state = selectedState,
            userId = userId,
            createdAt = Date().toSQLDateString(),
            updatedAt = Date().toSQLDateString(),
            status = "available",
            quantity = quantity,
            image = selectedImage
        )


        // Send to server
        sendArticleToServer(command)
        Toast.makeText(this, "Article successfully uploaded", Toast.LENGTH_SHORT).show()

    }

    fun Date.toSQLDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(this)
    }


    private fun sendArticleToServer(command: CreateArticleCommand) {
        val repository = RetrofitFactory.instance.create(IArticleRepository::class.java)

        val titlePart = command.title.toRequestBody(MultipartBody.FORM)
        val descriptionPart = command.description.toRequestBody(MultipartBody.FORM)
        val pricePart = command.price.toString().toRequestBody(MultipartBody.FORM)
        val categoryPart = command.category.toRequestBody(MultipartBody.FORM)
        val statePart = command.state.toRequestBody(MultipartBody.FORM)
        val userIdPart = command.userId.toString().toRequestBody(MultipartBody.FORM)
        val createdAtPart = command.createdAt.toRequestBody(MultipartBody.FORM)
        val updatedAtPart = command.updatedAt.toRequestBody(MultipartBody.FORM)
        val statusPart = command.status.toRequestBody(MultipartBody.FORM)
        val quantityPart = command.quantity.toString().toRequestBody(MultipartBody.FORM)

        // convert the image file in MultipartBody.Part
        val imageRequestBody = command.image.asRequestBody(MultipartBody.FORM)
        val imagePart =
            MultipartBody.Part.createFormData("Image", command.image.name, imageRequestBody)

        // Launch the request
        lifecycleScope.launch {
            try {
                val response = repository.createArticle(
                    titlePart,
                    descriptionPart,
                    pricePart,
                    categoryPart,
                    statePart,
                    userIdPart,
                    createdAtPart,
                    updatedAtPart,
                    statusPart,
                    quantityPart,
                    imagePart
                )
                Log.d("CreateArticle", "Article created: ${response.title}")
            } catch (e: Exception) {
                Log.e("CreateArticle", "Error: ${e.message}")
            }
        }
    }

    private fun getUserIdFromToken(): String? {
        val jwtSharedPrefs = getSharedPreferences("JWT", MODE_PRIVATE)
        val token = jwtSharedPrefs.getString("value", null)

        return if (token != null) {
            val jwt = JWT(token)
            jwt.getClaim("userId").asString()
        } else {
            null
        }
    }



}