package com.example.myapplication.model


data class AccountRegisterRequest(
    val bankCode: String,
    val accountNumber: Int
)

data class AccountRegisterResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val id: Int
    )
}

data class AccountHistoryCheckResponse(
    val code: String,
    val message: String,
    val data: Data
) {
    data class Data(
        val accountHistoryElements: List<Transaction>
    ) {
        data class Transaction(
            val id: Int,
            val amount: Int,
            val apiCreatedAt: String,
            val transactionType: String,
            val transactionDistinction: String,
            val transactionPartner: String
        )
    }
}

data class AccountCheckResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val amount: Int
    )
}

data class TaxResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val amount: Int,
        val overdueAmount: Int,
        val expiredAt : String
    )
}

data class TaxTransferRequest(
    val amount: Int
)

data class TaxTransferResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val id: Int,
        val amount: Int,
        val apiCreatedAt: String,
        val transactionType: String,
        val transactionDistinction: String,
        val transactionPartner: String
    )
}

data class BonusRequest(
    val amount: Int
)


data class BonusResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val amount: Int
    )
}

//이자 조회
data class InterestResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val amount: Int,
        val overdueAmount: Int,
        val expiredAt: String
    )
}

//이자상환
data class InterestRepaymentRequest(
    val amount:Int
)

data class InterestRepaymentResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val id: Int,
        val amount: Int,
        val apiCreatedAt: String,
        val transactionType: String,
        val transactionDistinction: String,
        val transactionPartner: String
    )
}
//압류조회
data class ConfiscationResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val amount: Int,
        val startedAt: String
    )
}

//변제금 송금
data class ConfiscationTransferRequest(
    val amount: Int
)

data class ConfiscationTransferResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val id: Int,
        val amount: Int,
        val apiCreatedAt: String,
        val transactionType: String,
        val transactionDistinction: String,
        val transactionPartner: String
    )
}




