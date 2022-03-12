package com.example.noteapp.adapter


import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.entities.Notes
import com.makeramen.roundedimageview.RoundedImageView
import java.io.InputStream


class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var arrayList = ArrayList<Notes>()
    var listener:OnItemClickListener? = null


    fun setData(arrayNoteList: List<Notes>){
        arrayList = arrayNoteList as ArrayList<Notes>
    }
    fun setOnClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_items_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        // styling the title and description
        holder.noteTitle.text = arrayList[position].title
        holder.noteTitle.maxLines = 2
        holder.noteTitle.ellipsize = TextUtils.TruncateAt.END
        holder.noteDescription.text = arrayList[position].noteText
        holder.noteDescription.maxLines = 6
        holder.noteDescription.ellipsize = TextUtils.TruncateAt.END

        holder.dateTime.text = arrayList[position].dateTime

        arrayList[position].color?.toInt()?.let { holder.cardNoteItem.setCardBackgroundColor(it) }


        //styling the text of the note based onn the background color
        if (arrayList[position].colorChar == "WHITE") {
            holder.noteTitle.setTextColor(Color.rgb(16, 20, 28))
        } else if (arrayList[position].colorChar == "DEFAULT") {
            holder.noteTitle.setTextColor(Color.rgb(255, 255, 255))
            holder.noteDescription.setTextColor(Color.rgb(202, 202, 202))
            holder.dateTime.setTextColor(Color.rgb(202, 202, 202))
        }

        //setting the image of the note
        if (arrayList[position].imgPath != "") {
            holder.noteImage.setImageBitmap(BitmapFactory.decodeFile(arrayList[position].imgPath))
            holder.noteImage.visibility = View.VISIBLE
        } else {
            holder.noteImage.visibility = View.GONE
        }

        holder.cardNoteItem.setOnClickListener {
            listener!!.onClicked(arrayList[position].id!!)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle = view.findViewById<TextView>(R.id.rvNoteTitle)
        val noteDescription = view.findViewById<TextView>(R.id.rvNoteDescription)
        val dateTime = view.findViewById<TextView>(R.id.rvDateTime)
        val cardNoteItem = view.findViewById<CardView>(R.id.card_view_item)
        val noteImage = view.findViewById<RoundedImageView>(R.id.note_image)
    }


    interface  OnItemClickListener{
        fun onClicked(notesId:Int)
    }


}