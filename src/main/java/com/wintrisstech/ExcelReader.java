package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 211211
 * Read large SportData excel work book (SportData.xlsx) on user's desktop and return workBook
 *******************************************************************/

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
public class ExcelReader
{
    private String deskTopPath = "/Users/vicwintriss/Desktop/SportData.xlsx";
    private XSSFWorkbook sportDataWorkbook;
    private InputStream is;
    public XSSFWorkbook readSportData()
    {
        try
        {
            is = new FileInputStream(deskTopPath);
            ZipSecureFile.setMinInflateRatio(0);//To prevent zip bomb exception
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