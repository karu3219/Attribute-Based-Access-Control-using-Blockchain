package abac_blockchain.blockchain

import abac_blockchain.abac.ABACPolicy

data class Block(
    private var previousHash:String,
    private val hash:String,
    private val data:ABACPolicy
    ){

    fun getPreviousHash():String = previousHash

    fun getHash():String = hash

    fun setPreviousHash(newHash:String){
        previousHash = newHash
    }

    fun getDataString() = data.toString()
}
