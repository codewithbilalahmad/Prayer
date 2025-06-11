package com.muhammad.prayer.domain.repository

import com.muhammad.prayer.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationSettings {
    suspend fun saveLocation(location : LocationData)
    fun getLocationFlow() : Flow<LocationData?>
}