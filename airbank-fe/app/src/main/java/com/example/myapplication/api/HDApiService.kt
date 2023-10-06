package com.example.myapplication.api

import com.example.myapplication.model.GETCreditHistoryResponse
import com.example.myapplication.model.GETGroupsEnrollResponse
import com.example.myapplication.model.GETGroupsResponse
import com.example.myapplication.model.GETLogoutResponse
import com.example.myapplication.model.GETMembersResponse
import com.example.myapplication.model.PATCHGroupsConfirmRequest
import com.example.myapplication.model.PATCHGroupsConfirmResponse
import com.example.myapplication.model.PATCHGroupsFundResponse
import com.example.myapplication.model.PATCHMembersRequest
import com.example.myapplication.model.PATCHMembersResponse
import com.example.myapplication.model.POSTGroupsFundRequest
import com.example.myapplication.model.POSTGroupsFundResponse
import com.example.myapplication.model.POSTGroupsRequest
import com.example.myapplication.model.POSTGroupsResponse
import com.example.myapplication.model.POSTLoginRequest
import com.example.myapplication.model.POSTLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query


interface HDApiService {

    @GET("/auth/logout")
    suspend fun logoutUser(): Response<GETLogoutResponse>
    @POST("/auth/login")
    suspend fun loginUser(@Body postLoginRequest: POSTLoginRequest): Response<POSTLoginResponse>

    @PATCH("/members")
    suspend fun signupUser(@Body patchMembersRequest: PATCHMembersRequest): Response<PATCHMembersResponse>
    @GET("/members")
    suspend fun getUserInfo(): Response<GETMembersResponse>

    @GET("/groups")
    suspend fun getGroups() : Response<GETGroupsResponse>
    @POST("/groups")
    suspend fun postGroups(@Body postGroupsRequest: POSTGroupsRequest) :Response<POSTGroupsResponse>
    @PATCH("/groups/confirm")
    suspend fun patchGroupsConfirm(@Body patchGroupsConfirmRequest: PATCHGroupsConfirmRequest, @Query("group_id") groupId: Int): Response<PATCHGroupsConfirmResponse>
    @POST("/groups/fund")
    suspend fun postGroupsFund(@Body postGroupsFundRequest: POSTGroupsFundRequest, @Query("group_id") groupId: Int): Response<POSTGroupsFundResponse>
    @PATCH("/groups/fund")
    suspend fun patchGroupsFund(@Body patchGroupsFundRequest: POSTGroupsFundRequest, @Query("group_id") groupId: Int): Response<PATCHGroupsFundResponse>

    @GET("/groups/fund")
    suspend fun getGroupsFund(@Query("group_id") groupId: Int): Response<PATCHGroupsFundResponse>

    @GET("/groups/enroll")
    suspend fun getGroupsEnroll():Response<GETGroupsEnrollResponse>

    @GET("/members/credit-history")
    suspend fun getCreditHistory(@Query("group_id") groupId: Int):Response<GETCreditHistoryResponse>

}








