package br.com.zup.edu.chavepix

import br.com.zup.edu.RemoveKeyRequest
import com.fasterxml.jackson.annotation.JsonProperty

data class RemoverChaveRequest(val idPortador:String, val idChave:String ){


    fun paraRemoveKeyRequest():RemoveKeyRequest{
        return RemoveKeyRequest.newBuilder()
                .setIdPortador(this.idPortador)
                .setPixId(idChave)
                .build()
    }
}
