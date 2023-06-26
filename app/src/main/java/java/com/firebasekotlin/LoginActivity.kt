package java.com.firebasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = Firebase.auth

        if(auth.currentUser != null){
            gotoPostActivity()
        }

        val button:Button = findViewById(R.id.button)
        button.setOnClickListener{

            button.isEnabled = false
            val email : EditText = findViewById(R.id.editTextEmail)
            val password: EditText = findViewById(R.id.editTextTextPassword)
            if(email.text.isBlank() || password.text.isBlank()){
                Toast.makeText(this@LoginActivity, "empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener{task ->
                    button.isEnabled = true
                    if(task.isSuccessful){
                        Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show()
                        gotoPostActivity()
                    }
                    else{
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }

    private fun gotoPostActivity(){
        startActivity(Intent(this,PostActivity::class.java))
        finish()
    }
}