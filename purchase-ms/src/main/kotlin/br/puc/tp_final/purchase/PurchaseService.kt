package br.puc.tp_final.purchase

import org.springframework.stereotype.Service
import java.util.*
import br.puc.tp_final.purchase.model.Product;
import br.puc.tp_final.purchase.model.Response;
import br.puc.tp_final.purchase.model.StatusRetorno;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class PurchaseService {

    val log: Logger = LoggerFactory.getLogger(PurchaseService::class.java)

    fun buy(product: Product): Response {
        log.info("Inicio do processo de compra")

        try {
            PublishRabbit()

            if (ValidateStock()) {
                log.info("Estoque validado com sucesso id ", product.id)

                if (ValidatePay()) {
                    log.info("Pagamento validado com sucesso id ", product.id)
                    return Response(StatusRetorno.Success.value() , "Compra confirmada", UUID.randomUUID())
                } else {
                    log.info("Erro ao validar pagamento id ", product.id)

                   return  Response(StatusRetorno.ClientError.value() , "Pagamento", UUID.randomUUID())
                }
            } else {
                log.info("Erro ao validar estoque id ", product.id)
                return Response(StatusRetorno.ServerError.value() , "Estoque", UUID.randomUUID())
            }
        } catch (ex: Exception) {
            log.info("Erro: ", ex.message)
            return Response(StatusRetorno.ClientError.value() , "Pagamento", UUID.randomUUID())
        }
    }

    fun ValidateStock(): Boolean = true

    fun PublishRabbit(): Boolean = true

    fun ValidatePay(): Boolean = true

}