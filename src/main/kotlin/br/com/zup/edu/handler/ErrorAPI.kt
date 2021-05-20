package br.com.zup.edu.handler

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory

data class ErrorAPI(

        @field:JsonProperty  val erro:String
) {


}
