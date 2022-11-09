package abac_blockchain.abac

import abac_blockchain.attributes.AttributeEnvironment
import abac_blockchain.attributes.AttributeObject
import abac_blockchain.attributes.AttributePermission
import abac_blockchain.attributes.AttributeSubject

data class ABACP(
    val _AttributeSubject: AttributeSubject?,
    val _AttributeObject: AttributeObject?,
    val _AttributePermission: AttributePermission?,
    val _AttributeEnvironment: AttributeEnvironment?
)
