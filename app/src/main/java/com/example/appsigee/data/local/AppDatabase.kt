package com.example.appsigee.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsigee.data.local.dao.AlertaDao
import com.example.appsigee.data.local.dao.ConfiguracionConsumoDao
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
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dispositivoDao(): DispositivoDao
    abstract fun grupoDao(): GrupoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun configuracionConsumoDao(): ConfiguracionConsumoDao
    abstract fun alertaDao(): AlertaDao

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
                    .fallbackToDestructiveMigration()
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
                        populateDatabase(
                            database.usuarioDao(),
                            database.grupoDao(),
                            database.dispositivoDao(),
                            database.alertaDao()
                        )
                    }
                }
            }

            suspend fun populateDatabase(
                usuarioDao: UsuarioDao,
                grupoDao: GrupoDao,
                dispositivoDao: DispositivoDao,
                alertaDao: AlertaDao
            ) {
                val uId = "u1"
                usuarioDao.insertUsuario(UsuarioEntity(uId, "Admin", "admin@sigee.com", "1234", "S001"))
                
                val salaId = "g1"
                grupoDao.insertGrupo(GrupoEntity(salaId, "Sala", uId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d1", "Televisión", "TELEVISION", true, 23.0, null, salaId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d2", "Nintendo", "CONSOLA", true, 15.0, null, salaId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d3", "Nuevo Dispositivo", "NUEVO", true, 0.0, null, salaId))

                val cocinaId = "g2"
                grupoDao.insertGrupo(GrupoEntity(cocinaId, "Cocina", uId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d4", "Refrigerador", "REFRIGERADOR", true, 91.0, null, cocinaId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d5", "Microondas", "MICROONDAS", true, 38.0, null, cocinaId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d6", "Nuevo Dispositivo", "NUEVO", true, 0.0, null, cocinaId))

                val juanId = "g3"
                grupoDao.insertGrupo(GrupoEntity(juanId, "Habitación Juan", uId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d7", "Laptop", "COMPUTADORA", true, 45.0, null, juanId))
                dispositivoDao.insertDispositivo(DispositivoEntity("d8", "Nuevo Dispositivo", "NUEVO", true, 0.0, null, juanId))

                // Alertas para Televisión (d1)
                alertaDao.insertAlerta(AlertaEntity("a1", "Límite de tiempo", System.currentTimeMillis(), "ALERTA", "d1"))
                alertaDao.insertAlerta(AlertaEntity("a2", "Límite de tiempo", System.currentTimeMillis() - 86400000, "OK", "d1"))
                
                // Alertas para Refrigerador (d4)
                alertaDao.insertAlerta(AlertaEntity("a3", "Exceso de consumo", System.currentTimeMillis() - 3600000, "ALERTA", "d4"))
                alertaDao.insertAlerta(AlertaEntity("a4", "Exceso de consumo", System.currentTimeMillis() - 172800000, "OK", "d4"))

                // Alertas para Microondas (d5)
                alertaDao.insertAlerta(AlertaEntity("a5", "Límite de tiempo", System.currentTimeMillis() - 7200000, "ALERTA", "d5"))
                
                // Alertas para Laptop (d7) - Más datos para probar scroll
                alertaDao.insertAlerta(AlertaEntity("a6", "Exceso de consumo", System.currentTimeMillis() - 10800000, "ALERTA", "d7"))
                alertaDao.insertAlerta(AlertaEntity("a7", "Límite de tiempo", System.currentTimeMillis() - 432000000, "OK", "d7"))
                alertaDao.insertAlerta(AlertaEntity("a8", "Falla de red", System.currentTimeMillis() - 500000000, "ALERTA", "d7"))
                alertaDao.insertAlerta(AlertaEntity("a9", "Batería baja", System.currentTimeMillis() - 600000000, "OK", "d7"))
                alertaDao.insertAlerta(AlertaEntity("a10", "Sobrecalentamiento", System.currentTimeMillis() - 700000000, "ALERTA", "d7"))
            }
        }
    }
}
