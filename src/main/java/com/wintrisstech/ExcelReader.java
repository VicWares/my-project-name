package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 220120
 * Read large SportData excel work book (SportData.xlsx) on user's desktop and return workBook
 *******************************************************************/
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
public class ExcelReader
{
    private String deskTopPath = "target.SportData.xlsx";
    private XSSFWorkbook sportDataWorkbook;
    private InputStream is;
    public XSSFWorkbook readSportData(String currentDir)
    {
        try
        {
            is = new FileInputStream(currentDir + "/target/SportData.xlsx");
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