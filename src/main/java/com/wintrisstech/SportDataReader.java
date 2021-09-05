package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 210613A
 * Read large SportData excel work book (SportData.xlsx) on user's desktop and return workBook
 *******************************************************************/
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
public class SportDataReader
{
    private String deskTopPath = "/Users/vicwintriss/git/Covers/SportData.xlsx";//System.getProperty("user.home") + "/Desktop";/* User's desktop path */
    private XSSFWorkbook sportDataWorkbook;
    private InputStream is;
    public XSSFWorkbook readSportData()
    {
        try
        {
            System.out.println("xxxxxxxxxxxxx " + deskTopPath);
            is = new FileInputStream(deskTopPath);
            sportDataWorkbook = (XSSFWorkbook) WorkbookFactory.create(is);
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sportDataWorkbook;
    }
}