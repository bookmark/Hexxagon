
package com.helloworld.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.helloworld.HelloWorld;
import com.helloworld.utils.HexxagonConstant;

public class Map {
    public static Cell[][] maps;

    public static int cols = 0;

    public static int rows = 0;

    public static int TOP = 0;

    public static int RIGHT = 0;

    public static int BUTTOM = 0;

    public static int LEFT = 0;

    private static String TAG = "MAP";

    public static float HEIGHT_HEX;

    public static float WIDTH_HEX;

    public static float X_AXIAL = 0;

    public static float Y_AXIAL = 0;

  
    public Map() {
        // TODO Auto-generated constructor stub
    }

    private static SecretKey getSecretKey(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        // NOTE: last argument is the key length, and it is 256
        KeySpec spec = new PBEKeySpec(password, salt, 1024, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return (secret);
    }

    private static String decryptMap(String encrypttext, String key) {
        String originalString = "";
        byte[] decoded = new byte[32];
        decoded = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(decoded, "AES");
        byte[] encrypted = encrypttext.getBytes();
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(encrypted);
            originalString = new String(original);

            System.out.println("Original string: " + originalString);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return originalString;
    }

    public static void initMap(InputStream is) {

        byte[] dimentions = new byte[2];
        byte[] border = new byte[4];
        byte[] value = new byte[3];

        char ch_x, ch_y, ch_v;
        String str_x, str_y, str_v;
        int x, y, v;

        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        //
        // try {
        // String data = bufferedReader.readLine();
        // decryptMap(data, "L0ck it up saf3");
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        try {
            is.read(border, 0, 4);

            initBorder(border);

            maps = new Cell[9][9];

            int count = 81;
            while (count > 0) {

                is.read(value, 0, 3);

                ch_x = (char)value[1];
                ch_y = (char)value[0];
                ch_v = (char)value[2];

                str_x = Character.toString(ch_x);
                str_y = Character.toString(ch_y);
                str_v = Character.toString(ch_v);

                x = Integer.parseInt(str_x);
                y = Integer.parseInt(str_y);
                v = Integer.parseInt(str_v);

                maps[y][x] = new Cell(v);
                count--;
            }
            ;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void initBorder(byte[] border) {
        TOP = Integer.parseInt(Character.toString((char)border[0]));
        RIGHT = Integer.parseInt(Character.toString((char)border[1]));
        BUTTOM = Integer.parseInt(Character.toString((char)border[2]));
        LEFT = Integer.parseInt(Character.toString((char)border[3]));
    }

    /**
     * Tinh toan heigth va width cua moi hexa tren man hinh
     */
    public static void calsSizePerItem() {

        // HexxagonConstant.MAP_WIDTH = HelloWorld.CAMERA_WIDTH * 3 / 4;
        HexxagonConstant.MAP_WIDTH = HelloWorld.CAMERA_WIDTH;// Full width
        HexxagonConstant.MAP_HEIGHT = HelloWorld.CAMERA_HEIGHT; // Full height

        int distance_x = 17 - (8 - RIGHT) - LEFT;
        int distance_y = 9;

        float k = distance_x * 3 / 4 + 1 / 2;
        WIDTH_HEX = HexxagonConstant.MAP_WIDTH * 4 / (distance_x * 3);
        WIDTH_HEX = HexxagonConstant.MAP_WIDTH * 4 / (distance_x * 3 + 1); // Full
                                                                           // Width

        HEIGHT_HEX = HexxagonConstant.MAP_HEIGHT / distance_y;

        X_AXIAL = 0 - (LEFT) * (WIDTH_HEX * 3 / 4);
    }
}
