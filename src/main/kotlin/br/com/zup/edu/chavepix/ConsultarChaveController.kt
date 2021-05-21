package br.com.zup.edu.chavepix

import br.com.zup.edu.*
import br.com.zup.edu.handler.ControllerGRPC
import br.com.zup.edu.handler.ErroHandler
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Controller("/api/v1/keys")
@ErroHandler
@Validated
class ConsultarChaveController(
        @Inject private val grpcClient:SearchPixKeyManagerGrpcServiceGrpc.SearchPixKeyManagerGrpcServiceBlockingStub
): ControllerGRPC {

    @Get("/{idPortador}/{idChave}")
    fun buscarChaveInterno(@PathVariable idPortador:String,@PathVariable idChave:String):HttpResponse<*>{
        val responseGrpc = grpcClient.consultarChaveKeyManager(paraSearchKeyRequest(idPortador, idChave))
        val response = responseGrpc.paraConsultarChaveInternaResponse()
        return HttpResponse.ok(response)
    }
    @Get("/{chave}")
    fun buscarChaveExternal(@PathVariable chave:String):HttpResponse<*>{
        val responseGrpc = grpcClient.consultaChaveExternal(paraSearchKeyExternalRequest(chave))
        val response = responseGrpc.paraConsultarChaveExternalResponse()
        return HttpResponse.ok(response)
    }
}

