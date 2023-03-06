package com.example.permissionhandler

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.permissionhandler.ui.theme.PermissionHandlerTheme

class MainActivity : ComponentActivity() {

    private val permissionToRequest = arrayOf(Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandlerTheme {
                // A surface container using the 'background' color from the theme
                val viewModel = viewModel<MainViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult ={
                        isGranted ->
                        viewModel.onPermissionResult(permission = Manifest.permission.CAMERA,
                        isGranted = isGranted)
                    }
                )
                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult ={
                        perms ->
                        permissionToRequest.forEach{ permission ->

                            viewModel.onPermissionResult(permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }

                    }
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement =  Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(onClick = {

                        cameraPermissionResultLauncher.launch(
                            Manifest.permission.CAMERA
                        )


                    }) { Text(text = "Request one permission")

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { multiplePermissionResultLauncher.launch(permissionToRequest) }) {
                        Text(text = "Requesr multiple permission")
                    }
                }
               dialogQueue
                   .reversed()
                   .forEach { permission ->
                   PermissionDialog(
                       permissionTextProvider = when(permission){
                                                                Manifest.permission.CAMERA ->
                                                                {
                                                                    CameraPermissionTextProvider()

                                                                }
                           Manifest.permission.RECORD_AUDIO ->
                                                                {
                                                                    RecordAudioPermissionTextProvider()

                                                                }
                           Manifest.permission.CALL_PHONE ->
                                                                {
                                                                    PhoneCallPermissionTextProvider()

                                                                }
                           else-> return@forEach
                                                                },
                       isPermentlyDeclined = !shouldShowRequestPermissionRationale(
                           permission
                       ),
                       onDismiss = viewModel::dismissDialog,
                       onOkClick = {
                                   viewModel.dismissDialog()
                           multiplePermissionResultLauncher.launch(
                               arrayOf(permission)
                           )
                       },
                       onGoToAppSettingsClick = ::openAppSetting)
               }
            }
        }
    }
}

fun Activity.openAppSetting(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package", packageName,null)
    ).also(::startActivity)
}


