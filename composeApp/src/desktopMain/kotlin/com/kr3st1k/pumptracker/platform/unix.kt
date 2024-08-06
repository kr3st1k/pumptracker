package com.kr3st1k.pumptracker.platform

import sn
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream


fun getSNNix(): String? {
    if (sn == null) {
        readDmidecode();
    }
    if (sn == null) {
        readLshal();
    }
    if (sn == null) {
        print("Huh")
        sn = "TrollSNForYou"
    }

    return sn;
}

fun readLshal() {
    var line: String? = null
    val marker = "system.hardware.serial ="
    var br: BufferedReader? = null

    try {
        br = read("lshal")
        while ((br.readLine().also { line = it }) != null) {
            if (line!!.indexOf(marker) != -1) {
                sn = line!!.split(marker.toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].replace("\\(string\\)|(\\')".toRegex(), "")
                    .trim { it <= ' ' }
                break
            }
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

fun readDmidecode() {
    var line: String? = null
    val marker = "Serial Number:"
    var br: BufferedReader? = null

    try {
        br = read("dmidecode -t system")
        while ((br.readLine().also { line = it }) != null) {
            if (line!!.indexOf(marker) != -1) {
                sn = line!!.split(marker.toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].trim { it <= ' ' }
                break
            }
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    } finally {
        if (br != null) {
            try {
                br.close()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}