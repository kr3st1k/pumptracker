package com.kr3st1k.pumptracker.platform

import sn
import java.io.IOException
import java.io.OutputStream
import java.util.*

fun getSNWindows(): String? {
    if (sn != null) {
        return sn
    }

    var os: OutputStream? = null

    val runtime = Runtime.getRuntime()
    var process: Process? = null
    try {
        process = runtime.exec(arrayOf("wmic", "bios", "get", "serialnumber"))
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    os = process.outputStream
    val inputStream = process.inputStream

    try {
        os.close()
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    val sc: Scanner = Scanner(inputStream)
    try {
        while (sc.hasNext()) {
            val next: String = sc.next()
            if ("SerialNumber" == next) {
                sn = sc.next().trim()
                break
            }
        }
    } finally {
        try {
            inputStream.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    if (sn == null) {
        print("huh")
        sn = "TrollSNForYou"
    }

    return sn
}