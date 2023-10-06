package com.example.myapplication.navigate

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.AirbankApplication
import com.example.myapplication.screens.AddChildScreen
import com.example.myapplication.screens.AuthScreen
import com.example.myapplication.screens.BonusTransferScreen
import com.example.myapplication.screens.BottomNavItem
import com.example.myapplication.screens.ChildConfiscationTransferScreen
import com.example.myapplication.screens.ChildLoanRepaymentScreen
import com.example.myapplication.screens.ChildLoanScreen
import com.example.myapplication.screens.ChildLoanStartScreen
import com.example.myapplication.screens.ChildMainScreen
import com.example.myapplication.screens.ChildRuleScreen
import com.example.myapplication.screens.ChildSavingsApplication
import com.example.myapplication.screens.ChildSavingsScreen
import com.example.myapplication.screens.ChildSavingsTransferScreen
import com.example.myapplication.screens.ChildSavingsWaitingScreen
import com.example.myapplication.screens.ChildTaxTransferScreen
import com.example.myapplication.screens.ChildTransactionHistoryScreen
import com.example.myapplication.screens.ChildWalletScreen
import com.example.myapplication.screens.FirstScreen
import com.example.myapplication.screens.GroupConfirmScreen
import com.example.myapplication.screens.LoanScreen
import com.example.myapplication.screens.MainScreen
import com.example.myapplication.screens.MyPageScreen
import com.example.myapplication.screens.NotificationScreen
import com.example.myapplication.screens.SavingsApproveScreen
import com.example.myapplication.screens.SavingsBonusScreen
import com.example.myapplication.screens.SavingsScreen
import com.example.myapplication.screens.SavingsWaitingScreen
import com.example.myapplication.screens.SignUpScreen
import com.example.myapplication.screens.WalletScreen
import com.example.myapplication.viewmodel.SavingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AppNavigation(navController: NavHostController){
    var userRole by remember { mutableStateOf("")}
    val savingsViewModel: SavingsViewModel = hiltViewModel()
    val savingsData by savingsViewModel.savingsState.collectAsState(initial = null)

    LaunchedEffect(Unit){
        savingsViewModel.getSavings()
    }

    LaunchedEffect(Unit) {
        val userrole = withContext(Dispatchers.IO) {
            AirbankApplication.prefs.getString("role", "") // 비동기 작업을 수행하여 SharedPreferences 값 가져오기
        }
        userRole = userrole
    }

    NavHost(navController = navController, startDestination = "First") {
        composable(BottomNavItem.Main.screenRoute) {
            userRole = AirbankApplication.prefs.getString("role","")
            Log.d("userRole",userRole)
            if (userRole == "CHILD"){
                ChildMainScreen(navController = navController)
            }
            else if(userRole == "PARENT"){
                MainScreen(navController = navController)
            }
        }
        composable(BottomNavItem.Savings.screenRoute) {
            userRole = AirbankApplication.prefs.getString("role","")
            Log.d("userRole",userRole)
            if (userRole == "CHILD"){
                ChildSavingsScreen(navController = navController)
            }
            else if(userRole == "PARENT"){
                if (savingsData?.data?.data?.status == null) {
                    navController.navigate("SavingsWaiting")
                } else if (savingsData?.data?.data?.status == "PENDING") {
                    navController.navigate("savingsApprove")
                } else if (savingsData?.data?.data?.status == "PROCEEDING") {
                    navController.navigate("savings")
                }
//                SavingsScreen(navController = navController)
            }


        }
        composable(BottomNavItem.Loan.screenRoute) {
            userRole = AirbankApplication.prefs.getString("role","")
            Log.d("userRole",userRole)
            if (userRole == "CHILD"){
                ChildLoanScreen(navController = navController)
            }
            else if(userRole == "PARENT"){
                LoanScreen(navController = navController)
            }

        }
        composable(BottomNavItem.Wallet.screenRoute) {
            userRole = AirbankApplication.prefs.getString("role","")
            Log.d("userRole",userRole)
            if (userRole == "CHILD"){
                ChildWalletScreen(navController = navController)
            }
            else if(userRole == "PARENT"){


                WalletScreen(navController = navController)
            }

        }
        composable(BottomNavItem.MyPage.screenRoute){
            MyPageScreen(navController = navController)
        }
        composable(BottomNavItem.Notification.screenRoute){
            NotificationScreen(navController = navController)
        }

        composable(BottomNavItem.SignUp.screenRoute){
            SignUpScreen(navController = navController)
        }

        composable(BottomNavItem.SignIn.screenRoute){
            AuthScreen(navController = navController)
        }
        composable("savingsApplication") {
            ChildSavingsApplication(navController = navController)
        }
        composable("childSavings") {
            ChildSavingsScreen(navController = navController)
        }
        composable("First"){
            FirstScreen(navController = navController)
        }

        composable("savingsApprove") {
            SavingsApproveScreen(navController = navController)
        }
        composable("childSavingsTransfer") {
            ChildSavingsTransferScreen(navController = navController)
        }
        composable("savingsBonus"){
            SavingsBonusScreen(navController = navController)
        }
        composable("ChildWallet"){
            ChildWalletScreen(navController = navController)
        }
        composable("ChildTaxTransfer"){
            ChildTaxTransferScreen(navController =navController)
        }
        composable("ChildConfiscationTransfer"){
            ChildConfiscationTransferScreen(navController= navController)
        }

        composable("addChild"){
            AddChildScreen(navController = navController)
        }

        composable("ChildLoanStart"){
            ChildLoanStartScreen(navController = navController)
        }

        composable("ChildLoanRepayment"){
            ChildLoanRepaymentScreen(navController = navController)
        }
        composable("BonusTransfer"){
            BonusTransferScreen(navController=navController)
        }
        composable("ChildLoan"){
            ChildLoanScreen(navController = navController)
        }
        composable("Loan"){
            LoanScreen(navController = navController)
        }
        composable("ChildTransactionHistory"){
            ChildTransactionHistoryScreen(navController = navController)
        }
        composable("SavingsWaiting"){
            SavingsWaitingScreen(navController = navController)
        }
        composable("ChildSavingsWaiting"){
            ChildSavingsWaitingScreen(navController = navController)
        }
        composable("ChildRule"){
            ChildRuleScreen(navController= navController)
        }
        composable("GroupConfirm"){
            GroupConfirmScreen(navController=navController)
        }


    }
}

