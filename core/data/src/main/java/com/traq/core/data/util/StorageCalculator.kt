package com.traq.core.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class StorageCalculator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /** Returns total size of all Room database files in bytes */
    fun getDatabaseSizeBytes(): Long {
        val dbDir = context.getDatabasePath("traq_database").parentFile ?: return 0
        return dbDir.listFiles()
            ?.filter { it.name.startsWith("traq_database") }
            ?.sumOf { it.length() }
            ?: 0
    }

    /** Returns the app's total cache size in bytes */
    fun getCacheSizeBytes(): Long {
        return dirSize(context.cacheDir)
    }

    fun clearCache() {
        context.cacheDir.deleteRecursively()
    }

    private fun dirSize(dir: File): Long {
        if (!dir.exists()) return 0
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }
}
