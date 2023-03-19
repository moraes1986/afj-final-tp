package br.puc.tp_final.purchase

import org.springframework.stereotype.Service
import java.util.*
import br.puc.tp_final.purchase.model.Product;
import br.puc.tp_final.purchase.model.Response;
import br.puc.tp_final.purchase.model.StatusRetorno;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection
import java.net.URL

@Service
class PurchaseService {

    val log: Logger = LoggerFactory.getLogger(PurchaseService::class.java)

    fun buy(product: Product): Response {

        product.uuid = UUID.randomUUID()
        log.info("Inicio do processo de compra com o uuid de intenção " + product.uuid)
        try {
            PublishQueue()

            // Valida se possui estoque após retorno do endpoint stock-ms

            if (ValidateStock(product.uuid)) {
                log.info("Estoque validado com sucesso id " + product.id + " name " + product.name)

                // Se possui esteoque, deve validar se foi pago
                if (ValidatePay()) {

                    // Se foi pago, confirma a compra
                    log.info("Pagamento validado com sucesso id "  + product.id + " name " + product.name)

                    // A clase StatusRetorno define o status code para cada tipo de retorno
                    return Response(StatusRetorno.Success.value() , "Compra confirmada", product.uuid)
                } else {

                    // Se não foi pago cancela a compra
                    log.info("Erro ao validar pagamento id "  + product.id + " name " + product.name)

                   return  Response(StatusRetorno.ClientError.value() , "Pagamento", product.uuid)
                }
            } else {
                // Se não possui estoque, interrompe a compra
                log.info("Erro ao validar estoque id " + product.id + " name " + product.name)
                return Response(StatusRetorno.ClientError.value() , "Estoque", product.uuid)
            }
        } catch (ex: Exception) {
            log.info("Erro: ", ex.message)
            return Response(StatusRetorno.ServerError.value() , "Erro inesperado ao processar compra!", product.uuid)
        }
    }

    // Método que valida se o produto possui estoque, deverá bater no endpont de estoque stock-ms
    fun ValidateStock(uuid: UUID?): Boolean {

        log.info("Entrando no processo de validação de estoque com uuid $uuid")
        // Alterar valor para false para validar todas apps rodando
        var validate = true
        try {
            if(uuid != null) {
                val url = URL("http://localhost:8080/stock-ms/rest/stock/status/$uuid")

                log.info("API de validação de estoque: $url")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"  // optional default is GET

                    log.info("Sent 'GET' request to URL : $url; Response Code : $responseCode")

                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->
                            println(line)
                        }
                    }
                    if(responseCode == 200){
                        validate = true
                    }
                }
            }
            return validate
        }catch (ex: Exception){
            log.info("Erro de validação de estoque: ", ex.message)
            return validate
        }

    }

    // Método que publica a intenção de compra na fila, no caso o ideal seria uma fila em RabbitMQ
    fun PublishQueue(): Boolean = true

    // Método que valida o pagamento, deverá bater no endpoint de pagamento payment-ms
    fun ValidatePay(): Boolean = true

}