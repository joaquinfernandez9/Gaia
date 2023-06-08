package com.example.uigaiav2.framework.accout

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import com.example.uigaiav2.databinding.FragmentAccountBinding
import com.example.uigaiav2.domain.usecases.crypt.CryptoManager
import dagger.hilt.android.AndroidEntryPoint
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.example.uigaiav2.framework.MainActivity
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class AccountFragment : AppCompatActivity() {
    private lateinit var binding: FragmentAccountBinding
    private val cryptoManager = CryptoManager()

    //camera
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        binding = FragmentAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cryptoManager.readTxt(this@AccountFragment)

        with(binding) {
            btnCamera.setOnClickListener {
                cameraCheckPermission()
            }

            btnGallery.setOnClickListener {
                gallery()
            }

            closeSession.setOnClickListener {
                cryptoManager.logout(this@AccountFragment)
                val intent = Intent(this@AccountFragment, MainActivity::class.java)
                startActivity(intent)
            }

            loadUserPhotoFromFirebaseStorage(cryptoManager.readTxt(this@AccountFragment))
        }

    }

    private fun loadUserPhotoFromFirebaseStorage(username: String) {
        val fileName = "$username.jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_pics/$fileName")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imageView.load(bitmap) {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }.addOnFailureListener { exception ->
            Log.e(
                "FirebaseStorage",
                "Error downloading image: ${exception.message}",
                exception
            )
        }
    }


    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@AccountFragment,
                    "You have denied the storage permission to select image",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }
            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun cameraCheckPermission() {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(
            object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.let {
                        camera()
                        if (p0.areAllPermissionsGranted()) {
                            Log.d("Permissions", "All permissions are granted")
                        } else {
                            Log.d("Permissions", "At least one permission is denied")
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRotationalDialogForPermission()
                }
            }
        ).onSameThread().check()
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun uploadImageToFirebaseStorage(bitmap: Bitmap, username: String) {
        val fileName = "$username.jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_pics/$fileName")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            Toast.makeText(this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error al subir la imagen: ${exception.message}", exception)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    //we are using coroutine image loader (coil)
                    binding.imageView.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                    uploadImageToFirebaseStorage(
                        bitmap,
                        cryptoManager.readTxt(this@AccountFragment)
                    )
                }
                GALLERY_REQUEST_CODE -> {
                    binding.imageView.load(data?.data) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        data?.data
                    )
                    uploadImageToFirebaseStorage(
                        bitmap,
                        cryptoManager.readTxt(this@AccountFragment)
                    )
                }
            }
        }
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off permissions"
                        + "required for this feature. It can be enable under App settings!!!"
            )
            .setPositiveButton("Go TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}