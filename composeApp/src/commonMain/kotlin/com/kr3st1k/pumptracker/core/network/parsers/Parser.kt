package com.kr3st1k.pumptracker.core.network.parsers

import com.fleeksoft.ksoup.nodes.Document

abstract class Parser<T> {
    abstract fun parse(document: Document): T

}