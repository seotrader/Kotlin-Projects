package com.giladdev.testcase.model

import com.google.gson.annotations.SerializedName

class PurchaseInfo {
    @SerializedName("manufacturer")
    var Manufactor: String?= null
    @SerializedName ("market_name")
    var market_name : String?=null
    @SerializedName ("codename")
    var codename : String?=null
    @SerializedName ("model")
    var model: String?=null
    @SerializedName("usage_statistics")
    var usage_statistics: usageStatistics?=null

}

class usageStatistics{
    @SerializedName("session_infos")
    var buildings : MutableList<buildingPurchases>?=null
}

class buildingPurchases{
    @SerializedName("building_id")
    var buildingID : Int = 0
    @SerializedName("purchases")
    var purchases : MutableList<singlePurchase>?=null
}

class singlePurchase{
    @SerializedName("item_id")
    var item_id : Int=0
    @SerializedName("item_category_id")
    var item_category_id : Int=0
    @SerializedName("cost")
    var cost : Double=0.0
}

