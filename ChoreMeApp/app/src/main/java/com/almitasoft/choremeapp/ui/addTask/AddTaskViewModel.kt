package com.almitasoft.choremeapp.ui.addTask

import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseInterface

class AddTaskViewModel(var fb : FireBaseInterface) : ViewModel() {
    var taskTime = 0L
}
