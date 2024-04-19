package com.denis.mytodo



import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.denis.mytodo.Preferences.setLoggedIn
import com.denis.mytodo.adapters.ToDoAdapter
import com.denis.mytodo.dataClasess.TodoModel
import com.denis.mytodo.databinding.FragmentHomeBinding
import com.denis.mytodo.localDataBase.DataHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment(),ToDoAdapter.OnClickListener {


    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: ToDoAdapter
    lateinit var todoDBHelper: DataHelper
    val toDoList= mutableListOf<TodoModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        todoDBHelper= DataHelper()


        binding.logoutBtn.setOnClickListener { showLogoutDialog() }
        binding.addBtn.setOnClickListener {
            showToDoDialog()
        }


        getData()

    }

    private fun getData(){

        launch {
            toDoList.clear()
            todoDBHelper.getAllTodos().let { toDoList.addAll(it) }
            activity?.runOnUiThread {
                adapter= ToDoAdapter(this@HomeFragment,toDoList)
                binding.todoRV.adapter=adapter
            }
        }

    }

    override fun onCheckChange(position: Int) {
        val item=toDoList[position]
        if(item.checked){
            item.checked=false
        }else{
            item.checked=true
        }
        toDoList.set(position,item)
        adapter.notifyItemChanged(position)

        launch {
            todoDBHelper.updateTodo(item)
        }

    }

    override fun onDelete(position: Int) {
        showOptionDialog(position)
    }

    private fun showToDoDialog(position: Int=-1){
        val dialog= BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.layout_add_todo)
        val textBox= dialog.findViewById<EditText>(R.id.todoBox)
        if(position!=-1) {textBox?.setText(toDoList[position].todo)}
        dialog.findViewById<AppCompatButton>(R.id.addBtn)?.setOnClickListener {
            val todo= textBox?.text.toString()
            if(todo.isNotEmpty()) {
                if(position==-1) { addToDo(todo) }
                else{updateToDo(todo,position)}
            }
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun addToDo(todo:String){

        val todoModel=TodoModel()
        todoModel.todo=todo
        launch {
            todoDBHelper.addTodo(todoModel)
            getData()
        }
        Toast.makeText(requireContext(),"To-do added", Toast.LENGTH_LONG).show()

    }

    private fun updateToDo(todo:String,position: Int){

        val todoModel=toDoList[position]
        todoModel.todo=todo
        launch {
            todoDBHelper.updateTodo(todoModel)
            getData()
        }
        Toast.makeText(requireContext(),"To-do updated", Toast.LENGTH_LONG).show()

    }


    private fun showLogoutDialog(){
        val dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_logout)
        dialog.findViewById<TextView>(R.id.ok).setOnClickListener {
            requireContext().setLoggedIn(false)
            findNavController().navigate(HomeFragmentDirections.navToLogin())
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showOptionDialog(position: Int){
        val dialog=BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.layout_select_option)
        dialog.findViewById<TextView>(R.id.deleteBtn)?.setOnClickListener {
            dialog.dismiss()
            showDeleteDialog(position)
        }
        dialog.findViewById<TextView>(R.id.editBtn)?.setOnClickListener {
            dialog.dismiss()
            showToDoDialog(position)
        }
        dialog.show()
    }

    private fun showDeleteDialog(position: Int){
        val dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_delete)
        dialog.findViewById<TextView>(R.id.ok).setOnClickListener {

            val item=toDoList[position]
            launch {
                todoDBHelper.deleteTodoById(item.id)
            }
            toDoList.removeAt(position)
            adapter.notifyItemRemoved(position)
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


}

