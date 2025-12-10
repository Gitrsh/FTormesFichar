package com.rsh.f_tormes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rsh.f_tormes.navigation.AppNavigation
import com.rsh.f_tormes.ui.theme.F_TormesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            F_TormesTheme {
                AppNavigation()
            }
        }
    }
}