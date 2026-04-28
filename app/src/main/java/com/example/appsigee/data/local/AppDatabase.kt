package com.example.appsigee.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsigee.data.local.dao.DispositivoDao
import com.example.appsigee.data.local.dao.GrupoDao
import com.example.appsigee.data.local.dao.UsuarioDao
import com.example.appsigee.data.local.entity.*

import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsuarioEntity::class,
        GrupoEntity::class,
        GatewayEntity::class,
        DispositivoEntity::class,
        ConfiguracionConsumoEntity::class,
        ConsumoEntity::class,
        AlertaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dispositivoDao(): DispositivoDao
    abstract fun grupoDao(): GrupoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sigee_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.usuarioDao(), database.grupoDao(), database.dispositivoDao())
                    }
                }
            }

            suspend fun populateDatabase(
                usuarioDao: UsuarioDao,
                grupoDao: GrupoDao,
                dispositivoDao: DispositivoDao
            ) {
                val uId = "u1"
                usuarioDao.insertUsuario(UsuarioEntity(uId, "Admin", "admin@sigee.com", "1234", "S001"))
                
                val salaId = "g1"
                grupoDao.insertGrupo(GrupoEntity(salaId, "Sala", uId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d1", "Televisión", "TELEVISION", true, 23.0, null, salaId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d2", "Nintendo", "CONSOLA", true, 15.0, null, salaId))

                val cocinaId = "g2"
                grupoDao.insertGrupo(GrupoEntity(cocinaId, "Cocina", uId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d3", "Refrigerador", "REFRIGERADOR", true, 91.0, null, cocinaId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d4", "Microondas", "MICROONDAS", true, 38.0, null, cocinaId))
            }
        }
    }
}
