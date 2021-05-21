package br.com.zup.edu

import br.com.zup.edu.chavepix.*
import com.google.protobuf.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun SearchKeyResponse.paraConsultarChaveInternaResponse(): ConsultarChaveInternaResponse {
    val conta = ContaBancariaResponse(
            nomeInstituicao = this.conta.nomeInstituicao,
            numeroConta = this.conta.numeroConta,
            tipoDaConta = TipoDaConta.valueOf(this.conta.tipoConta.name)
    )
    return ConsultarChaveInternaResponse(
            nomePortador = this.nomePortador,
            idPortador = this.idPortador,
            id = this.idPix,
            chave = this.chave,
            cpfPortador = this.cpfPortador,
            tipoDaChave = TipoDaChave.valueOf(this.tipoChave.name),
            contaBancaria = conta,
            criadoEm = this.criadoEm.paraLocalDateTime()


    )
}
fun SearchKeyExternalResponse.paraConsultarChaveExternalResponse(): ConsultarChaveExternalResponse {
    val conta = ContaBancariaResponse(
            nomeInstituicao = this.conta.nomeInstituicao,
            numeroConta = this.conta.numeroConta,
            tipoDaConta = TipoDaConta.valueOf(this.conta.tipoConta.name)
    )
    return ConsultarChaveExternalResponse(
            nomePortador = this.nomePortador,
            chave = this.chave,
            cpfPortador = this.cpfPortador,
            tipoDaChave = TipoDaChave.valueOf(this.tipoChave.name),
            contaBancaria = conta,
            criadoEm = this.criadoEm.paraLocalDateTime()
    )

}

fun Timestamp.paraLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this.seconds, this.nanos, ZoneOffset.UTC)
}
fun paraSearchKeyRequest(idPortador:String, idChave: String):SearchKeyRequest{
    return SearchKeyRequest.newBuilder()
            .setIdPortador(idPortador)
            .setPixId(idChave)
            .build()
}

fun paraSearchKeyExternalRequest(chave: String):SearchKeyExternalRequest{
    return SearchKeyExternalRequest.newBuilder().setChave(chave).build()
}