package com.example.exchangeapp.screens.camera

import android.Manifest
import android.net.Uri
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(viewModel: CameraViewModel = hiltViewModel()) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.BottomCenter
        ) {
            FloatingActionButton(
                onClick = {
                    val executor = ContextCompat.getMainExecutor(context)
                    takePicture(cameraController, executor, viewModel)
                },
                contentColor = Color(0xFF0F3048)
            ) {
                Icon(
                    imageVector = Icons.Filled.Camera,
                    contentDescription = "Camera Icon"
                )
            }
        }
    }) {
        if (permissionState.status.isGranted) {
            CameraComposable(cameraController, lifecycle, modifier = Modifier.padding(it))
        } else {
            Text(text = "Permission Denied!", modifier = Modifier.padding(it))
        }
    }
}

private fun takePicture(cameraController: LifecycleCameraController, executor: Executor, viewModel: CameraViewModel) {
    val file = File.createTempFile("imagetest", ".jpg")
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()

    cameraController.takePicture(
        outputDirectory,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                // Upload image to Firebase Storage
                uploadImageToStorage(file, viewModel)
            }

            override fun onError(exception: ImageCaptureException) {
                // Handle error
                println("Error capturing image: ${exception.message}")
            }
        },
    )
}


@Composable
fun CameraComposable(
    cameraController: LifecycleCameraController,
    lifecycle: LifecycleOwner,
    modifier: Modifier = Modifier,
) {
    cameraController.bindToLifecycle(lifecycle)
    AndroidView(modifier = modifier, factory = { context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        previewView.controller = cameraController

        previewView
    })
}


private fun uploadImageToStorage(file: File, viewModel: CameraViewModel) {
    val storageReference = FirebaseStorage.getInstance().reference
    val fileUri = Uri.fromFile(file)
    val profileImagesRef = storageReference.child("profile_pictures/${file.name}")
    profileImagesRef.putFile(fileUri)
        .addOnSuccessListener { taskSnapshot ->
            // Get the download URL
            profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                // Update the profile picture URL in Firestore
                val profilePictureUrl = uri.toString()
                viewModel.updateProfilePictureUrl(profilePictureUrl)
            }
        }
        .addOnFailureListener { e ->
            // Handle the failure
            println("Error uploading image: ${e.message}")
        }
}
