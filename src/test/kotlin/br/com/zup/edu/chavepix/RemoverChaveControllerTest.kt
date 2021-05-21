package br.com.zup.edu.chavepix

import br.com.zup.edu.RemoveKeyManagerGrpcServiceGrpc
import br.com.zup.edu.grpcclient.GrpcClientFactory
import io.grpc.Status
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.quality.Strictness
import org.mockito.session.MockitoSessionLogger
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoverChaveControllerTest {
    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcRClient: RemoveKeyManagerGrpcServiceGrpc.RemoveKeyManagerGrpcServiceBlockingStub


    @BeforeEach
    fun resetBeffore() {
        Mockito.reset(grpcRClient)
    }

    @AfterEach
    fun resetAfter() {
        Mockito.reset(grpcRClient)
    }


    @Test
    internal fun `nao deve remover  chave com pix id e id portador nulo`() {
        val removerChaveRequest = RemoverChaveRequest(
                idPortador = "",
                idChave = ""
        )
        //  given(grpcRClient.removerChave(Mockito.any())).willReturn(null)
        val request = HttpRequest.DELETE("/api/v1/keys", removerChaveRequest)
        val e = assertThrows<HttpClientResponseException> {
            val response = httpClient.toBlocking().exchange(request, ChaveRequest::class.java)

        }
        with(e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)

        }
    }

    @Test
    internal fun `nao deve remover  chave com pix id e id portador diferente de UUID`() {
        val removerChaveRequest = RemoverChaveRequest(
                idPortador = "assafsafs",
                idChave = "fsafsfafa"
        )
        // BDDMockito.given(grpcClient.removerChave(Mockito.any())).willReturn(null)
        val request = HttpRequest.DELETE("/api/v1/keys", removerChaveRequest)

        val e = assertThrows<HttpClientResponseException> {
            val response = httpClient.toBlocking().exchange(request, ChaveRequest::class.java)

        }
        with(e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)

        }
    }

    @Test
    internal fun `nao deve remover  chave inexistente com pix id e id portador nulo`() {
        val removerChaveRequest = RemoverChaveRequest(
                idPortador = UUID.randomUUID().toString(),
                idChave = UUID.randomUUID().toString()
        )
        given(grpcRClient.removerChave(any())).willThrow(Status.NOT_FOUND.asRuntimeException())
        val request = HttpRequest.DELETE("/api/v1/keys", removerChaveRequest)
        val e = assertThrows<HttpClientResponseException> {
            val response = httpClient.toBlocking().exchange(request, ChaveRequest::class.java)

        }
        with(e) {
            assertEquals(HttpStatus.NOT_FOUND, e.status)

        }
    }



    @Singleton
    @Replaces(bean = RemoveKeyManagerGrpcServiceGrpc.RemoveKeyManagerGrpcServiceBlockingStub::class)
    fun blockingStub() = Mockito.mock(RemoveKeyManagerGrpcServiceGrpc.RemoveKeyManagerGrpcServiceBlockingStub::class.java)

}