package com.giladdev.testcase.model

import com.google.gson.annotations.SerializedName

class BuildingInfo {
    @SerializedName("building_id")
    var building_id: String?= null
    @SerializedName("building_name")
    var building_name : String?=null
    @SerializedName("city")
    var city : String?=null
    @SerializedName("state")
    var state: String?=null
    @SerializedName("country")
    var country: String?=null
}