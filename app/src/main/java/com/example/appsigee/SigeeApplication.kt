package com.example.appsigee

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appsigee.data.local.AppDatabase

import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.appsigee.data.local.entity.DispositivoEntity
import com.example.appsigee.data.local.entity.GrupoEntity
import com.example.appsigee.data.local.entity.UsuarioEntity
import com.example.appsigee.domain.model.TipoDispositivo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SigeeApplication : Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default)
    
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this, applicationScope)
    }
}
