package abac_blockchain.abac

import abac_blockchain.attributes.AttributeEnvironment
import abac_blockchain.attributes.AttributeObject
import abac_blockchain.attributes.AttributePermission
import abac_blockchain.attributes.AttributeSubject
import java.io.IOException



class ABACPolicy(
    abacp: ABACP
    ) {
    private lateinit var attributeSubject:AttributeSubject
    private lateinit var attributeObject:AttributeObject
    private lateinit var attributeEnvironment: AttributeEnvironment
    private lateinit var attributePermission: AttributePermission

    init {
        ABACAlgorithm.checkPolicy(abacp)
            ?.let {
                it.apply {
                    attributeSubject = _AttributeSubject!!
                    attributeObject = _AttributeObject!!
                    attributePermission = _AttributePermission!!
                    attributeEnvironment = _AttributeEnvironment!!
                }
            } ?: throw IOException()
    }

    fun getAS():AttributeSubject = attributeSubject

    fun getAO():AttributeObject = attributeObject

    fun getAP():AttributePermission = attributePermission

    fun getAE():AttributeEnvironment = attributeEnvironment

}