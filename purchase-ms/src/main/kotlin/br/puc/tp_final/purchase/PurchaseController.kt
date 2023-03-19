package br.puc.tp_final.purchase

import org.springframework.web.bind.annotation.*
import java.util.*
import br.puc.tp_final.purchase.model.Product;
import br.puc.tp_final.purchase.model.Response;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/purchase-ms/rest/purchase")
class PurchaseController(
    val purchaseService: PurchaseService
) {
    val log: Logger = LoggerFactory.getLogger(PurchaseController::class.java)

    @PostMapping("buy")
    fun buy(@RequestBody product: Product): Response {

        log.info("Iniciando uma tentativa de compra, produto({}) ", product)

        val result = purchaseService.buy(product)

        log.info("tentativa de compra do produto({}), status({}):", product.id, result.statusCode)

        return result
    }
}