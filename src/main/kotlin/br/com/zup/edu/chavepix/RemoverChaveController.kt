package br.com.zup.edu.chavepix

import br.com.zup.edu.RemoveKeyManagerGrpcServiceGrpc
import br.com.zup.edu.handler.ControllerGRPC
import br.com.zup.edu.handler.ErroHandler
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.NO_CONTENT
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Controller("/api/v1/keys")
@ErroHandler
@Validated
class RemoverChaveController(
        @Inject private val grpcClient: RemoveKeyManagerGrpcServiceGrpc.RemoveKeyManagerGrpcServiceBlockingStub,
):ControllerGRPC {

    @Delete
    fun deletarChave(@Valid @Body removerChaveRequest: RemoverChaveRequest):HttpResponse<*>{
        grpcClient.removerChave(removerChaveRequest.paraRemoveKeyRequest())
        return HttpResponse.status<Unit>(NO_CONTENT)
    }

}
