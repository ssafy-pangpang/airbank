package com.example.myapplication.model

data class LoanResponse (
    val code: String,
    val message: String,
    val data: Data
){

    data class Data(
        val amount: Int,
        val loanLimit: Int
    )
}

data class LoanStartRequest(
    val amount: Int
)

data class LoanStartResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val amount: Int
    )
}

data class LoanRepaymentRequest(
    val amount: Int
)

data class LoanRepaymentResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val amount: Int,
        val loanBalance: Int
    )
}


data class LoanChargeRequest(
    val amount: Int
)

data class LoanChargeResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val id: Int,
        val amount: Int,
        val apiCreateAt : String,
        val transactionType : String,
        val transactionDistinction : String,
        val transactionPartner : String
    )
}

