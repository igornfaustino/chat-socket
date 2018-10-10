/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 10 2018
 * 
 * Handle file
 */

package com.nfaustino.chat;

import java.io.*;

public class MyFile {
    File dir;
    String dirPath;

    public MyFile(String path) {
        this.dirPath = path;
        this.dir = new File(path);
        this.dir.mkdirs();
    }

    public String[] getFiles() {
        return this.dir.list();
    }

    public Boolean fileExist(String path) {
        File f = new File(path);
        return (f.exists() && !f.isDirectory());
    }

    public byte[] getFile(String path) {
        File f = new File(this.dirPath + path);
        byte[] bytesArray = new byte[(int) f.length()];
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytesArray;
    }

    public boolean saveFile(String path, byte[] b) {
        try {
            FileOutputStream fos = new FileOutputStream(this.dirPath + "/" + path);
            fos.write(b);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void appendStrToFile(String fileName, String str) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public boolean deleteFile(String path) {
        File f = new File(this.dirPath + path);
        return f.delete();
    }
}