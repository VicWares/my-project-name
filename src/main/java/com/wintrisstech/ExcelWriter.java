package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 220203
 * write new NFL Covers data to the large SportData Excel sheet
 *******************************************************************/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
public class ExcelWriter
{
    private OutputStream os;
    public void writeSportData(XSSFWorkbook sportDataWorkbook, String currentDir)
    {
        String deskTopPath = currentDir + "/target/SportData.xlsx";
        System.out.println("EW20 Writing to desktop");
        try
        {
            sportDataWorkbook.write(os);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void closeOutputStream(String currentDir) throws IOException
    {
        os.close();
    }
    public void openOutputStream(String currentDir) throws FileNotFoundException
    {
        os = new FileOutputStream(currentDir + "/target/SportData.xlsx");
    }
}
