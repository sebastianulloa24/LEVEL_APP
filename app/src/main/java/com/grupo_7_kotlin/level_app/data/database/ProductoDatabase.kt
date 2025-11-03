package com.grupo_7_kotlin.level_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.grupo_7_kotlin.level_app.data.dao.ProductoDao
import com.grupo_7_kotlin.level_app.data.dao.UsuarioDao
import com.grupo_7_kotlin.level_app.data.dao.ResenaDao
import com.grupo_7_kotlin.level_app.data.model.Producto
import com.grupo_7_kotlin.level_app.data.model.Resena
import com.grupo_7_kotlin.level_app.data.model.Usuario
import com.grupo_7_kotlin.level_app.data.obtenerProductosPredefinidos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Usuario::class, Producto::class, Resena::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun resenaDao(): ResenaDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            // Si la INSTANCE no es nula, devuélvela; si lo es, crea la base de datos.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "level_up_db"
                )
                    // Agregamos el callback de precarga aquí
                    .addCallback(PrepopulateCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


    private class PrepopulateCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        // OBSERVACION:db aquí es SupportSQLiteDatabase, no AppDatabase.
        // Usaremos getDatabase para obtener la instancia completa.
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database -> //ESTA ES LA CLAVE: Usamos la INSTANCIA completa
                scope.launch {
                    // Llamamos al DAO desde la instancia completa de AppDatabase
                    val productoDao = database.productoDao()
                    productoDao.insertarTodosLosProductos(obtenerProductosPredefinidos())

                    // Opcional: precargar un usuario para pruebas es que es necesario
                    // database.usuarioDao().insertarUsuario(Usuario(...))
                }
            }
        }
    }
}