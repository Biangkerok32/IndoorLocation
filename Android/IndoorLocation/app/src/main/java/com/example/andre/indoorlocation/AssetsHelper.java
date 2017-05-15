package com.example.andre.indoorlocation;


import android.content.Context;
import android.graphics.Path;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by doom on 15/4/2.
 */
public class AssetsHelper
{
    public static String getContent(Context context, Path fileName)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;

            System.out.print(Result);
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}