package java.com.firebasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    lateinit var firestoreDB : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firestoreDB = FirebaseFirestore.getInstance()

        val postsReference = firestoreDB.collection("posts")
        postsReference.addSnapshotListener { value, error ->
            if(error != null || value == null){
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
        }
            for (documents in value.documents){
                //todo: will represent data
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_profile,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       if(item.itemId == R.id.logout){
           FirebaseAuth.getInstance().signOut()
           val logoutIntent = Intent(this, LoginActivity::class.java)
           logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
           startActivity(logoutIntent)
       }
        return super.onOptionsItemSelected(item)
    }
}