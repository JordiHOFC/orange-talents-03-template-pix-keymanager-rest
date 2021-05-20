package br.com.zup.edu.chavepix

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(FIELD, CONSTRUCTOR,TYPE, CLASS)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidaChaveValidator::class])
annotation class ValidaChave(
    val message: String = "Chave em formato invalido.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Any>> = []
)

class ValidaChaveValidator:ConstraintValidator<ValidaChave,ChaveRequest> {
    override fun isValid(
            value: ChaveRequest?,
            annotationMetadata: AnnotationValue<ValidaChave>,
            context: ConstraintValidatorContext,
    ): Boolean {
        if (value == null) {
            return false
        }
        return when (value.tipoChave) {
            TipoDaChave.CHAVEALEATORIA -> value.chave.isNotBlank()
            TipoDaChave.EMAIL -> value.chave.matches("^[A-Za-z0-9+_.-]+@(.+)\$".toRegex())
            TipoDaChave.TELEFONECELULAR -> value.chave.matches("\\+[1-9][0-9]\\d{1,14}".toRegex())
            TipoDaChave.CPF -> value.chave.matches("[0-9]{11}".toRegex())
        }
    }
}