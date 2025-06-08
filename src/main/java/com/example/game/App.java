package com.example.game;

import com.example.game.Server.DatabaseAccess.SamplePostgresDriver;

public class App
{
    public static void main( String[] args )
    {
        System.out.println("Hello World!");
        SamplePostgresDriver.TestConnection();
    }
}
