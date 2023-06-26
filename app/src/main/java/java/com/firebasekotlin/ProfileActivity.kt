package java.com.firebasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : PostActivity() {


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_profile,menu)
        return true
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