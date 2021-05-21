package br.com.zup.edu.grpcclient

import br.com.zup.edu.PixKeyManagerGRpcServiceGrpc
import br.com.zup.edu.RemoveKeyManagerGrpcServiceGrpc
import br.com.zup.edu.SearchPixKeyManagerGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {
    @Singleton
    fun registerStub(@GrpcChannel("keymanager") channel: ManagedChannel): PixKeyManagerGRpcServiceGrpc.PixKeyManagerGRpcServiceBlockingStub {
        return PixKeyManagerGRpcServiceGrpc.newBlockingStub(channel)
    }
    @Singleton
    fun removeStub(@GrpcChannel("keymanager")channel: ManagedChannel):RemoveKeyManagerGrpcServiceGrpc.RemoveKeyManagerGrpcServiceBlockingStub {
        return RemoveKeyManagerGrpcServiceGrpc.newBlockingStub(channel)
    }
    @Singleton
    fun findStub(@GrpcChannel("keymanager")channel: ManagedChannel):SearchPixKeyManagerGrpcServiceGrpc.SearchPixKeyManagerGrpcServiceBlockingStub {
        return SearchPixKeyManagerGrpcServiceGrpc.newBlockingStub(channel)
    }
}