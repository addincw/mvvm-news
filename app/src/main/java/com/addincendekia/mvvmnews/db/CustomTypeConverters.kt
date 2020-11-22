package com.addincendekia.mvvmnews.db

import androidx.room.TypeConverter
import com.addincendekia.mvvmnews.model.Source

class CustomTypeConverters {
    @TypeConverter
    fun fromSource(source: Source) = source.name
    @TypeConverter
    fun toSource(name: String) = Source(name, name)
}