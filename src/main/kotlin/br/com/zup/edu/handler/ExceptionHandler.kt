package br.com.zup.edu.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
@InterceptorBean(ErroHandler::class)
class ExceptionHandler : MethodInterceptor<ControllerGRPC, HttpResponse<*>> {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)
    override fun intercept(context: MethodInvocationContext<ControllerGRPC, HttpResponse<*>>): HttpResponse<*>? {
        try {

            return context.proceed()
        } catch (e: StatusRuntimeException) {
            return when (e.status.code.toStatus().code) {
                Status.ALREADY_EXISTS.code -> {
                    LOGGER.info(e.message)
                    val indexOf = e.message!!.indexOf(":")
                    HttpResponse.status<ErrorAPI>(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorAPI(e.message!!.substring(indexOf + 2)))
                }
                Status.NOT_FOUND.code-> {
                    LOGGER.info(e.message)
                    val indexOf = e.message!!.indexOf(":")
                    HttpResponse.status<ErrorAPI>(HttpStatus.NOT_FOUND).body(ErrorAPI(e.message!!.substring(indexOf + 2)))
                }
                else-> {
                    LOGGER.info(e.message)
                    val indexOf = e.message!!.indexOf(":")
                    HttpResponse.status<ErrorAPI>(HttpStatus.BAD_REQUEST).body(ErrorAPI(e.message!!.substring(indexOf + 2)))
                }
            }
        }
    }
}