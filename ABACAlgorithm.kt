package abac_blockchain.abac

import abac_blockchain.attributes.AttributeObject
import abac_blockchain.attributes.AttributeSubject
import abac_blockchain.blockchain.Blockchain

object ABACAlgorithm {

    fun checkPolicy(
        abacp: ABACP
    ): ABACP?{
        abacp.apply {
            _AttributeSubject?.let {
                if (it.getUserId() == null
                    && it.getRole() == null
                    && it.getGroup() == null
                )
                    return null
            } ?: return null

            _AttributeObject?.let {
                if (it.getDeviceId()==null)
                    return null
            } ?: return null

            _AttributeEnvironment?.let {
                if(it.getCreateTime() == null
                    && it.getEndTime() == null)
                    return null
            } ?: return null

            if(_AttributePermission == null)
                return null
        }
        return abacp
    }
    fun addPolicy(
        abacp: ABACP
    ):Boolean{
        val abacPolicy = ABACPolicy(abacp)
        if (!Blockchain.addBlock(abacPolicy))
            throw Error("Error While Adding New Block")
        return true
    }

    fun deletePolicy(
        attributeSubject:AttributeSubject,
        attributeObject:AttributeObject
    ):Boolean{
        if(!Blockchain.deleteBlock(attributeSubject,attributeObject))
            throw Error("Error While Deleting The Block")
        return true
    }
}