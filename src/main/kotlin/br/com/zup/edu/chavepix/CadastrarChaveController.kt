package br.com.zup.edu.chavepix

import br.com.zup.edu.PixKeyManagerGRpcServiceGrpc
import br.com.zup.edu.handler.ControllerGRPC
import br.com.zup.edu.handler.ErroHandler
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.*
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1")
@ErroHandler
class CadastrarChaveController(
        @Inject private  val grpcCLient: PixKeyManagerGRpcServiceGrpc.PixKeyManagerGRpcServiceBlockingStub,
):ControllerGRPC {
    @Post("/keys")
    fun registrarChave(@Body @Valid request:ChaveRequest):HttpResponse<*>{
        val requestGrpc = request.paraPixKeyRegister()
        val cadastrarChave = grpcCLient.cadastrarChave(requestGrpc)
        val location = UriBuilder.of("/keys/{id}").expand(mutableMapOf(Pair("id", cadastrarChave.idPix)))
        return HttpResponse.status<Unit>(CREATED)
    }

}