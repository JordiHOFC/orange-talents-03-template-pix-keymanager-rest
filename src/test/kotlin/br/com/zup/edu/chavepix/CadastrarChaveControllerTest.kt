package br.com.zup.edu.chavepix

import br.com.zup.edu.PixKeyManagerGRpcServiceGrpc
import br.com.zup.edu.PixKeyResponse
import br.com.zup.edu.grpcclient.GrpcClientFactory
import br.com.zup.edu.handler.ErrorAPI
import io.grpc.Status
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false, startApplication = true)
internal class CadastrarChaveControllerTest {
    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcClient: PixKeyManagerGRpcServiceGrpc.PixKeyManagerGRpcServiceBlockingStub

    @BeforeEach
    fun restBefore() {
        Mockito.reset(grpcClient)
    }
    @AfterEach
    fun restAffter() {
        Mockito.reset(grpcClient)
    }
    @Test
    internal fun `nao deve cadastrar chave para tipo de chave e id portador invalido`() {
        val pixID = UUID.randomUUID().toString()
        val chaveRequest = ChaveRequest(
                chave = "13460466921",
                tipoChave = TipoDaChave.CPF,
                tipoConta = TipoDaConta.CONTA_POUPANCA,
                idPortador = "123143253"
        )
        given(grpcClient.cadastrarChave(Mockito.any())).willReturn(null)
        val request = HttpRequest.POST("/api/v1/keys", chaveRequest)
        val e = assertThrows<HttpClientResponseException> {
            val response = httpClient.toBlocking().exchange(request, ChaveRequest::class.java)

        }
        with(e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)

        }
    }

    @Test
    internal fun `nao deve cadastrar chave com tamanho invalido`() {
        val pixID = UUID.randomUUID().toString()
        val chaveRequest = ChaveRequest(
                chave = "134602937219873921730921093812098309128309182039218093812098390466921",
                tipoChave = TipoDaChave.CPF,
                tipoConta = TipoDaConta.CONTA_POUPANCA,
                idPortador = pixID
        )

        given(grpcClient.cadastrarChave(Mockito.any())).willReturn(null)

        val request = HttpRequest.POST("/api/v1/keys", chaveRequest)
        val e = assertThrows<HttpClientResponseException> {
            val response = httpClient.toBlocking().exchange(request, ChaveRequest::class.java)

        }
        with(e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)

        }


    }

    @Test
    internal fun `deve cadastrar chave para request valida`() {
        val pixID = UUID.randomUUID().toString()
        val chaveRequest = ChaveRequest(
                chave = "13460466921",
                tipoChave = TipoDaChave.CPF,
                tipoConta = TipoDaConta.CONTA_POUPANCA,
                idPortador = UUID.randomUUID().toString()
        )
        val keyResponse = PixKeyResponse.newBuilder().setIdPix(pixID).build()

        given(grpcClient.cadastrarChave(Mockito.any())).willReturn(keyResponse)

        val request = HttpRequest.POST("/api/v1/keys", chaveRequest)
        val response = httpClient.toBlocking().exchange(request, ChaveRequest::class.java)
        with(response) {
            assertEquals(HttpStatus.CREATED, response.status)

        }
    }

    @Test
    internal fun ` deve cobrir exceptions do status already exists`() {
        val pixID = UUID.randomUUID().toString()
        val chaveRequest = ChaveRequest(
                chave = "13460466921",
                tipoChave = TipoDaChave.CPF,
                tipoConta = TipoDaConta.CONTA_POUPANCA,
                idPortador = UUID.randomUUID().toString()
        )
        val keyResponse = PixKeyResponse.newBuilder().setIdPix(pixID).build()

        val e = assertThrows<HttpClientResponseException> {

            given(grpcClient.cadastrarChave(Mockito.any())).willThrow(Status.ALREADY_EXISTS.withDescription("Ja existe").asRuntimeException())
            val request = HttpRequest.POST("/api/v1/keys", chaveRequest)
            val response = httpClient.toBlocking().exchange(request, ErrorAPI::class.java)
        }


    }
    @Test
    internal fun `deve cobrir exceptions do status not found`() {
        val pixID = UUID.randomUUID().toString()
        val chaveRequest = ChaveRequest(
                chave = "13460466921",
                tipoChave = TipoDaChave.CPF,
                tipoConta = TipoDaConta.CONTA_POUPANCA,
                idPortador = UUID.randomUUID().toString()
        )
        val keyResponse = PixKeyResponse.newBuilder().setIdPix(pixID).build()
        given(grpcClient.cadastrarChave(Mockito.any())).willThrow(Status.NOT_FOUND.withDescription("Ja existe").asRuntimeException())

        val e = assertThrows<HttpClientResponseException> {

            val request = HttpRequest.POST("/api/v1/keys", chaveRequest)
            val response = httpClient.toBlocking().retrieve(request, ErrorAPI::class.java)
        }

    }
    @Test
    internal fun `deve cobrir exceptions que nao sejam already exists e notfound`() {
        val pixID = UUID.randomUUID().toString()
        val chaveRequest = ChaveRequest(
                chave = "13460466921",
                tipoChave = TipoDaChave.CPF,
                tipoConta = TipoDaConta.CONTA_POUPANCA,
                idPortador = UUID.randomUUID().toString()
        )
        val keyResponse = PixKeyResponse.newBuilder().setIdPix(pixID).build()
        given(grpcClient.cadastrarChave(Mockito.any())).willThrow(Status.FAILED_PRECONDITION.withDescription("Ja existe").asRuntimeException())

        val e = assertThrows<HttpClientResponseException> {

            val request = HttpRequest.POST("/api/v1/keys", chaveRequest)
            val response = httpClient.toBlocking().retrieve(request, ErrorAPI::class.java)
        }


    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class MockStubFactory {
        @Singleton
        @Replaces(bean = PixKeyManagerGRpcServiceGrpc.PixKeyManagerGRpcServiceBlockingStub::class)
        fun blockingStub() = Mockito.mock(PixKeyManagerGRpcServiceGrpc.PixKeyManagerGRpcServiceBlockingStub::class.java)
    }
}