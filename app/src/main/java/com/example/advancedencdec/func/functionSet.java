package com.example.advancedencdec.func;


import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Random;

public class functionSet {

    public static String funcData = "";

    public static String KeyGen(String key) // make a 200 byte key from user key
    {
        int len = key.length(), i = 0, n = 1;

        do {
            int x = key.charAt(i) + n; // more randomness by 'n'
            key += x;
            n++;
            i++;

            if (i == len - 1)
                i = 0;
        } while (key.length() < len + 100);

        key = key.substring(len, key.length());

        int[] kArray = new int[key.length()];

        for (int m = 0; m <= key.length() - 1; m++) {
            kArray[m] = key.charAt(m);
            //System.out.println("Generating 200 byte key from Entered key: " + functionSet.percentage(m, key.length() - 1) + "%");
        }

        int kArrLen = kArray.length;
        for (int j = kArrLen - 1; j >= 1; j--) // FISHER-YATES Random Shuffle
        {
            Random rand = new Random();
            // generate a random number k such that 0 <= k <= j
            int k = rand.nextInt(j + 1);

            // swap current element with randomly generated index
            int temp = kArray[j];
            kArray[j] = kArray[k];
            kArray[k] = temp;
        }

        key = "";
        for (int m = 0; m <= kArrLen - 1; m++) {
            key += kArray[m];
        }
        //System.out.println(key.length() + " Byte Key Generated Successfully!");
        return key;
    }

    public static void xor(RandomAccessFile in, RandomAccessFile temp, String key, String mode, String round) {

        int len_var = 0;
        try {
            long incount = in.length();
            int p = 0;
            double percent;
            in.seek(0);
            temp.seek(0);
            for (int j = 0; j <= incount - 1; j++) {
                int intchr = in.read();
                // write at beginning
                temp.write(intchr ^ key.charAt(len_var));

                len_var++;
                if (len_var > key.length() - 1)
                    len_var = 0;
                percent = percentage(p, incount);
                p++;
                System.out.println("[" + round + "]" + " " + mode + " Characters to File:" + percent + "%");
                //funcData=funcData.concat("\n"+("[" + round + "]" + " " + mode + " Characters to File:" + percent + "%"));
            }
        } catch (Exception ignored) {

        }
    }

    public static void shuffle(RandomAccessFile in, RandomAccessFile out) {

        try {
            int p = 0;
            long count = in.length();

            for (long i = count - 1; i >= 0; i--) {
                // Writing on file>>
                in.seek(i); // set cursor of in file at last >> i=count-1
                out.seek(p); // set cursor of output file to start >> p=0
                int ch = in.read(); // read() from in
                out.write(ch); // write it at start of output file
                p = p + 1; // increment p

            }
        } catch (IOException e) {
            Log.e("sky", e.getLocalizedMessage());
        }

    }

    public static double percentage(long no, long total) // percent function
    {
        double per = (no * 100.0) / total;
        per = per * 100;
        per = Math.round(per);
        per = per / 100;
        return per;
    }


    public static String shiftKey(String key, int shiftby) // left shift by factor "shiftby'
    {

        int keylen = key.length();
        String s1 = key.substring(shiftby, keylen);
        String s2 = key.substring(0, shiftby);
        key = s1 + s2;
        return key;
    }

    public static String rounds(RandomAccessFile in, RandomAccessFile out, String key, int shiftby, String mode) // round
    // Encryption/decryption
    {


        int round = 0, ch = 0;
        String roundname = "";
       /* System.out.println("=========================================================================");
        System.out.println("Enter Mode:\n1.FAST(2 Round Enc/Dec)\t\t--Estimated Time :: " + EstTime(in, 2)
                + " seconds (" + (EstTime(in, 2)) / 60 + " minutes)\n2.FASTER(4-R E/D)\t\t--Estimated Time :: "
                + EstTime(in, 4) + " seconds (" + (EstTime(in, 4)) / 60
                + " minutes)\n3.STANDARD(8-R E/D)\t\t--Estimated Time :: " + EstTime(in, 8) + " seconds ("
                + (EstTime(in, 8)) / 60 + " minutes)\n4.STANDARD-Plus(12-R E/D)\t--Estimated Time :: " + EstTime(in, 12)
                + " seconds (" + (EstTime(in, 12)) / 60 + " minutes)\n5.EXPRESS(16-R E/D)\t\t--Estimated Time :: "
                + EstTime(in, 16) + " seconds (" + (EstTime(in, 16)) / 60
                + " minutes)\n\t\tUse Same Mode for Decryption with which the File was ENcrypted!");
        System.out.println("=========================================================================");
       */

        //testing
        ch=1;

        if (ch == 1) {
            round = 2;
            roundname = "2 Round Enc/Dec";
        } else if (ch == 2) {
            round = 4;
            roundname = "4 Round Enc/Dec";
        } else if (ch == 3) {
            round = 8;
            roundname = "8 Round Enc/Dec";
        } else if (ch == 4) {
            round = 12;
            roundname = "12 Round Enc/Dec";
        } else if (ch == 5) {
            round = 16;
            roundname = "16 Round Enc/Dec";
        } else
            System.out.println("Invalid Option!");

        if (key.length() % 2 != 0)
            round--;

        for (int i = 1; i <= round; i++) // Apply Alternate rounds
        {

            if (i % 2 != 0) {
                System.out.println("\t\t\tROUND--" + i);
                funcData=funcData.concat("\nROUND--"+i+" : "+ Calendar.getInstance().getTime());
                key = functionSet.shiftKey(key, shiftby);
                functionSet.xor(in, out, key, mode, "Round-" + i);
            } else {
                System.out.println("\t\t\tROUND--" + i);
                funcData=funcData.concat("\nROUND--"+i+" : "+ Calendar.getInstance().getTime());
                key = functionSet.shiftKey(key, shiftby);
                functionSet.xor(out, in, key, mode, "Round-" + i);
            }
        }
        System.out.println("\t\t\t" + roundname + " Successfully Completed!");
        funcData=funcData.concat("\n\t\t\t" + roundname + " Successfully Completed!");
        return funcData;

    }

    public static void copyFile(String source, String dest) throws IOException // using File channel=>faster method
    {
        File src = new File(source);
        File dst = new File(dest);
        FileChannel sourceCh = null;
        FileChannel destCh = null;
        try {
            sourceCh = new FileInputStream(src).getChannel();
            destCh = new FileOutputStream(dst).getChannel();
            destCh.transferFrom(sourceCh, 0, sourceCh.size());

        } finally {
            sourceCh.close();
            destCh.close();
        }
    }


}
