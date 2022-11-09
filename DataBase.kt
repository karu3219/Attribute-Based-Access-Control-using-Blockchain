package abac_blockchain.database

import abac_blockchain.abac.ABACP
import abac_blockchain.abac.ABACPolicy
import abac_blockchain.attributes.AttributeEnvironment
import abac_blockchain.attributes.AttributeObject
import abac_blockchain.attributes.AttributePermission
import abac_blockchain.attributes.AttributeSubject
import abac_blockchain.blockchain.Block
import java.sql.Connection
import java.sql.DriverManager

object DataBase {
    private var connection: Connection? = null

    private fun connectToDataBase():Connection?{
        return try {
            DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/abacBlockchain",
                "postgres",
                "SarahSada"
            )
        }catch (e:Exception){
            println("Connection Failed")
            null
        }
    }
    private fun closeDatabase(){
        connection?.close()
        connection = null
    }
    fun addBlockToDatabase(block:Block):Boolean{
        connection = connectToDataBase()
        connection?.let {
            return try {
                val result = it.prepareStatement(
                    "INSERT INTO blockchain (previoushash, hashvalue, dataabac) VALUES (" +
                            "'${block.getPreviousHash()}'," +
                            "'${block.getHash()}'," +
                            "'${block.getDataString()}'" +
                            ")"
                ).execute()
                closeDatabase()
                true
            }catch (e:Exception) {
                closeDatabase()
                false
            }
        }
        closeDatabase()
        return false
    }

    fun countRows():Int{
        connection = connectToDataBase()
        connection?.let {
            val result = it.prepareStatement("" +
                    "SELECT count(*) FROM blockchain").executeQuery()
            connection!!.close()
            return if(result.next())
                result.getInt("count")
            else
                0
        }
        connection?.close()
        return 0
    }

    fun lastHashValue():String?{
        connection = connectToDataBase()
        connection?.let {
            val result = it.prepareStatement("" +
                    "SELECT hashvalue FROM Blockchain ORDER BY id DESC LIMIT 1").executeQuery()
            connection!!.close()
            return if (result.next())
                result.getString("hashvalue")
            else
                null
        }
        connection?.close()
        return null
    }
    fun deleteRecord(hashvalue:String):Int{
        connection = connectToDataBase()
        connection?.let {
            it.prepareStatement(
                "DELETE FROM blockchain WHERE hashvalue = '${hashvalue}'"
            ).execute()
            val resultSet = it.createStatement().executeQuery("SELECT * FROM blockchain ORDER BY id")
            connection!!.close()
            var index = 0
            while(resultSet.next() && index != countRows()-1){
                if(resultSet.getString("previoushash") == hashvalue)
                    return index
                index++
            }
            return -1
        }
        connection?.close()
        return -1
    }
    fun updatePreviousHash(oldHash:String,newHash:String){
        connection = connectToDataBase()
        connection?.let {
            it.prepareStatement(
                "UPDATE blockchain SET previoushash = '${newHash}' WHERE previoushash = '${oldHash}'"
            ).execute()
        }
        connection?.close()
    }
    fun getPreviousHash(index:Int):String?{
        connection = connectToDataBase()
        connection?.let {
            val resultSet = it.prepareStatement(
                "SELECT previoushash FROM blockchain ORDER BY id"
            ).executeQuery()
            connection!!.close()
            var i:Int = 0
            while(resultSet.next() && i != index) {
                i++
            }
            if(i==index && i != countRows()){
                return resultSet.getString("previoushash");
            }
            return null
        }
        connection?.close()
        return null
    }
    fun getHashValue(index:Int):String?{
        connection = connectToDataBase()
        connection?.let {
            val resultSet = it.prepareStatement(
                "SELECT hashvalue FROM blockchain ORDER By id"
            ).executeQuery()
            connection!!.close()
            var i:Int = 0
            while(resultSet.next() && i != index) {
                i++
            }
            if(i==index && i != countRows()){
                return resultSet.getString("hashvalue");
            }
            return null
        }
        connection?.close()
        return null
    }
}