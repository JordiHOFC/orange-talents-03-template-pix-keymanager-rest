package br.com.zup.edu.chavepix

import br.com.zup.edu.RemoveKeyRequest
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class RemoverChaveRequest(@NotBlank @isUUID val idPortador:String, @isUUID @NotBlank val idChave:String ){

    fun paraRemoveKeyRequest():RemoveKeyRequest{
        return RemoveKeyRequest.newBuilder()
                .setIdPortador(this.idPortador)
                .setPixId(idChave)
                .build()
    }
}
