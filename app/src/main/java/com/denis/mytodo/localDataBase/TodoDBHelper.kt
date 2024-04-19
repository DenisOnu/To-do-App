package com.denis.mytodo.localDataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.denis.mytodo.dataClasess.TodoModel
import com.denis.mytodo.dataClasess.UserModel

class TodoDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TodoDB"
        private const val TABLE_TODO = "todos"
        private const val KEY_ID = "id"
        private const val KEY_TODO = "todo"
        private const val KEY_CHECKED = "checked"
        private const val TABLE_USERS = "users"
        private const val KEY_EMAIL = "email"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_TODO ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_TODO TEXT, $KEY_CHECKED INTEGER)"
        db.execSQL(createTableQuery)

        val createUserTableQuery = "CREATE TABLE $TABLE_USERS ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_EMAIL TEXT,$KEY_USERNAME TEXT, $KEY_PASSWORD TEXT)"
        db.execSQL(createUserTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun addTodo(todo: TodoModel): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO, todo.todo)
        values.put(KEY_CHECKED, if (todo.checked) 1 else 0)
        val id = db.insert(TABLE_TODO, null, values)
        db.close()
        return id
    }

    fun getTodoById(id: Int): TodoModel? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_TODO, arrayOf(KEY_ID, KEY_TODO, KEY_CHECKED), "$KEY_ID=?", arrayOf(id.toString()), null, null, null)
        val todoModel: TodoModel?
        if (cursor != null && cursor.moveToFirst()) {
            todoModel = TodoModel()
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val todoIndex = cursor.getColumnIndex(KEY_TODO)
            val checkedIndex = cursor.getColumnIndex(KEY_CHECKED)

            if (idIndex >= 0) {
                todoModel.id = cursor.getInt(idIndex)
            }
            if (todoIndex >= 0) {
                todoModel.todo = cursor.getString(todoIndex)
            }
            if (checkedIndex >= 0) {
                todoModel.checked = cursor.getInt(checkedIndex) == 1
            }
        } else {
            todoModel = null
        }
        cursor?.close()
        db.close()
        return todoModel
    }

    fun getAllTodos(): List<TodoModel> {
        val todoList = mutableListOf<TodoModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TODO", null)
        if (cursor.moveToFirst()) {
            do {
                val todoModel = TodoModel()
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val todoIndex = cursor.getColumnIndex(KEY_TODO)
                val checkedIndex = cursor.getColumnIndex(KEY_CHECKED)

                if (idIndex >= 0) {
                    todoModel.id = cursor.getInt(idIndex)
                }
                if (todoIndex >= 0) {
                    todoModel.todo = cursor.getString(todoIndex)
                }
                if (checkedIndex >= 0) {
                    todoModel.checked = cursor.getInt(checkedIndex) == 1
                }

                todoList.add(todoModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todoList
    }

    fun updateTodo(todo: TodoModel): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO, todo.todo)
        values.put(KEY_CHECKED, if (todo.checked) 1 else 0)
        val result = db.update(TABLE_TODO, values, "$KEY_ID=?", arrayOf(todo.id.toString()))
        db.close()
        return result
    }

    fun deleteTodoById(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_TODO, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }


    fun registerUser(user: UserModel): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_EMAIL, user.email)
        values.put(KEY_USERNAME, user.username)
        values.put(KEY_PASSWORD, user.password)
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    fun loginUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_ID), "$KEY_USERNAME=? AND $KEY_PASSWORD=?", arrayOf(username, password), null, null, null)
        val success = cursor != null && cursor.count > 0
        cursor?.close()
        db.close()
        return success
    }
}
