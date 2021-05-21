package br.com.zup.edu.chavepix

import br.com.zup.edu.PixKeyRequest
import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidaChave
data class ChaveRequest(
        @field:NotBlank @field:Size(max = 77) val chave: String,
        @field:NotNull val tipoChave: TipoDaChave,
        @field:NotNull val tipoConta: TipoDaConta,
        @field:NotNull @field:isUUID val idPortador: String,
){
    fun paraPixKeyRegister():PixKeyRequest{
        return PixKeyRequest.newBuilder()
                .setChave(chave)
                .setConta(TipoConta.valueOf(tipoConta.name))
                .setTipo(TipoChave.valueOf(tipoChave.name))
                .setIdPortador(idPortador)
                .build()
    }
}

