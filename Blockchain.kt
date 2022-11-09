package abac_blockchain.blockchain

import abac_blockchain.abac.ABACPolicy
import abac_blockchain.attributes.AttributeObject
import abac_blockchain.attributes.AttributeSubject
import abac_blockchain.database.DataBase
import abac_blockchain.encryption.SHA256

object Blockchain {

    fun addBlock(
        abacp: ABACPolicy
    ):Boolean{
        val generateHash = SHA256().encrypt(
                abacp.getAS().getUserId()
                    + abacp.getAS().getRole()
                    + abacp.getAS().getGroup()
                    + abacp.getAO().getDeviceId()
        )!!
        val block = Block(
            if (DataBase.countRows() == 0)
                "0".repeat(64)
            else
                DataBase.lastHashValue()!!,
            generateHash,
            abacp
        )
        return DataBase.addBlockToDatabase(block)
    }

    fun deleteBlock(
        attributeSubject: AttributeSubject,
        attributeObject:AttributeObject
    ):Boolean{
        val generateHash = SHA256().encrypt(
                attributeSubject.getUserId()
                    + attributeSubject.getRole()
                    + attributeSubject.getGroup()
                    + attributeObject.getDeviceId()
        )!!
        val index  = DataBase.deleteRecord(generateHash)
        when (index){
            0 -> {
                if(DataBase.countRows() != 0){
                    DataBase.updatePreviousHash(
                        generateHash,
                        "0".repeat(64)
                    )
                }
            }
            in 1 until  DataBase.countRows() ->{
                DataBase.updatePreviousHash(
                    DataBase.getPreviousHash(index)!!,
                    DataBase.getHashValue(index-1)!!
                )
            }
            else ->{
                if (DataBase.countRows() == 1){
                    DataBase.updatePreviousHash(
                        DataBase.getPreviousHash(0)!!,
                        "0".repeat(64)
                    )
                }
            }
        }
        return true
    }
}