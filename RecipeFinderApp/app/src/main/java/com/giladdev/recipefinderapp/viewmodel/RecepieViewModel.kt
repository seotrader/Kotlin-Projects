package com.giladdev.recipefinderapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.giladdev.recipefinderapp.model.Recipe
import org.json.JSONException

class RecepieViewModel(app : Application) : AndroidViewModel(app){
    var volleyRequest : RequestQueue? = null
    public var arrayList = MutableLiveData<ArrayList<Recipe>>()
    var tempList = arrayListOf<Recipe>()
    init{
        volleyRequest = Volley.newRequestQueue(app)
    }

    fun getReceipt(url : String){
        var urlString : String = url
        val receiptRequest = JsonObjectRequest(Request.Method.GET,urlString,null,
            Response.Listener{response->
                try{
                    val resultArray = response.getJSONArray("results")
                    for (i in 0..resultArray.length()-1){
                        var receipeObj = resultArray.getJSONObject(i)
                        var title = receipeObj.getString("title")
                        var link = receipeObj.getString("href")
                        var thumbnail = receipeObj.getString("thumbnail")
                        var ingredients  = receipeObj.getString("ingredients")
                        Log.d("Results==>",title)

                        var recipe = Recipe()
                        recipe.title = title
                        recipe.link = link
                        recipe.thumbnail = thumbnail
                        recipe.ingredients = ingredients

                        tempList.add(recipe)
                    }
                    arrayList.value = tempList

                } catch (e : JSONException){
                    e.printStackTrace()
                }

            },
            Response.ErrorListener {
                try{

                }catch (e : JSONException){
                    e.printStackTrace()
                }

            })

            volleyRequest?.let{request->
                request.add(receiptRequest)
            }
    }

}