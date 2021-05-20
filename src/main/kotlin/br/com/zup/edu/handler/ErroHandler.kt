package br.com.zup.edu.handler

import io.micronaut.aop.Around

/*
    Esta anotacao delimita qual classe esta sendo ouvida para tratar suas exceções
 */
@Target(AnnotationTarget.CLASS,AnnotationTarget.FIELD,AnnotationTarget.TYPE)
@Around
@MustBeDocumented
annotation class ErroHandler
