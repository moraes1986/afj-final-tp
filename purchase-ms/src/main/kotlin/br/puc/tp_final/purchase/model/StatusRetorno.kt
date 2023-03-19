package br.puc.tp_final.purchase.model

enum class StatusRetorno {
    Success {
        override fun value() = 200
    },
    ClientError {
        override fun value() = 424
    },
    ServerError {
        override fun value() = 500
    };

    abstract fun value(): Int
}
