package com.giladdev.rickyandmarty.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.data.CharacterListAdaptor
import com.giladdev.rickyandmarty.model.Character
import com.giladdev.rickyandmarty.model.Api
import com.giladdev.rickyandmarty.model.CharacterList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private var characterslist : ArrayList<Character>?=null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var adapter : CharacterListAdaptor?=null
    private var layoutManager : RecyclerView.LayoutManager?=null
    private val BASE_URL = "https://rickandmortyapi.com/api/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        characterslist = ArrayList<Character>()
        adapter = CharacterListAdaptor(characterslist!!,this)
        layoutManager = LinearLayoutManager(this)
        myCompositeDisposable = CompositeDisposable()

        // set up the list , recycler view
        RecyclerView.layoutManager = layoutManager
        RecyclerView.adapter = adapter

        loadData()
    }

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




//Build a Retrofit object//

        /*val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//Get a usable Retrofit object by calling .build()//
            .build().create(GetData::class.java)
*/
        /*
//Add all RxJava disposables to a CompositeDisposable//
            myCompositeDisposable?.add(requestInterface.getData()
//Send the Observable’s notifications to the main UI thread//
            .observeOn(AndroidSchedulers.mainThread())
//Subscribe to the Observer away from the main UI thread//
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse))
*/
    }


    private fun handleResponse(characterList: List<Character>) {
        characterslist = ArrayList(characterList)

        adapter!!.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

//Clear all your disposables//

        myCompositeDisposable?.clear()

    }


}



