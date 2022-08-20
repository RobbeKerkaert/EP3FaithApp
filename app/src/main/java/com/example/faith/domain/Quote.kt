package com.example.faith.domain

data class Quote(
    var q: String = "",
    var a: String = "",
    var i: String = "",
    var c: String = "",
    var h: String = "",
)

data class QuoteList(
    var quotes: List<Quote>,
)
