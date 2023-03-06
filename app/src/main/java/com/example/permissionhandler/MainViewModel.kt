package com.example.permissionhandler

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    //[RECORD_AUDIO, CAMERA]
    val visiblePermissionDialogQueue = mutableListOf<String>()

    fun  dismissDialog(){

        visiblePermissionDialogQueue.removeFirst()
    }
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean

    ){
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)){
            visiblePermissionDialogQueue.add( permission)
        }
    }
}