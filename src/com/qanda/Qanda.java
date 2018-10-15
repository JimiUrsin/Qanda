package com.qanda;



import static spark.Spark.*;

public class Qanda {

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World!");
    }
}
