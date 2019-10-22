package com.giladdev.rickyandmarty.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.model.CharacterList
import com.giladdev.rickyandmarty.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private var charAdapter =  CharacterListAdaptor(arrayListOf(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        // apply - Scope Functions
        RecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = charAdapter
        }

        errorTextView.visibility = View.GONE
        RecyclerView.visibility = View.GONE

        observeViewModel()
    }

    fun observeViewModel() {

        charAdapter.lastLine.observe(this, Observer {isLastLine : Boolean->
            isLastLine.let{
                if (it == true) {
                    viewModel.refresh()
                    charAdapter.lastLine.value = false
                }
                }
        })
        viewModel.countryLoadError.observe(this, Observer {isError: Boolean ->
            isError?.let{
                errorTextView.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        viewModel.characters.observe(this, androidx.lifecycle.Observer { characters: CharacterList? ->
            characters?.let {
                RecyclerView.visibility = View.VISIBLE
                charAdapter.UpdateCharacters(it)}
        })

        viewModel.loading.observe(this, Observer {isLoading : Boolean ->
            isLoading?.let {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE

                if (it)
                {
                    RecyclerView.visibility = View.GONE
                }
            }
        })
    }


}
/*
    fun loadData() {

        // create the Retrofit object
        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        // gets the API class
        var api = retrofit.create(Api::class.java)

        var call : Call<CharacterList> = api.GetCharecters()

        call.enqueue(object : Callback<CharacterList>{
            override fun onFailure(call: Call<CharacterList>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<CharacterList>,
                response: Response<CharacterList>
            ) {
                var Charecters : CharacterList? = response.body()

                for (x in Charecters!!.characterList!!.toTypedArray())
                {
                    var newCharacter : Character? = Character(x.name,x.gender,x.image)
                    if (newCharacter != null) {
                        characterslist!!.add(newCharacter)
                    }
                adapter!!.notifyDataSetChanged()
                }
            }
        } )
    }


    private fun handleResponse(characterList: List<Character>) {
        characterslist = ArrayList(characterList)

        adapter!!.notifyDataSetChanged()
    }

    override fun onDestroy() {

    }
*/





