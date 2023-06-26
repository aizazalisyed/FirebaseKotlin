package java.com.firebasekotlin

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter (val context : Context, val posts : List<PostModel> ) : RecyclerView.Adapter <PostAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(postModel: PostModel) {
            itemView.findViewById<TextView>(R.id.userName).text = postModel.user?.username
            itemView.findViewById<TextView>(R.id.description).text = postModel.description
            Glide.with(context).load(postModel.imageUrl).into(itemView.findViewById<ImageView>(R.id.imageView))
            itemView.findViewById<TextView>(R.id.time).text = DateUtils.getRelativeTimeSpanString(postModel.creationTimeMs)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

}