package co.touchlab.kampkit

import co.touchlab.kampkit.models.DataState
import co.touchlab.kampkit.models.ItemDataSummary
import com.rickclephas.kmp.nativecoroutines.NativeFlow
import com.rickclephas.kmp.nativecoroutines.asNativeFlow

val NativeViewModel.breedsNativeFlow: NativeFlow<DataState<ItemDataSummary>>
    get() = breedStateFlow.asNativeFlow()
