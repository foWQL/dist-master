package com.pansky.user.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Fo
 * @date 2022/12/11 16:12
 */
public class ReadTxtUtil {
    public  static String getLine(String filePath) throws FileNotFoundException
    {
        String result = null;

        File file = new File(filePath);

        Random rand = new Random();
        int n = 0;
        for(Scanner sc = new Scanner(file); sc.hasNext(); )
        {
            ++n;
            String line = sc.nextLine();
            if(rand.nextInt(n) == 0)
                result = line;
        }
        return result;
    }
}
