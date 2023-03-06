package com.example.permissionhandler

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermentlyDeclined: Boolean,
    onDismiss:()-> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick:() -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(onDismissRequest = onDismiss,
        buttons = { Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Divider()
            Text(
                text = if (isPermentlyDeclined) {
                "Grant permission"
                } else{
                    "Ok"
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isPermentlyDeclined) {
                            onGoToAppSettingsClick()
                        } else {
                            onOkClick()
                        }
                    }
                    .padding(16.dp)

            )
        }

        },
        title = { Text(text = "Permission requires")

        },
        text = {

               Text(text = permissionTextProvider.getDescription(
                   isPermentlyDeclined = isPermentlyDeclined
               ))
        },
        modifier = modifier)
}
interface  PermissionTextProvider{
   // val description: String
    fun getDescription(isPermentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider: PermissionTextProvider{
    override  fun getDescription(isPermentlyDeclined: Boolean): String{
        return if(isPermentlyDeclined){
            "It seems you permently declined camera permission. " +
                    "You can go to the app settings to grant it."
        }else{
            "This app needs acces to your camera so that your friends " +
                    "can see you in a call."
        }
    }
}
class RecordAudioPermissionTextProvider: PermissionTextProvider{
    override  fun getDescription(isPermentlyDeclined: Boolean): String{
        return if(isPermentlyDeclined){
            "It seems you permently declined microphone permission. " +
                    "You can go to the app settings to grant it."
        }else{
            "This app needs acces to your microphone so that your friends " +
                    "can hear you in a call."
        }
    }
}
class PhoneCallPermissionTextProvider: PermissionTextProvider{
    override  fun getDescription(isPermentlyDeclined: Boolean): String{
        return if(isPermentlyDeclined){
            "It seems you permently declined phone calling permission. " +
                    "You can go to the app settings to grant it."
        }else{
            "This app needs phone calling to your microphone so that your friends " +
                    "can hear you in a call."
        }
    }
}


