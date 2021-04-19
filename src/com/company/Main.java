package com.company;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Main{
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

        ArrayList<String> gestureList = new ArrayList<>(7);

        gestureList.add("rock");
        gestureList.add("scissors");
        gestureList.add("lizard");
        gestureList.add("paper");
        gestureList.add("Spock");

        ArrayList<String> computersMoves = checkParameters(args, gestureList);
        String key = getRandomHexString();
        String hmac = getHMAC(key, computersMoves.get(0));
        game(key, hmac, computersMoves, gestureList);
    }

    public static String getRandomHexString(){
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 32){
            sb.append(Integer.toHexString(random.nextInt()));
        }
        return sb.toString();
    }

    public static String getHMAC(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256.init(sks);
        return hex(sha256.doFinal(data.getBytes()));
    }

    public static ArrayList<String> checkParameters(String[] args, ArrayList<String> gestureList){

        ArrayList<String> computersMoves =  new ArrayList<>(args.length);
        boolean flag = true;

        if (args.length == 0) {
            System.out.println("Nothing was inputted.");
            flag = false;
        }
        if (args.length < 3) {
            System.out.println("Parameters` amount must be three or more");
            flag = false;
        }
        if (args.length%2 == 0){
            System.out.println("Parameters` amount must be odd");
            flag = false;
        }
        for (String str: args){
            if (!gestureList.contains(str)){
                System.out.println("Use correct parameters");
                flag = false;
            }
            if (computersMoves.toArray().length>0){
                if (computersMoves.get(computersMoves.toArray().length-1).equals(str)){
                    System.out.println("Don't repeat same parameter in row");
                    flag = false;
                    break;
                }
            }
            computersMoves.add(str);
        }

        if (!flag){
            System.out.println("Example set of correct parameters: rock paper scissors lizard Spock");
            System.exit(0);
        }
        return computersMoves;
    }

    public static void game(String key, String hmac, ArrayList<String> computerMoves,
                            ArrayList<String> gestureList){
        System.out.println("HMAC: " + hmac);
        int num = -1;
        String s;
        Scanner in = new Scanner(System.in);
        while (num>5||num<0){
            System.out.println("Available moves:");
            System.out.println("1 - rock");
            System.out.println("2 - scissors");
            System.out.println("3 - lizard");
            System.out.println("4 - paper");
            System.out.println("5 - Spock");
            System.out.println("0 - exit");
            System.out.print("Enter your move: ");
            s = in.next();
            try{
                num = Integer.parseInt(s);
            }catch (NumberFormatException e){
                System.out.println("Please, use numbers");
            }
        }
        in.close();
        if (num==0){System.exit(0);}
        matchpoint(num-1, computerMoves.get(0), gestureList, key);
    }

    public static void matchpoint(int playerMove, String computerMoveName, ArrayList<String> gestureList, String key){
//        String playerMove = gestureList.get(num-1);

        int cM = gestureList.indexOf(computerMoveName);
        System.out.println("Your move: " + gestureList.get(playerMove));
        System.out.println("Computer move: " +computerMoveName);
        if (playerMove == cM){
            System.out.println("Draw!");
        }
        else {
            switch (playerMove) {
                case 0:
                    if (cM == 1 || cM == 2) {
                        System.out.println("You win!");
                    } else {
                        System.out.println("You lose!");
                    }
                    break;
                case 1:
                    if (cM == 2 || cM == 3) {
                        System.out.println("You win!");
                    } else {
                        System.out.println("You lose!");
                    }
                    break;
                case 2:
                    if (cM == 3 || cM == 4) {
                        System.out.println("You win!");
                    } else {
                        System.out.println("You lose!");
                    }
                    break;
                case 3:
                    if (cM == 4 || cM == 0) {
                        System.out.println("You win!");
                    } else {
                        System.out.println("You lose!");
                    }
                    break;
                case 4:
                    if (cM == 0 || cM == 1) {
                        System.out.println("You win!");
                    } else {
                        System.out.println("You lose!");
                    }
                    break;
            }
        }
        System.out.println("HMAC key:" + key);
    }

    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }

}
