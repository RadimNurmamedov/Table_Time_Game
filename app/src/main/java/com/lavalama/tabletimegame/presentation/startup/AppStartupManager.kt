package com.lavalama.tabletimegame.presentation.startup

import com.lavalama.tabletimegame.domain.repository.TTRepository
import com.lavalama.tabletimegame.domain.usecase.StartRefreshDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class AppStartupManager @Inject constructor(
    private val startRefreshDataUseCase: StartRefreshDataUseCase
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun startRefreshData(){
        scope.launch {
            startRefreshDataUseCase()
        }
    }
}