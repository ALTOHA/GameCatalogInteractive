package com.example.iainteractive.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.iainteractive.data.model.VideogameModel

class GamesDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "GamesDatabase.db"
        const val DATABASE_VERSION = 1

        const val TABLE_GAMES = "games"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_THUMBNAIL = "thumbnail"
        const val COLUMN_GAMEURL = "url"
        const val COLUMN_SHORT_DESCRIPTION = "description"
        const val COLUMN_GENRE = "genre"
        const val COLUMN_PLATFORM = "platform"
        const val COLUMN_PUBLISHER = "publisher"
        const val COLUMN_DEVELOPER = "developer"
        const val COLUMN_RELEASE_DATE = "release"
        const val COLUMN_PROFILE_URL = "profile"

        // Script de creación de la tabla
        private const val CREATE_TABLE_GAMES = """
            CREATE TABLE $TABLE_GAMES (
                $COLUMN_ID INT PRIMARY KEY ,
                $COLUMN_TITLE TEXT,
                $COLUMN_THUMBNAIL TEXT,
                $COLUMN_SHORT_DESCRIPTION TEXT,
                $COLUMN_GAMEURL TEXT,
                $COLUMN_GENRE TEXT,
                $COLUMN_PLATFORM TEXT, 
                $COLUMN_PUBLISHER TEXT,
                $COLUMN_DEVELOPER TEXT,
                $COLUMN_RELEASE_DATE TEXT,
                $COLUMN_PROFILE_URL TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crea la tabla cuando se crea la base de datos por primera vez
        db.execSQL(CREATE_TABLE_GAMES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla anterior si la versión cambia y crea una nueva
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }

    // Inserta un juego
    fun insertGame(
        id: Int,
        title: String,
        thumbnail: String,
        description: String,
        url: String,
        genre: String,
        platform: String,
        publisher: String,
        developer: String,
        releaseDate: String,
        profileUrl: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_TITLE, title)
            put(COLUMN_THUMBNAIL, thumbnail)
            put(COLUMN_GAMEURL, url)
            put(COLUMN_SHORT_DESCRIPTION, description)
            put(COLUMN_GENRE, genre)
            put(COLUMN_PLATFORM, platform)
            put(COLUMN_PUBLISHER, publisher)
            put(COLUMN_DEVELOPER, developer)
            put(COLUMN_RELEASE_DATE, releaseDate)
            put(COLUMN_PROFILE_URL, profileUrl)
        }
        return db.insert(TABLE_GAMES, null, values)
    }

    // Obtener todos los juegos
    fun getAllGames(): List<VideogameModel> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_GAMES, null, null, null, null, null, null)
        val games = mutableListOf<VideogameModel>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val thumbnail = getString(getColumnIndexOrThrow(COLUMN_THUMBNAIL))
                val url = getString(getColumnIndexOrThrow(COLUMN_GAMEURL))
                val description = getString(getColumnIndexOrThrow(COLUMN_SHORT_DESCRIPTION))
                val genre = getString(getColumnIndexOrThrow(COLUMN_GENRE))
                val platform = getString(getColumnIndexOrThrow(COLUMN_PLATFORM))
                val publisher = getString(getColumnIndexOrThrow(COLUMN_PUBLISHER))
                val developer = getString(getColumnIndexOrThrow(COLUMN_DEVELOPER))
                val releaseDate = getString(getColumnIndexOrThrow(COLUMN_RELEASE_DATE))
                val profileUrl = getString(getColumnIndexOrThrow(COLUMN_PROFILE_URL))
                games.add(
                    VideogameModel(
                        id,
                        title,
                        thumbnail,
                        description,
                        url,
                        genre,
                        platform,
                        publisher,
                        developer,
                        releaseDate,
                        profileUrl
                    )
                )
            }
            close()
        }
        return games
    }

    // Obtiene la información del juego seleccionado
    fun getOneGame(uid: String): List<VideogameModel> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_GAMES, null, "id=?", arrayOf(uid), null, null, null)
        val games = mutableListOf<VideogameModel>()

        if (cursor.moveToFirst()) {
            do {
                val game = VideogameModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)),
                    gameUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMEURL)),
                    shortDescription = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_SHORT_DESCRIPTION
                        )
                    ),
                    genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE)),
                    platform = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATFORM)),
                    publisher = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHER)),
                    developer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEVELOPER)),
                    releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE)),
                    freeToGameProfileUrl = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_PROFILE_URL
                        )
                    ),
                )
                games.add(game)
            } while (cursor.moveToNext())
        }
        cursor.close() // Cerrar el cursor después de su uso
        return games
    }

    // Actualiza un juego
    fun updateGame(
        id: String,
        title: String,
        url: String,
        genre: String,
        platform: String,
        publisher: String,
        developer: String,
        releaseDate: String,
        profileUrl: String
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_GAMEURL, url)
            put(COLUMN_GENRE, genre)
            put(COLUMN_PLATFORM, platform)
            put(COLUMN_PUBLISHER, publisher)
            put(COLUMN_DEVELOPER, developer)
            put(COLUMN_RELEASE_DATE, releaseDate)
            put(COLUMN_PROFILE_URL, profileUrl)
        }
        return db.update(TABLE_GAMES, values, "$COLUMN_ID=?", arrayOf(id))
    }

    // Elimina un juego
    fun deleteGame(id: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_GAMES, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Guarda un marcador en SharedPreferences para indicar que los datos iniciales se han insertado
    fun isDataInserted(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("dataInserted", false)
    }

    fun setDataInserted(context: Context, inserted: Boolean) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("dataInserted", inserted).apply()
    }

}