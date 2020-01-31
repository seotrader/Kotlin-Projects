package com.almitasoft.choremeapp.ui.addTask

import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseInterface
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.User

class AddTaskViewModel(var fb : FireBaseInterface) : ViewModel() {
    var taskTime = 0L
    var userPicket = User(CurrentUser.displanyName!!,CurrentUser.userID!!)
}
