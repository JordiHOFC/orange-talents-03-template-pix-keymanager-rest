package br.com.zup.edu.chavepix

import br.com.zup.edu.*
import com.google.protobuf.Timestamp
import io.grpc.Status
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
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultarChaveControllerTest {
    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcClient: SearchPixKeyManagerGrpcServiceGrpc.SearchPixKeyManagerGrpcServiceBlockingStub


    val pixID = UUID.randomUUID().toString()
    val idPortador = UUID.randomUUID().toString()



    @BeforeEach
    fun resetBeffore() {
        reset(grpcClient)
    }

    @AfterEach
    fun resetAfter() {
        reset(grpcClient)
    }


    /*
    * TESTES PARA CONSULTA INTERNA
    *
    * */
    @Test
    internal fun `nao deve buscar chave para id pix e do portador nulo`() {
        val portador: String? = null
        val idpix: String? = null

        given(grpcClient.consultarChaveKeyManager(any())).willThrow(Status.INVALID_ARGUMENT.asRuntimeException())

        val e = assertThrows<HttpClientResponseException> {
            val uri = "/api/v1/keys/$portador/$idpix"
            val request = HttpRequest.GET<ConsultarChaveInternaResponse>(uri)
            val response = httpClient.toBlocking().retrieve(request, ConsultarChaveInternaResponse::class.java)
        }
        with(e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }

    }

    @Test
    internal fun `nao deve buscar chave  inexistente`() {

        given(grpcClient.consultarChaveKeyManager(any())).willThrow(Status.NOT_FOUND.asRuntimeException())

        val e = assertThrows<HttpClientResponseException> {
            val uri = "/api/v1/keys/$idPortador/$pixID"
            val request = HttpRequest.GET<ConsultarChaveInternaResponse>(uri)
            val response = httpClient.toBlocking().retrieve(request, ConsultarChaveInternaResponse::class.java)
        }
        with(e) {
            assertEquals(HttpStatus.NOT_FOUND, e.status)
        }

    }

    @Test
    internal fun `deve buscar chave existente`() {
        val conta = ContaBancaria.newBuilder().setTipoConta(TipoConta.CONTA_CORRENTE).setNumeroConta("123456").setNomeInstituicao("ITAU").build()
        val chaveResponse = SearchKeyResponse
                .newBuilder()
                .setConta(conta)
                .setCriadoEm(Timestamp.getDefaultInstance())
                .setIdPix(pixID)
                .setNomePortador("Jordi H")
                .setCpfPortador("12312312312")
                .setIdPortador(idPortador)
                .setTipoChave(TipoChave.CPF)
                .setChave("12312312312").build()
        given(grpcClient.consultarChaveKeyManager(any())).willReturn(chaveResponse)

        val uri = "/api/v1/keys/$idPortador/$pixID"
        val request = HttpRequest.GET<ConsultarChaveInternaResponse>(uri)
        val response = httpClient.toBlocking().retrieve(request, ConsultarChaveInternaResponse::class.java)

        with(response) {
            assertEquals(chaveResponse.paraConsultarChaveInternaResponse(), response)
        }

    }

    /*
    * TESTES PARA CONSULTA EXTERNA
    *
    * */
    @Test
    internal fun `nao deve buscar para chave nula`() {
        val chave: String? = null

        given(grpcClient.consultaChaveExternal(any())).willThrow(Status.INVALID_ARGUMENT.asRuntimeException())

        val e = assertThrows<HttpClientResponseException> {
            val uri = "/api/v1/keys/$chave"
            val request = HttpRequest.GET<ConsultarChaveExternalResponse>(uri)
            val response = httpClient.toBlocking().retrieve(request, ConsultarChaveExternalResponse::class.java)
        }
        with(e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }

    }

    @Test
    internal fun `nao deve retornar chave inexistente`() {
        val chave="jordi@mail.com"
        given(grpcClient.consultaChaveExternal(any())).willThrow(Status.NOT_FOUND.asRuntimeException())

        val e = assertThrows<HttpClientResponseException> {
            val uri = "/api/v1/keys/$chave"
            val request = HttpRequest.GET<ConsultarChaveExternalResponse>(uri)
            val response = httpClient.toBlocking().retrieve(request, ConsultarChaveExternalResponse::class.java)
        }
        with(e) {
            assertEquals(HttpStatus.NOT_FOUND, e.status)
        }

    }

    @Test
    internal fun `deve retornar chave existente`() {
        val chave="jordi@mail.com"

        val conta = ContaBancaria.newBuilder().setTipoConta(TipoConta.CONTA_CORRENTE).setNumeroConta("123456").setNomeInstituicao("ITAU").build()

        val chaveResponse = SearchKeyExternalResponse
                .newBuilder()
                .setConta(conta)
                .setCriadoEm(Timestamp.getDefaultInstance())
                .setNomePortador("Jordi H")
                .setCpfPortador("12312312312")
                .setTipoChave(TipoChave.CPF)
                .setChave("12312312312").build()
        given(grpcClient.consultaChaveExternal(any())).willReturn(chaveResponse)

        val uri = "/api/v1/keys/$chave"
        val request = HttpRequest.GET<ConsultarChaveExternalResponse>(uri)
        val response = httpClient.toBlocking().retrieve(request, ConsultarChaveExternalResponse::class.java)

        with(response) {
            assertEquals(chaveResponse.paraConsultarChaveExternalResponse(), response)
        }

    }

    /*
   * TESTES PARA LISTAGEM DE CHAVES
   *
   * */
    @Singleton
    @Replaces(bean = SearchPixKeyManagerGrpcServiceGrpc.SearchPixKeyManagerGrpcServiceBlockingStub::class)
    fun blockingStub() = mock(SearchPixKeyManagerGrpcServiceGrpc.SearchPixKeyManagerGrpcServiceBlockingStub::class.java)
}