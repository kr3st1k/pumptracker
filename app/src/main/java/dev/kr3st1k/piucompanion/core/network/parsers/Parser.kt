package dev.kr3st1k.piucompanion.core.network.parsers

import org.jsoup.nodes.Document

abstract class Parser<T> {
    abstract fun parse(document: Document): T

}