package com.lab_lib.restapi;

import org.springframework.boot.SpringApplication;

import io.github.cdimascio.dotenv.Dotenv;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Rest api!" );

        Dotenv dotenv = Dotenv.load();

        System.out.println(dotenv.get("DB_PASSWORD"));
    }
}
