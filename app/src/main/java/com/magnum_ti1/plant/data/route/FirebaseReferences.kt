package com.magnum_ti1.plant.data.route

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseReferences {
    fun userReferences() = FirebaseDatabase.getInstance().getReference("user")
    fun plantReferences() = FirebaseFirestore.getInstance().collection("plant")
}