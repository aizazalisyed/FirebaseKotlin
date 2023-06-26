package java.com.firebasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User

open class PostActivity : AppCompatActivity() {

    var signedInUser: UserModel? = null
    lateinit var firestoreDB : FirebaseFirestore
    lateinit var posts : MutableList<PostModel>
    lateinit var adapter: PostAdapter
    lateinit var recyclerView: RecyclerView
    final val USERName = "USER_NAME"
    lateinit var fab : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        fab = findViewById(R.id.floating)
        posts = mutableListOf()
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PostAdapter(context = this,posts)
        recyclerView.adapter = adapter

        firestoreDB = FirebaseFirestore.getInstance()

        firestoreDB.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener{
                signedInUser = it.toObject(UserModel::class.java)
                Log.i("profile","${signedInUser}")
            }

        var postsReference = firestoreDB
            .collection("posts")
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)

        var name = intent.getStringExtra(USERName)

        Log.i("intend username","name")
        if(name != null){
            postsReference = postsReference.whereEqualTo("user.username",name)
        }

        //addsnapshot will automatically update the data
        postsReference.addSnapshotListener { value, error ->
            if(error != null || value == null){
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            val postList = value.toObjects(PostModel::class.java)

            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for (post in postList){
                Log.i("posts","Post: ${post}")
            }
        }
        fab.setOnClickListener{
            val intent = Intent(this,CreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       if(item.itemId == R.id.profile){
           val i = Intent(this@PostActivity, ProfileActivity::class.java)
           i.putExtra(USERName,signedInUser?.username)
           startActivity(i)
           Log.i("username","${signedInUser?.username}")
       }
        return super.onOptionsItemSelected(item)
    }
}