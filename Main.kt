package abac_blockchain

import abac_blockchain.abac.ABACAlgorithm
import abac_blockchain.abac.ABACP
import abac_blockchain.attributes.AttributeEnvironment
import abac_blockchain.attributes.AttributeObject
import abac_blockchain.attributes.AttributePermission
import abac_blockchain.attributes.AttributeSubject
import org.json.JSONObject
import java.util.*

fun main(){
        val input = readLine()!!
        try {
                val jsonObject = JSONObject(input.trim())
                val jsonAction = jsonObject.getString("action")
                if(jsonAction.toLowerCase().equals("add")) {
                        val jsonObjectAS = jsonObject.getJSONObject("attributeSubject")
                        val jsonObjectAO = jsonObject.getJSONObject("attributeObject")
                        val jsonObjectAP = jsonObject.getString("attributePermission")
                        val jsonObjectAE = jsonObject.getJSONObject("attributeEnvironment")
                        val abacp = ABACP(
                                AttributeSubject(
                                        jsonObjectAS.getInt("userId"),
                                        jsonObjectAS.getString("role"),
                                        jsonObjectAS.getString("group")
                                ),
                                AttributeObject(
                                        jsonObjectAO.getString("deviceId")
                                ),
                                if (jsonObjectAP.isNullOrEmpty())
                                        null
                                else if (jsonObjectAP.lowercase(Locale.getDefault()) == ("true"))
                                        AttributePermission.ALLOW
                                else
                                        AttributePermission.DENY,
                                AttributeEnvironment(
                                        jsonObjectAE.getLong("createTime"),
                                        jsonObjectAE.getLong("endTime")
                                )
                        )
                        ABACAlgorithm.addPolicy(abacp)
                } else if(jsonAction.toLowerCase() == "delete") {
                        val jsonObjectAS = jsonObject.getJSONObject("attributeSubject")
                        val jsonObjectAO = jsonObject.getJSONObject("attributeObject")
                        val attributeSubject = AttributeSubject(
                                jsonObjectAS.getInt("userId"),
                                jsonObjectAS.getString("role"),
                                jsonObjectAS.getString("group")
                        )
                        val attributeObject = AttributeObject(
                                jsonObjectAO.getString("deviceId")
                        )
                        ABACAlgorithm.deletePolicy(attributeSubject, attributeObject);
                }else {
                        throw Error("Invalid Action")
                }
        }catch(e:Exception){
                println("Error In Input Data ${e.message}")
        }
}