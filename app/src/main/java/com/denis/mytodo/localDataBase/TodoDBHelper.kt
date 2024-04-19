package com.denis.mytodo.localDataBase

import com.denis.mytodo.dataClasess.TodoModel
import com.denis.mytodo.dataClasess.UserModel
import com.denis.mytodo.dataManager.DataManager

class DataHelper {

    fun addTodo(todo: TodoModel): Int = DataManager.addTodo(todo)

    fun getTodoById(id: Int): TodoModel? = DataManager.getTodoById(id)

    fun getAllTodos(): List<TodoModel> = DataManager.getAllTodos()

    fun updateTodo(todo: TodoModel): Boolean = DataManager.updateTodo(todo)

    fun deleteTodoById(id: Int): Boolean = DataManager.deleteTodoById(id)

    fun registerUser(user: UserModel): Int = DataManager.registerUser(user)

    fun loginUser(username: String, password: String): Boolean = DataManager.loginUser(username, password)
}
