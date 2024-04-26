package com.example.localpros.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

object LogUtils {
    private var printWriter: PrintWriter? = null

    fun init(context: Context) {
        try {
            val logFile = File(context.getExternalFilesDir(null), "ErroresFileLocalPros.txt")
            printWriter = PrintWriter(FileOutputStream(logFile, false))
        } catch (e: Exception) {
            Log.e("LogUtils", "Error al inicializar el log file", e)
        }
    }

    fun log(message: String) {
        printWriter?.println(message)
        printWriter?.flush()
        Log.d("LogUtils", message)
    }

    fun close() {
        try {
            printWriter?.close()
        } catch (e: Exception) {
            Log.e("LogUtils", "Error al cerrar el log file", e)
        }
    }
}
