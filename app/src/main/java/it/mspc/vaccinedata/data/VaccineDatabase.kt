package it.mspc.vaccinedata.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.matrix.roomigrant.GenerateRoomMigrations
import it.mspc.vaccinedata.data.lastupdate.LastUpdate
import it.mspc.vaccinedata.data.lastupdate.LastUpdateDao
import it.mspc.vaccinedata.data.vaccine.Vaccine
import it.mspc.vaccinedata.data.vaccine.VaccineDao
import it.mspc.vaccinedata.models.PlateaDao


@Database( entities = [Vaccine::class, Platea::class, LastUpdate::class], version = 3, exportSchema = false)
//@TypeConverters(Converters::class)
@GenerateRoomMigrations
abstract class VaccineDatabase: RoomDatabase() {

    abstract fun vaccineDao(): VaccineDao
    abstract fun plateaDao(): PlateaDao
    abstract fun lastupdateDao(): LastUpdateDao

    companion object{
        @Volatile
        private var INSTANCE: VaccineDatabase? = null

        fun getDatabase(context: Context): VaccineDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VaccineDatabase::class.java,
                    "vaccine_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}