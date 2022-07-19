package com.magnum_ti1.plant.data.repository.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.data.entity.User
import com.magnum_ti1.plant.data.route.FirebaseReferences
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthRepositoryImpl : AuthRepository, KoinComponent {

    private val auth: FirebaseAuth by inject()
    private val firebaseReferences: FirebaseReferences by inject()

    override suspend fun register(user: User): Flow<Response<User>> = callbackFlow {
        auth.createUserWithEmailAndPassword(user.email as String, user.password as String)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    val newUser = user.copy(
                        userId = userId,
                        password = null
                    )

                    firebaseReferences.userReferences().child(userId ?: "0").setValue(newUser)
                    trySend(Response.Success(newUser)).isSuccess
                }
            }.addOnFailureListener {
                trySend(Response.Fail(it)).isFailure
            }

        awaitClose { this.cancel() }
    }

    override suspend fun login(user: User): Flow<Response<User>> = callbackFlow {
        auth.signInWithEmailAndPassword(user.email as String, user.password as String)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    firebaseReferences.userReferences().child(userId ?: "0").addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val newUser = snapshot.getValue(User::class.java) as User
                                trySend(Response.Success(newUser)).isSuccess
                            }

                            override fun onCancelled(error: DatabaseError) {}

                        })
                }
            }.addOnFailureListener {
                trySend(Response.Fail(it)).isFailure
            }

        awaitClose { this.cancel() }
    }

    override suspend fun resetPassword(user: User): Flow<Response<Boolean>> = callbackFlow {
        auth.sendPasswordResetEmail(user.email as String).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Response.Success(true)).isSuccess
            }
        }.addOnFailureListener {
            trySend(Response.Fail(it)).isFailure
        }

        awaitClose { this.cancel() }
    }

    override suspend fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount): Flow<Response<User>> =
        callbackFlow {
            auth.signInWithCredential(
                GoogleAuthProvider.getCredential(
                    googleSignInAccount.idToken,
                    null
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    val firebaseUser = auth.currentUser
                    val email = firebaseUser?.email.toString()
                    val getPhoto = firebaseUser?.photoUrl?.toString()
                    val resizePhoto = getPhoto?.replace("s96-c", "s400-c").toString()
                    val username = firebaseUser?.displayName

                    val newUser = User(
                        username = username,
                        userId = userId,
                        photo = resizePhoto,
                        email = email,
                        password = null,
                        phoneNumber = null
                    )
                    firebaseReferences.userReferences().child(userId ?: "0").setValue(newUser)
                    trySend(Response.Success(newUser)).isSuccess
                }
            }.addOnFailureListener {
                trySend(Response.Fail(it)).isFailure
            }
            awaitClose { this.cancel() }
        }
}