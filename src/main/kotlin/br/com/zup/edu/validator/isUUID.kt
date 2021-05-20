package br.com.zup.edu.chavepix

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.validation.Constraint
import kotlin.reflect.KClass



@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [isUUIDValidator::class])
annotation class isUUID(
        val message: String = "Este Identificador nao corresponde ao um UUID",
        val groups: Array<KClass<Any>> = [],
        val payload: Array<KClass<Any>> = [],
)

class isUUIDValidator : ConstraintValidator<isUUID, String> {
    override fun isValid(
            value: String?,
            annotationMetadata: AnnotationValue<isUUID>,
            context: ConstraintValidatorContext,
    ): Boolean {
        if (value==null){
            return false
        }
        return value.matches("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}".toRegex())
    }

}


