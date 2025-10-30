package com.grupo_7_kotlin.level_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grupo_7_kotlin.level_app.data.dao.ProductoDao
import com.grupo_7_kotlin.level_app.data.model.Producto
import kotlin.jvm.java

@Database(
    entities = [Producto::class],
    version=1,
    exportSchema = false // evite warning
)

abstract class ProductoDatabase: RoomDatabase(){
    abstract fun productoDao(): ProductoDao

    companion object{
        @Volatile   //  cualquier cambio en el valor de la INSTANCE va ser visible
        private var INSTANCE:ProductoDatabase? = null

        fun getDatabase(context: Context):ProductoDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"
                ).build()
                INSTANCE = instance
                instance

            } // fin return

        }// fin getdatabase

    } // fin companion

} // fin abstract
