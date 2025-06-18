package org.example;

public class CoreLogic {

    public void printMessage (String message){
        System.out.println(message);
    }

    public void printOS (){
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("os.version"));
    }
}