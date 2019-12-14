package com.giladdev.testcase.UI

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.giladdev.testcase.R
import com.giladdev.testcase.model.BuildingInfo
import com.giladdev.testcase.model.PurchaseInfo
import com.giladdev.testcase.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var dashboardViewModel: MainViewModel

    lateinit var purchasesList: MutableList<PurchaseInfo>
    lateinit var buildingsList : MutableList<BuildingInfo>

    var ManufacturList = arrayListOf<String>()

    // hash maps for calculations
    var manufactorHash = hashMapOf<String,Double>()
    var categoryHash = hashMapOf<String,Double>()
    var itemIDHash= hashMapOf<String,Int>()
    var buildingidHash = hashMapOf<Int,Double>()
    var countriesHash = hashMapOf<String, Double>()
    var statesHash = hashMapOf<String,Double>()

    // lists for the adapters
    var categoryList = arrayListOf<String>()
    var countiresList = arrayListOf<String>()
    var stateList = arrayListOf<String>()
    var itemsList = arrayListOf<String>()

    // the adapters
    lateinit var manufacturAdapter : ArrayAdapter<String>
    lateinit var categoryListAdapter : ArrayAdapter<String>
    lateinit var countriesListAdapter: ArrayAdapter<String>
    lateinit var itemsAdapter : ArrayAdapter<String>
    lateinit var stateAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title="Mapsted Test Case #8"
        dashboardViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // set the adapters
        manufacturAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item
            ,ManufacturList )

        categoryListAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item
            , categoryList)

        countriesListAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item
            , countiresList)

        stateAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item
            , stateList)

        itemsAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item
            , itemsList)

        // Apply the adapter to the spinner
        spinnerManufactor.adapter = manufacturAdapter

        spinnerCategory.adapter = categoryListAdapter

        spinnerCountry.adapter = countriesListAdapter

        spinnerState.adapter = stateAdapter

        spinnerItemID.adapter = itemsAdapter

        // read the JSON objects with Retrofit
        retriveData()

        // observer the received data
        observeData()

        // set the UI spinner listeners
        setUIListeners()


    }

    fun updateMostSellingBuilding(){
        var buildingInfo : BuildingInfo

        var mapEntry : Map.Entry<Int,Double>?=null

        mapEntry = buildingidHash.maxBy {
            it.value
        }

        buildingMostPurchase.text = "Building: ${mapEntry!!.key.toString()}"

        buildingInfo = buildingsList.find {
            it.building_id==mapEntry.key.toString()
        }!!

        buildingName.text = "Building name: ${buildingInfo.building_name}"

    }



    fun setUIListeners()
    {
        spinnerManufactor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var ManufacturSelected = ManufacturList[position]
                totalManufactor.text = "$ ${manufactorHash[ManufacturSelected]}"
            }

        }

        spinnerCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var categorySelected = categoryList[position]
                totalCategory.text = "$ ${categoryHash[categorySelected]}"
            }

        }

        spinnerCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var countrySelected = countiresList[position]
                totalCountry.text = "$ ${countriesHash[countrySelected]}"
            }
        }

        spinnerState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var stateSelected = stateList[position]
                totalState.text = "$ ${statesHash[stateSelected]}"
            }


        }

        spinnerState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var stateSelected = stateList[position]
                totalState.text = "$ ${statesHash[stateSelected]}"
            }


        }


        spinnerItemID?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var itemSelected = itemsList[position]
                totalItemID.text = "${itemIDHash[itemSelected]}"
            }


        }


    }
    fun observeData(){
        dashboardViewModel.purchasesList.observe(this, Observer {
            purchasesList = it
            updateUISpinners()
        })

        dashboardViewModel.buildingList.observe(this, Observer {
            buildingsList = it
        })
    }

    fun updateUISpinners()
    {
        purchasesList.forEach {purchaseInfo->

            purchaseInfo.usage_statistics!!.buildings!!.forEach {building->
                building.purchases!!.forEach {
                    var manufactor = purchaseInfo.Manufactor!!
                    var prevSum : Double = 0.0
                    var prevCategorySum: Double = 0.0
                    var prevBuildingidSum : Double = 0.0
                    var prevItemIDSum = 0

                    if ( manufactorHash[manufactor] != null){
                        prevSum = manufactorHash[manufactor]!!
                    }

                    if (categoryHash[it.item_category_id.toString()] != null){
                        prevCategorySum = categoryHash[it.item_category_id.toString()]!!
                    }

                    if (itemIDHash[it.item_id.toString()] != null){
                        prevItemIDSum = itemIDHash[it.item_id.toString()]!!
                    }

                    if (buildingidHash[building.buildingID.toInt()] != null){
                        prevBuildingidSum = buildingidHash[building.buildingID.toInt()]!!
                    }

                    manufactorHash[manufactor] = it.cost+prevSum
                    categoryHash[it.item_category_id.toString()] = it.cost+prevCategorySum
                    buildingidHash[building.buildingID] = it.cost+prevBuildingidSum
                    itemIDHash[it.item_id.toString()] = 1+prevItemIDSum


                }
            }
            // add manufacture to the list
        }

        // for countires and state the calculation is done from the buildings list
        UpdateCountriesStatesHash()

        // update the lists for the adapters
        manufactorHash.forEach { t, _ ->
            ManufacturList.add(t)
        }

        categoryHash.forEach { t, _ ->
            categoryList.add(t)
        }

        countriesHash.forEach { t, _ ->
            countiresList.add(t)
        }

        itemIDHash.forEach { t, _ ->
            itemsList.add(t)
        }

        statesHash.forEach { t, _ ->
            stateList.add(t)
        }

        updateMostSellingBuilding()


        manufacturAdapter.notifyDataSetChanged()
        categoryListAdapter.notifyDataSetChanged()
        countriesListAdapter.notifyDataSetChanged()
        stateAdapter.notifyDataSetChanged()
        itemsAdapter.notifyDataSetChanged()
    }

    fun UpdateCountriesStatesHash(){

        buildingsList.forEach {

            var prevCountrySum : Double = 0.0
            var sumToAdd=0.0
            var prevStateSum: Double = 0.0

            if (statesHash[it.state.toString()] != null){
                prevStateSum = statesHash[it.state.toString()]!!
            }

            if (countriesHash[it.country.toString()] != null){
                prevCountrySum = countriesHash[it.country.toString()]!!
            }

            if (buildingidHash[it.building_id!!.toInt()]!=null){
                sumToAdd = buildingidHash[it.building_id!!.toInt()]!!
            }

            countriesHash[it.country.toString()] = prevCountrySum+sumToAdd
            statesHash[it.state.toString()] = prevStateSum+sumToAdd

        }

    }

    fun retriveData(){
        dashboardViewModel.getPurchasesRxJava()
        dashboardViewModel.getBuildingsRxJava()

    }
}
