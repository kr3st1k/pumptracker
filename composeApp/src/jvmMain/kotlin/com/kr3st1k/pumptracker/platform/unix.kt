package com.kr3st1k.pumptracker.platform

import sn
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream


fun getSNNix(): String? {
    if (sn == null) {
        readUname();
    }
    if (sn == null) {
        print("Huh")
        sn = "TrollSNForYou"
    }

    return sn;
}

fun readUname() {
    var line: String? = null
    var br: BufferedReader? = null

    try {
        br = read("uname --nodename")
        while ((br.readLine().also { line = it }) != null) {
            sn = line!!.trim()
            break
        }
    } catch (e: IOException) {
        throw java.lang.RuntimeException(e)
    } finally {
        if (br != null) {
            try {
                br.close()
            } catch (e: IOException) {
                throw java.lang.RuntimeException(e)
            }
        }
    }
}

fun read(command: String): BufferedReader {
    var os: OutputStream? = null

    val runtime = Runtime.getRuntime()
    var process: Process? = null
    try {
        process =
            runtime.exec(command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    } catch (e: IOException) {
        throw java.lang.RuntimeException(e)
    }

    os = process.outputStream
    val `is` = process.inputStream

    try {
        os.close()
    } catch (e: IOException) {
        throw java.lang.RuntimeException(e)
    }

    return BufferedReader(InputStreamReader(`is`))
}