package com.example.cfttest

import java.net.HttpURLConnection
import java.net.URL

class Request {

    lateinit var connection: HttpURLConnection

    public suspend fun getConnection(): HttpURLConnection
    {
        val url = URL("https://www.cbr-xml-daily.ru/daily_json.js");
        connection = url.openConnection() as HttpURLConnection;
        connection.requestMethod = "GET";
        //connection.connect();
        return connection
    }
    public suspend fun closeConnection(){
        this.connection.disconnect()
    }
}