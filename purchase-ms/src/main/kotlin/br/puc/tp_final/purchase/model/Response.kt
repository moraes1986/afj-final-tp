package br.puc.tp_final.purchase.model

import java.util.*
data class Response (
    val statusCode: Int,
    val friendlyMessage: String,
    val uuid: UUID
) {
    class Builder {
        var statusCode: Int = 0
        var friendlyMessage: String = ""
        var uuid: Int = 0

        fun build(): Response {
            return Response(statusCode, friendlyMessage, UUID.randomUUID())
        }
    }
}