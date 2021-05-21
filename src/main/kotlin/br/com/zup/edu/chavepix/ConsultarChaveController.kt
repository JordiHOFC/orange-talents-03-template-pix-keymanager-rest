package br.com.zup.edu.chavepix

import br.com.zup.edu.*
import br.com.zup.edu.handler.ControllerGRPC
import br.com.zup.edu.handler.ErroHandler
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import javax.inject.Inject

@Controller
@ErroHandler
@Validated
class ConsultarChaveController(
        @Inject private val grpcClient: SearchPixKeyManagerGrpcServiceGrpc.SearchPixKeyManagerGrpcServiceBlockingStub,
) : ControllerGRPC {

    @Get("/api/v1/{idPortador}/keys/{idChave}")
    fun buscarChaveInterno(@PathVariable idPortador: String, @PathVariable idChave: String): HttpResponse<*> {
        val responseGrpc = grpcClient.consultarChaveKeyManager(paraSearchKeyRequest(idPortador, idChave))
        val response = responseGrpc.paraConsultarChaveInternaResponse()
        return HttpResponse.ok(response)
    }

    @Get("/api/v1/keys/{chave}")
    fun buscarChaveExternal(@PathVariable chave: String): HttpResponse<*> {
        val responseGrpc = grpcClient.consultaChaveExternal(paraSearchKeyExternalRequest(chave))
        val response = responseGrpc.paraConsultarChaveExternalResponse()
        return HttpResponse.ok(response)
    }

    @Get("/api/v1/{idPortador}/keys")
    fun listarChave(@PathVariable idPortador: String): HttpResponse<*> {
        val responseGrpc = grpcClient.listarTodasChavesDoPortador(paraSearchAllKeyBearer(idPortador))
        val response = responseGrpc.keysList.map { k->k.paraChaveResponse() }
        return HttpResponse.ok(response)
    }




}

