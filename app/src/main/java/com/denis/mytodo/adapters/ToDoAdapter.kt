package com.denis.mytodo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denis.mytodo.R
import com.denis.mytodo.dataClasess.TodoModel
import com.denis.mytodo.databinding.ListItemToDoBinding



class ToDoAdapter(private val mClickListener: OnClickListener, private val itemsList: List<TodoModel>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoItemHolder>() {


    inner class ToDoItemHolder(binding: ListItemToDoBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ListItemToDoBinding
        init {
            this.binding = binding
            binding.checkbox.setOnClickListener {
                mOnClickListener?.onCheckChange(adapterPosition) }
            binding.root.setOnLongClickListener {
                mOnClickListener?.onDelete(adapterPosition)
                true }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemHolder {
        return ToDoItemHolder(
            ListItemToDoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ToDoItemHolder, position: Int) {
        val item=itemsList[position]
        holder.binding.todo.text = item.todo
        if(item.checked){
            holder.binding.checkbox.setImageResource(R.drawable.ic_checked)
        }else{
            holder.binding.checkbox.setImageResource(R.drawable.ic_uncheck)
        }



    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }




    private var mOnClickListener: OnClickListener? = mClickListener

    interface OnClickListener{
        fun onCheckChange(position: Int)
        fun onDelete(position: Int)
    }
}