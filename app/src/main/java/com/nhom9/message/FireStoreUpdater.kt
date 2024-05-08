package com.nhom9.message

import com.google.firebase.storage.StorageReference
import com.nhom9.message.data.USER_NODE

class FireStoreUpdater (
    viewModel: MViewModel
){
    var hashMap : HashMap<String, String> = HashMap<String, String>()
    val uid = viewModel.auth.currentUser?.uid

}