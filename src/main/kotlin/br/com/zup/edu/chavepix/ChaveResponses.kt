package br.com.zup.edu.chavepix

import br.com.zup.edu.ContaBancaria
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ConsultarChaveInternaResponse(
        val tipoDaChave: TipoDaChave,
        val chave:String,
        val id:String,
        val idPortador: String,
        val contaBancaria: ContaBancariaResponse,
        @field:JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") val criadoEm: LocalDateTime,
        val nomePortador: String,
        val cpfPortador: String
)

data class ContaBancariaResponse(
        val nomeInstituicao:String,
        val numeroConta:String,
        val tipoDaConta: TipoDaConta
)
data class ConsultarChaveExternalResponse(
        val tipoDaChave: TipoDaChave,
        val chave:String,
        val contaBancaria: ContaBancariaResponse,
        @field:JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") val criadoEm: LocalDateTime,
        val nomePortador: String,
        val cpfPortador: String
)

data class ChaveResponse(
        val chave:String,
        val idPortador: String,
        val id:String,
        val tipoDaChave: TipoDaChave,
        val tipoDaConta: TipoDaConta,
        @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") val criadoEm: LocalDateTime
)
