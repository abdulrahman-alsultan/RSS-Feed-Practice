package com.example.rssfeedpractice

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sof_item_view.view.*
import java.lang.Exception

class SOFRecyclerViewAdapter(private val sOFList: List<SOFData>): RecyclerView.Adapter<SOFRecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.sof_item_view,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val question = sOFList[position]

        holder.itemView.apply {
            tv_published.text = question.published
            tv_question.text = question.title

            card_view.setOnClickListener {
                try {
                    holder.itemView.context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(question.id)))
                }catch (e: Exception){}
            }
        }
    }

    override fun getItemCount(): Int = sOFList.size
}