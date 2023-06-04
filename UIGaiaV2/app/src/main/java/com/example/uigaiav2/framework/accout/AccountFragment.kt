package com.example.uigaiav2.framework.accout

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.camera.core.ImageCapture
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.uigaiav2.R
import com.example.uigaiav2.databinding.FragmentAccountBinding
import com.example.uigaiav2.domain.usecases.crypt.CryptoManager
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import androidx.camera.core.Preview.SurfaceProvider
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.graphics.SurfaceTexture
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.StorageReference

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val cryptoManager = CryptoManager()

    //camera
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var storage: StorageReference
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var photoUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)

        with(binding) {
            cameraBtn.setOnClickListener {
                requestPermission()
            }

            closeSession.setOnClickListener {
                cryptoManager.logout(requireContext())
                requireActivity().finish()
            }
        }

        storage = FirebaseStorage.getInstance().reference

        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) {isPicture ->
                if(isPicture) {
                    //upload to firebase
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera error trying to get picture.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startCamera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera permission is required.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        return binding.root
    }


    private fun load(link: String, imageView: ImageView) {
        Glide.with(requireContext())
            .load(link)
            .override(190, 190)
            .centerCrop()
            .into(imageView)
    }

    private fun requestPermission() {
        requestCameraPermissionIfMissing { granted ->
            if (granted) {
                startCamera()
            } else {
                Snackbar.make(
                    binding.root,
                    "Camera permission is required.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") {
                    requestPermission()
                }.show()
            }
        }
    }

    private fun requestCameraPermissionIfMissing(onResult: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onResult(true)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val uriTemp = createPhotoUri()
                uriTemp?.let { uri ->
                    photoUri = uri
                    photoUri.let {
                        takePictureLauncher.launch(it)
                    }
                }
            }
        }
    }





    private fun createPhotoUri(): Uri? {
        val photoFile: File? = try {
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File? =
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(
                "${cryptoManager.readTxt(requireContext())}_${timeStamp}_",
                ".jpg",
                storageDir
            )
        } catch (ex: Exception) {
            null
        }

        return photoFile?.let {
            FileProvider.getUriForFile(
                requireContext(),
                "com.example.uigaiav2.fileprovider",
                it
            )
        }
    }

}