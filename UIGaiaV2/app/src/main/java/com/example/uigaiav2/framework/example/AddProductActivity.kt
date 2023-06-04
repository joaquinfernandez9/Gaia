//package com.example.uigaiav2.framework.example
//
//package com.example.uigaiav2.framework.example;
//
//package com.example.sellit.ui.product.add
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.view.MenuItem
//import android.widget.ArrayAdapter
//import android.widget.AutoCompleteTextView
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.repeatOnLifecycle
//import coil.load
//import com.bumptech.glide.Glide
//import com.example.sellit.R
//import com.example.sellit.databinding.ActivityAddProductBinding
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.launch
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//import java.util.UUID
//
//
//@AndroidEntryPoint
//class AddProductActivity : AppCompatActivity() {
//    lateinit var binding: ActivityAddProductBinding
//
//    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
//
//    private lateinit var storageReference: StorageReference
//
//    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
//
//    private lateinit var photoUri: Uri
//
//    private val viewModel: AddProductViewModel by viewModels()
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAddProductBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.appBarMain.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        scope()
//
//        storageReference = FirebaseStorage.getInstance().reference
//
//        takePictureLauncher =
//            registerForActivityResult(ActivityResultContracts.TakePicture()) { isPictureTaken ->
//                if (isPictureTaken) {
//                    uploadImageToFirebase(photoUri)
//                } else {
//                    Toast.makeText(this, "Error al capturar la foto", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        requestPermissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
//                if (granted) {
//                    startCamera()
//                } else {
//                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        binding.foto.setOnClickListener {
//            if (viewModel.uiState.value.images.size < 5) {
//                requestPermission()
//            }
//        }
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            finish()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun scope() {
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.uiState.collect { value ->
//                    if (!value.isLoading) {
//                        if (value.error != null) {
//                            Toast.makeText(this@AddProductActivity, value.error, Toast.LENGTH_SHORT)
//                                .show()
//                            viewModel.handleEvent(AddProductEvent.LimpiarError)
//                        }
//                        if (value.images.isNotEmpty()) {
//                            val list: List<String> = value.images
//                            binding.imageView.setImageResource(R.drawable.baseline_camera_alt_24)
//                            binding.imageView1.setImageResource(R.drawable.baseline_camera_alt_24)
//                            binding.imageView2.setImageResource(R.drawable.baseline_camera_alt_24)
//                            binding.imageView3.setImageResource(R.drawable.baseline_camera_alt_24)
//
//                            when (list.size) {
//                                1 -> {
//                                    binding.imageView.setTag(R.id.imageView, list[0])
//                                    load(list[0], binding.imageView)
//                                }
//
//                                2 -> {
//                                    binding.imageView.setTag(R.id.imageView, list[0])
//                                    load(list[0], binding.imageView)
//                                    binding.imageView.setTag(R.id.imageView1, list[1])
//                                    load(list[1], binding.imageView1)
//                                }
//
//                                3 -> {
//                                    binding.imageView.setTag(R.id.imageView, list[0])
//                                    load(list[0], binding.imageView)
//                                    binding.imageView.setTag(R.id.imageView1, list[1])
//                                    load(list[1], binding.imageView1)
//                                    binding.imageView.setTag(R.id.imageView2, list[2])
//                                    load(list[2], binding.imageView2)
//                                }
//
//                                4 -> {
//                                    binding.imageView.setTag(R.id.imageView, list[0])
//                                    load(list[0], binding.imageView)
//                                    binding.imageView.setTag(R.id.imageView1, list[1])
//                                    load(list[1], binding.imageView1)
//                                    binding.imageView.setTag(R.id.imageView2, list[2])
//                                    load(list[2], binding.imageView2)
//                                    binding.imageView.setTag(R.id.imageView3, list[3])
//                                    load(list[3], binding.imageView3)
//                                }
//                            }
//                        }
//                        if (value.categories.isNotEmpty()) {
//                            val items = value.categories
//                            val adapter = ArrayAdapter(
//                                this@AddProductActivity,
//                                R.layout.list_item,
//                                items.map { it.name })
//                            (binding.categoryTextInput.editText as? AutoCompleteTextView)?.setAdapter(
//                                adapter
//                            )
//                        }
//                        if (value.status.isNotEmpty()) {
//                            val items = value.status
//                            val adapter = ArrayAdapter(
//                                this@AddProductActivity,
//                                R.layout.list_item,
//                                items.map { it.name })
//                            (binding.statusTextInput.editText as? AutoCompleteTextView)?.setAdapter(
//                                adapter
//                            )
//                        }
//
//                        if (value.success) {
//                            Toast.makeText(
//                                this@AddProductActivity,
//                                "Producto agregado",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            finish()
//                        }
//                    } else {
//                        Toast.makeText(this@AddProductActivity, "Cargando...", Toast.LENGTH_LONG)
//                            .show()
//
//                    }
//                }
//            }
//        }
//    }
//
//    private fun load(link: String, imageView: ImageView) {
//        Glide.with(this@AddProductActivity)
//            .load(link)
//            .override(190, 190)
//            .centerCrop()
//            .into(imageView)
//    }
//
//    private fun requestPermission() {
//        requestCameraPermissionIfMissing { granted ->
//            if (granted) {
//                startCamera()
//            } else {
//                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun requestCameraPermissionIfMissing(onResult: (Boolean) -> Unit) {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            onResult(true)
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//
//    private fun startCamera() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                val uriTemp = createPhotoUri()
//                uriTemp?.let { uri ->
//                    photoUri = uri
//                    photoUri.let {
//                        takePictureLauncher.launch(it)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun createPhotoUri(): Uri? {
//        val photoFile: File? = try {
//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
//        } catch (ex: IOException) {
//            null
//        }
//
//        return photoFile?.let {
//            FileProvider.getUriForFile(
//                this,
//                "com.example.sellit.fileprovider",
//                it
//            )
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun uploadImageToFirebase(imageUri: Uri?) {
//        if (imageUri != null) {
//            val fileReference = storageReference.child("images/" + UUID.randomUUID().toString())
//
//            fileReference.putFile(imageUri)
//                .addOnSuccessListener { downloadUri ->
//                    val name = downloadUri.storage.name
//                    val url =
//                        "https://firebasestorage.googleapis.com/v0/b/sellit-tfg.appspot.com/o/images%2F$name?alt=media&token=3dcb5393-30ed-4bbc-8b8b-6f3fa4c546ff"
//                    guardarEnCache(url)
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun guardarEnCache(url: String) {
//        viewModel.handleEvent(AddProductEvent.AddImage(url))
//    }
//}