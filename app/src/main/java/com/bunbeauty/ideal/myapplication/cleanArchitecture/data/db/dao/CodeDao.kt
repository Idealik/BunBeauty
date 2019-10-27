package com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.dao

import androidx.room.*
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.Code

@Dao
interface CodeDao {
    @Insert
    suspend fun insert(code: Code)

    @Update
    suspend fun update(code: Code)

    @Delete
    suspend fun delete(code: Code)

    @Query("SELECT * FROM code WHERE code = :code")
    suspend fun getByCode(code: String): Code?
}