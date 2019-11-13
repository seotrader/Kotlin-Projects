package com.giladdev.recipefinderapp.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.giladdev.recipefinderapp.R
import com.giladdev.recipefinderapp.model.LEFT_LINK
import com.giladdev.recipefinderapp.model.QUERY
import com.giladdev.recipefinderapp.model.Recipe
import com.giladdev.recipefinderapp.viewmodel.RecepieViewModel
import kotlinx.android.synthetic.main.activity_receipt_list.*

class ReceiptList : AppCompatActivity() {

    lateinit var viewModel: RecepieViewModel
    var receipeAdapter = RecipeListAdapter(arrayListOf<Recipe>(),this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_list)

        viewModel = ViewModelProviders.of(this)[RecepieViewModel::class.java]

        var url: String?
        var extras = intent.extras

        var ingredients = extras?.let{
            it.get("ingredients")}

            var searchTerm = extras?.let{
                it.get("search")}

            if (extras != null && !ingredients!!.equals("")
                && !searchTerm!!.equals("")){
                var tempUrl = LEFT_LINK+ingredients+ QUERY+searchTerm
                url = tempUrl

            }else{
                url = "http://www.recipepuppy.com/api/?i=onions,garlic&q=omelet&p=3"
            }

            viewModel.getReceipt(url)

            observeViewModel()

            recyclerView.apply {

                adapter = receipeAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    fun observeViewModel(){
        viewModel.arrayList.observe(this, Observer {receipteArray->
            receipteArray?.run{
                receipeAdapter.updateRecipes(this)
            }

        })

    }

    }




