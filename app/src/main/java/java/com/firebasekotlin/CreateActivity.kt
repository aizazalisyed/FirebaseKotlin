package java.com.firebasekotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@Suppress("DEPRECATION")
class CreateActivity : AppCompatActivity() {

    var signedInUser: UserModel? = null
    lateinit var firestoreDB : FirebaseFirestore
    lateinit var storageReference : StorageReference
    private val Tag = "CreateActivity"
    val PickerCode = 123
    lateinit var imageView: ImageView
    var photoUri : Uri? = null
    lateinit var uploadButton: Button
    lateinit var description: EditText
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)


        storageReference = FirebaseStorage.getInstance().reference
        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener{
                signedInUser = it.toObject(UserModel::class.java)
                Log.i("profile","${signedInUser}")
            }

        uploadButton = findViewById(R.id.uploadButton)
        imageView = findViewById(R.id.imageView)
        description = findViewById(R.id.editTextdescription)
        submitButton = findViewById(R.id.submitButton)


        uploadButton.setOnClickListener{
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"

            if(imagePickerIntent.resolveActivity(packageManager) != null){
                startActivityForResult(imagePickerIntent,PickerCode)
            }
        }
        submitButton.setOnClickListener {

            submitButton.isEnabled = false
            val photoReference = storageReference.child("images/${System.currentTimeMillis()}-photo.jpg")
            photoReference.putFile(photoUri as Uri)
                .continueWithTask {photoUploadTask ->
                    photoReference.downloadUrl
                }.continueWithTask { downloadUrlTask ->
                    val post = PostModel(
                        description = description.text.toString(),
                        imageUrl = downloadUrlTask.result.toString(),
                        creationTimeMs = System.currentTimeMillis(),
                        user = signedInUser
                    )

                    firestoreDB.collection("posts").add(post)
                }.addOnCompleteListener{

                    submitButton.isEnabled = true
                    if(it.isSuccessful){
                        Toast.makeText(this, "post uploaded", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "post not uploaded", Toast.LENGTH_SHORT).show()
                    }
                    startActivity(Intent(this@CreateActivity, PostActivity::class.java))
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PickerCode){
            if(resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                Log.i("PhotoURi","${photoUri}" )
                imageView.setImageURI(photoUri)
            }
        }
    }
}