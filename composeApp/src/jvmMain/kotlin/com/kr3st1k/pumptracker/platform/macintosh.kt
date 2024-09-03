package com.kr3st1k.pumptracker.platform

import sn
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream


fun getSNMacintosh(): String? {
    if (sn != null) {
        return sn
    }

    var os: OutputStream? = null

    val runtime = Runtime.getRuntime()
    var process: Process? = null
    try {
        process = runtime.exec(arrayOf("/usr/sbin/system_profiler", "SPHardwareDataType"))
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    os = process.outputStream
    val `is` = process.inputStream

    try {
        os.close()
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    val br = BufferedReader(InputStreamReader(`is`))
    var line: String? = null
    val marker = "Serial Number"
    try {
        while ((br.readLine().also { line = it }) != null) {
            if (line!!.contains(marker)) {
                sn = line!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].trim { it <= ' ' }
                break
            }
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    } finally {
        try {
            `is`.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    if (sn == null) {
        throw RuntimeException("Cannot find computer SN")
    }

    return sn
}