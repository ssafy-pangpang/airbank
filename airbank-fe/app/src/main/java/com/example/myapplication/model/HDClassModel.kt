package com.example.myapplication.model

import com.google.gson.annotations.SerializedName


data class POSTLoginResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("name")
        val name: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
    )
}
data class POSTLoginRequest(
    @SerializedName("oauthIdentifier")
    val oauthIdentifier : String,
    @SerializedName("imageUrl")
    val imageUrl : String,
    @SerializedName("isDefaultImage")
    val isDefaultImage : Boolean
)

data class GETLogoutResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("name")
        val name: String
    )
}
data class PATCHMembersRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("role")
    val role: String
)

data class PATCHMembersResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("name")
        val name: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("role")
        val role: String
    )
}

data class GETMembersResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("name")
        val name: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("creditScore")
        val creditScore: Int,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("role")
        val role: String
    )
}

data class GETGroupsResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("members")
        val members: List<Member>
    ){
      data class Member(
        @SerializedName("id")
        val id: Int,
        @SerializedName("groupId")
        val groupId: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("creditScore")
        val creditScore: Int?
    )}
}

data class POSTGroupsRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String
)

data class POSTGroupsResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("id")
        val id: Int
    )
}

data class PATCHGroupsConfirmRequest(
    @SerializedName("isAccept")
    val isAccept: Boolean
)

data class PATCHGroupsConfirmResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("id")
        val id: Int
    )
}

data class POSTGroupsFundRequest(
    @SerializedName("taxRate")
    val taxRate: Int,

    @SerializedName("allowanceAmount")
    val allowanceAmount: Int,

    @SerializedName("allowanceDate")
    val allowanceDate: Int,

    @SerializedName("confiscationRate")
    val confiscationRate: Int,

    @SerializedName("loanLimit")
    val loanLimit: Int
)

data class POSTGroupsFundResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("id")
        val id: Int
    )
}

data class PATCHGroupsFundRequest(
    @SerializedName("taxRate")
    val taxRate: Int,

    @SerializedName("allowanceAmount")
    val allowanceAmount: Int,

    @SerializedName("allowanceDate")
    val allowanceDate: Int,

    @SerializedName("confiscationRate")
    val confiscationRate: Int,

    @SerializedName("loanLimit")
    val loanLimit: Int
)

data class PATCHGroupsFundResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("taxRate")
        val taxRate: Int,

        @SerializedName("allowanceAmount")
        val allowanceAmount: Int,

        @SerializedName("allowanceDate")
        val allowanceDate: Int,

        @SerializedName("confiscationRate")
        val confiscationRate: Int,

        @SerializedName("loanLimit")
        val loanLimit: Int
    )
}

data class GETGroupsEnrollResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("id")
        val id: Int
    )
}

data class GETCreditHistoryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("members")
        val creditHistories: List<creditHistory>
    ){
        data class creditHistory(
            @SerializedName("creditScore")
            val creditScore: Int,
            @SerializedName("createdAt")
            val createdAt: String
        )}
}
