package com.nsicyber.barblend.common

class ApiKeyProvider {
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
        external fun getApiKey(): String
    }
}