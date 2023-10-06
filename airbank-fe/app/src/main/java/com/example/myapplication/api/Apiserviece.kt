package com.example.myapplication.api

import com.example.myapplication.model.AccountCheckResponse
import com.example.myapplication.model.AccountHistoryCheckResponse
import com.example.myapplication.model.AccountRegisterRequest
import com.example.myapplication.model.AccountRegisterResponse
import com.example.myapplication.model.BonusRequest
import com.example.myapplication.model.BonusResponse
import com.example.myapplication.model.BonusSavingsRequest
import com.example.myapplication.model.BonusSavingsResponse
import com.example.myapplication.model.CancelSavingsRequest
import com.example.myapplication.model.CancelSavingsResponse
import com.example.myapplication.model.ConfiscationResponse
import com.example.myapplication.model.ConfiscationTransferRequest
import com.example.myapplication.model.ConfiscationTransferResponse
import com.example.myapplication.model.CreateSavingsItemRequest
import com.example.myapplication.model.CreateSavingsItemResponse
import com.example.myapplication.model.InterestRepaymentRequest
import com.example.myapplication.model.InterestRepaymentResponse
import com.example.myapplication.model.InterestResponse
import com.example.myapplication.model.LoanChargeRequest
import com.example.myapplication.model.LoanChargeResponse
import com.example.myapplication.model.LoanRepaymentRequest
import com.example.myapplication.model.LoanRepaymentResponse
import com.example.myapplication.model.LoanResponse
import com.example.myapplication.model.LoanStartRequest
import com.example.myapplication.model.LoanStartResponse
import com.example.myapplication.model.NotificationResponse
import com.example.myapplication.model.SavingsRemitRequest
import com.example.myapplication.model.SavingsRemitResponse
import com.example.myapplication.model.SavingsResponse
import com.example.myapplication.model.TaxResponse
import com.example.myapplication.model.TaxTransferRequest
import com.example.myapplication.model.TaxTransferResponse
import com.example.myapplication.model.UpdateSavingsRequest
import com.example.myapplication.model.UpdateSavingsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {
    @GET("/savings/current")
    suspend fun getSavings(@Query("group_id") groupId: Int): Response<SavingsResponse>

    @POST("/savings/item")
    suspend fun createSavingsItem(@Body request: CreateSavingsItemRequest): Response<CreateSavingsItemResponse>
    @PATCH("/savings/confirm")
    suspend fun updateSavings(@Query("group_id") groupId: Int, @Body request: UpdateSavingsRequest): Response<UpdateSavingsResponse>
    @PATCH("/savings/cancel")
    suspend fun cancelSavings(@Body request: CancelSavingsRequest) : Response<CancelSavingsResponse>

    @POST("/savings")
    suspend fun remitSavings(@Body request : SavingsRemitRequest) : Response<SavingsRemitResponse>

    @POST("/savings/reward")
    suspend fun bonusSavings(@Query("group_id") groupId: Int,@Body request: BonusSavingsRequest) : Response<BonusSavingsResponse>

    @GET("/loans")
    suspend fun getLoan(@Query("group_id") groupId: Int): Response<LoanResponse>

    @POST("/accounts")
    suspend fun accountRegister(@Body request: AccountRegisterRequest) : Response<AccountRegisterResponse>

    //계좌 내역 조회
    @GET("/accounts/history")
    suspend fun accountHistory(@Query("account_type") accountType: String): Response<AccountHistoryCheckResponse>

    //계좌 잔액 조회
    @GET("/accounts")
    suspend fun accountCheck(): Response<AccountCheckResponse>

    @GET("/funds/tax")
    suspend fun taxCheck(@Query("group_id") groupId: Int): Response<TaxResponse>

    @POST("/funds/tax")
    suspend fun taxTransfer(@Body request: TaxTransferRequest) : Response<TaxTransferResponse>

    @POST("/funds/bonus")
    suspend fun bonusTransfer(@Query("group_id") groupId: Int,@Body request: BonusRequest): Response<BonusResponse>


    @GET("/funds/interest")
    suspend fun interestCheck(@Query("group_id") groupId: Int): Response<InterestResponse>

    @POST("/funds/interest")
    suspend fun interestRepayment(@Body request: InterestRepaymentRequest): Response<InterestRepaymentResponse>

    @GET("/funds/confiscation")
    suspend fun confiscationCheck(@Query("group_id") groupId: Int): Response<ConfiscationResponse>

    @POST("/funds/confiscation")
    suspend fun confiscationTransfer(@Body request: ConfiscationTransferRequest): Response<ConfiscationTransferResponse>



    @POST("/loans")
    suspend fun loanStart(@Body request: LoanStartRequest): Response<LoanStartResponse>


    @POST("/loans/repaid")
    suspend fun loanRepayment(@Body request: LoanRepaymentRequest): Response<LoanRepaymentResponse>

    @POST("/loans/charge")
    suspend fun loanCharge(@Query("group_id") groupId: Int, @Body request: LoanChargeRequest): Response<LoanChargeResponse>

    @GET("/notification")
    suspend fun getNotifications(@Query("group_id") groupId: Int): Response<NotificationResponse>

}








