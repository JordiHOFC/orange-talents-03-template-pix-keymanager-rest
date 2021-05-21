package br.com.zup.edu.chavepix

import br.com.zup.edu.RemoveKeyRequest
import br.com.zup.edu.SearchKeyRequest
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class RemoveChaveRequest(@NotBlank @isUUID val idPortador:String, @isUUID @NotBlank val idChave:String ){

    fun paraRemoveKeyRequest():RemoveKeyRequest{
        return RemoveKeyRequest.newBuilder()
                .setIdPortador(this.idPortador)
                .setPixId(idChave)
                .build()
    }

}
