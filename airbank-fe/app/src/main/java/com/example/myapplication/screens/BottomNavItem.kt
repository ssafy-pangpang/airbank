package com.example.myapplication.screens

import com.example.myapplication.R
import dagger.hilt.android.HiltAndroidApp


sealed class BottomNavItem(
    val title: String, val icon: Int, val screenRoute: String
) {
    companion object {
        fun fromRoute(route: String): BottomNavItem {
            return when (route) {
                "main" -> Main
                "savings" -> Savings
                "Loan" -> Loan
                "ChildWallet" -> ChildWallet
                "wallet" -> Wallet
                "my" -> MyPage
                "ChildLoan" -> ChildLoan
                "ChildLoanStart" -> ChildLoanStart
                "ChildLoanRepayment" -> ChildLoanRepayment
                "ChildTransactionHistory" -> ChildTransactionHistory
                "childSavings" -> ChildSavings
                "savingsApplication" -> ChildSavingsApplication
                "savingsApprove" -> SavingsApprove
                "savingsBonus" -> SavingsBonus
                "SavingsWaiting" -> SavingsWaiting
                "BonusTransfer" -> BonusTransfer

                // Add cases for other routes
                else -> Main // Default to the main route
            }
        }
    }
    object Main : BottomNavItem("", R.drawable.ihome, "main")
    object Wallet : BottomNavItem("지갑 관리하기", R.drawable.iwallet, "wallet")
    object ChildWallet : BottomNavItem("내 지갑", R.drawable.iwallet, "ChildWallet")
    object Savings : BottomNavItem("티끌 모으기", R.drawable.isavings, "savings")
    object SavingsBonus : BottomNavItem("티끌 모으기", R.drawable.isavings, "savingsBonus")
    object SavingsWaiting : BottomNavItem("티끌 모으기", R.drawable.isavings, "SavingsWaiting")
    object BonusTransfer : BottomNavItem("보너스", R.drawable.isavings, "BonusTransfer")

    object ChildSavings : BottomNavItem("티끌 모으기", R.drawable.isavings, "childSavings")
    object ChildSavingsApplication : BottomNavItem("티끌 모으기", R.drawable.isavings, "savingsApplication")
    object SavingsApprove : BottomNavItem("티끌 모으기", R.drawable.isavings, "savingsApprove")
    object Loan : BottomNavItem("땡겨쓰기", R.drawable.iloan, "Loan")
    object ChildLoan : BottomNavItem("땡겨쓰기", R.drawable.iloan, "ChildLoan")
    object ChildLoanStart : BottomNavItem("떙겨쓰기", R.drawable.iloan, "ChildLoanStart")
    object ChildLoanRepayment : BottomNavItem("떙겨쓰기", R.drawable.iloan, "ChildLoanRepayment")
    object MyPage : BottomNavItem("마이 페이지", R.drawable.imypage, "my")
    object Notification :BottomNavItem("알림", androidx.core.R.drawable.notification_bg,"notification")
    object SignUp : BottomNavItem("회원가입",0,"SignUp")
    object SignIn : BottomNavItem("로그인",0,"SignIn")
    object ChildTransactionHistory : BottomNavItem("거래 내역", 0, "ChildTransactionHistory")


}