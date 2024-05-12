package com.nhom9.message.downloader

interface Downloader {
    fun downloadFile(url: String): Long
}