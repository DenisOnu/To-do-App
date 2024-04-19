package com.denis.mytodo.dataManager

import com.denis.mytodo.dataClasess.TodoModel
import com.denis.mytodo.dataClasess.UserModel

object DataManager {
    private val todos: MutableList<TodoModel> = mutableListOf()
    private val users: MutableList<UserModel> = mutableListOf()

    fun addTodo(todo: TodoModel): Int {
        todo.id = (todos.maxOfOrNull { it.id } ?: 0) + 1
        todos.add(todo)
        return todo.id
    }

    fun getTodoById(id: Int): TodoModel? = todos.find { it.id == id }

    fun getAllTodos(): List<TodoModel> = todos.toList()

    fun updateTodo(todo: TodoModel): Boolean {
        val index = todos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            todos[index] = todo
            return true
        }
        return false
    }

    fun deleteTodoById(id: Int): Boolean = todos.removeIf { it.id == id }

    fun registerUser(user: UserModel): Int {
        user.id = (users.maxOfOrNull { it.id } ?: 0) + 1
        users.add(user)
        return user.id
    }

    fun loginUser(username: String, password: String): Boolean {
        return users.any { it.username == username && it.password == password }
    }
}
