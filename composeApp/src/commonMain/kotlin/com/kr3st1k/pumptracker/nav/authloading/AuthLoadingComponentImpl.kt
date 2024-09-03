package com.kr3st1k.pumptracker.nav.authloading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@Composable
fun AuthLoadingComponentImpl(viewModel: AuthLoadingComponent) {
    Box(modifier = Modifier.fillMaxSize())
    {
        YouSpinMeRightRoundBabyRightRound("Authorizing...")
    }

}