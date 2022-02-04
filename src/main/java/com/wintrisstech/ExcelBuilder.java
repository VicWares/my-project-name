package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 220203
 * *******************************************************************/
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT;
public class ExcelBuilder
{
    private String thisSeason;
    private String ouUnder;
    private String ouOver;
    private String homeTeam;
    private String awayTeam;
    private String thisMatchupDate;
    private HashMap<String, String> homeTeamsMap = new HashMap<>();
    private HashMap<String, String> awayTeamsMap = new HashMap<>();
    private HashMap<String, String> gameDatesMap = new HashMap<>();
    private HashMap<String, String> atsHomesMap = new HashMap<>();
    private HashMap<String, String> atsAwaysMap = new HashMap<>();
    private HashMap<String, String> ouOversMap;
    private HashMap<String, String> ouUndersMap;
    private HashMap<String, String> homeMLOddsMap = new HashMap<>();
    private Sheet sportDataSheet;
    private XSSFWorkbook sportDataWorkBook = new XSSFWorkbook();
    private XSSFSheet sportDataUpdateSheet = null;
    byte[] redColor = new byte[]{(byte) 255, (byte) 0, (byte) 0};
    Color color =  new Color(215,228,188);
    private String atsHome;
    private String atsAway;
    private String completeHomeTeamName;
    private String completeAwayTeamName;
    private String gameIdentifier;
    private String awayMoneyLineOdds;
    private String homeMoneyLineOdds;
    private String awaySpreadOdds;
    private String homeSpreadOdds;
    private HashMap<String, String> homeMoneyLineOddsMap = new HashMap<>();
    private HashMap<String, String> awayMoneyLineOddsMap = new HashMap<>();
    private HashMap<String, String> homeSpreadOddsMap = new HashMap<>();
    private HashMap<String, String> awaySpreadOddsMap = new HashMap<>();
    public XSSFWorkbook buildExcel(XSSFWorkbook sportDataWorkbook, String dataEventID, int globalEventIndex, String gameIdentifier)
    {
        sportDataSheet = sportDataWorkbook.getSheet("Data");
        String time = LocalDate.now() + " " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
        CellStyle leftStyle = sportDataWorkbook.createCellStyle();
        leftStyle.setAlignment(LEFT);
        CellStyle centerStyle = sportDataWorkbook.createCellStyle();
        centerStyle.setAlignment(CENTER);
        CellStyle myStyle = sportDataWorkbook.createCellStyle();
        XSSFCellStyle redStyle = sportDataWorkbook.createCellStyle();
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        sportDataSheet.setDefaultColumnStyle(0, leftStyle);
        sportDataSheet.setDefaultColumnStyle(1, centerStyle);
        sportDataSheet.createRow(globalEventIndex);
        sportDataSheet.setColumnWidth(1, 25 * 256);
        homeTeam = homeTeamsMap.get(dataEventID);
        awayTeam = awayTeamsMap.get(dataEventID);
        thisMatchupDate = gameDatesMap.get(dataEventID);
        atsHome = atsHomesMap.get(dataEventID);
        atsAway = atsAwaysMap.get(dataEventID);
        ouOver = ouOversMap.get(dataEventID);
        ouUnder = ouUndersMap.get(dataEventID);
        sportDataSheet.getRow(globalEventIndex).createCell(0);
        sportDataSheet.getRow(globalEventIndex).getCell(0).setCellStyle(leftStyle);
        sportDataSheet.getRow(0).getCell(0).setCellValue(time);
        sportDataSheet.getRow(globalEventIndex).getCell(0).setCellValue(gameIdentifier);//e.g. 2021 - Washington Football Team @ Dallas Cowboys
        System.out.println("EB81 " + globalEventIndex + " " + gameIdentifier);

        sportDataSheet.getRow(globalEventIndex).createCell(1);
        sportDataSheet.getRow(globalEventIndex).getCell(1).setCellStyle(centerStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(1).setCellValue(thisMatchupDate);

        sportDataSheet.getRow(globalEventIndex).createCell(12);//Spread home odds, column M
        sportDataSheet.getRow(globalEventIndex).getCell(12).setCellStyle(centerStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(12).setCellValue(homeSpreadOddsMap.get(dataEventID));

        sportDataSheet.getRow(globalEventIndex).createCell(17);//MoneyLine Bet365 home odds, column R
        sportDataSheet.getRow(globalEventIndex).getCell(17).setCellStyle(centerStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(17).setCellValue(homeMoneyLineOddsMap.get(dataEventID));

        sportDataSheet.getRow(globalEventIndex).createCell(26);//Spread away odds, column AA
        sportDataSheet.getRow(globalEventIndex).getCell(26).setCellStyle(centerStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(26).setCellValue(awaySpreadOddsMap.get(dataEventID));

        sportDataSheet.getRow(globalEventIndex).createCell(31);//MoneyLine Bet365 away odds, column AF
        sportDataSheet.getRow(globalEventIndex).getCell(31).setCellStyle(centerStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(31).setCellValue(awayMoneyLineOddsMap.get(dataEventID));

        sportDataSheet.getRow(globalEventIndex).createCell(59);
        sportDataSheet.getRow(globalEventIndex).getCell(59).setCellStyle(myStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(59).setCellValue(atsHome);

        sportDataSheet.getRow(globalEventIndex).createCell(61);
        sportDataSheet.getRow(globalEventIndex).getCell(61).setCellStyle(myStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(61).setCellValue(atsAway);

        sportDataSheet.getRow(globalEventIndex).createCell(64);
        sportDataSheet.getRow(globalEventIndex).getCell(64).setCellStyle(myStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(64).setCellValue(ouOver);

        sportDataSheet.getRow(globalEventIndex).createCell(66);
        sportDataSheet.getRow(globalEventIndex).getCell(66).setCellStyle(myStyle);
        sportDataSheet.getRow(globalEventIndex).getCell(66).setCellValue(ouUnder);
        return sportDataWorkbook;
    }
    public void setHomeTeamsMap(HashMap<String, String> homeTeamsMap){this.homeTeamsMap = homeTeamsMap;}
    public void setThisWeekAwayTeamsMap(HashMap<String, String> thisWeekAwayTeamsMap){this.awayTeamsMap = thisWeekAwayTeamsMap;}
    public void setGameDatesMap(HashMap<String, String> gameDatesMap) {this.gameDatesMap = gameDatesMap;}
    public void setAtsHomesMap(HashMap<String, String> atsHomes)
    {
        this.atsHomesMap = atsHomes;
    }
    public void setAtsAwaysMap(HashMap<String, String> atsAwayMap)
    {
        this.atsAwaysMap = atsAwayMap;
    }
    public void setOuOversMap(HashMap<String, String> ouOversMap){this.ouOversMap = ouOversMap;}
    public void setOuUndersMap(HashMap<String, String> ouUndersMap)
    {
        this.ouUndersMap = ouUndersMap;
    }
    public void setCompleteHomeTeamName(String completeHomeTeamName){this.completeHomeTeamName = completeHomeTeamName;}
    public void setCompleteAwayTeamName(String completeAwayTeamName){this.completeAwayTeamName = completeAwayTeamName;}
    public void setGameIdentifier(String gameIdentifier){this.gameIdentifier = gameIdentifier;}
    public void setHomeMLOddsMap(HashMap<String, String> homeMLOddsMap)
    {
        this.homeMLOddsMap = homeMLOddsMap;
    }
    public void setMoneyLineOdds(String moneyLineOdds, String dataEventId)
    {
        String[] moneyLineOddsArray = moneyLineOdds.split(" ");
        if (moneyLineOddsArray.length > 0)
      {
          awayMoneyLineOdds = moneyLineOddsArray[0];
          awayMoneyLineOddsMap.put(dataEventId, awayMoneyLineOdds);
          homeMoneyLineOdds = moneyLineOddsArray[1];
          homeMoneyLineOddsMap.put(dataEventId, homeMoneyLineOdds);
      }
    }
    public void setSpreadOdds(String spreadOdds, String dataEventId)
    {
        String[] spreadOddsArray = spreadOdds.split(" ");
        if (spreadOddsArray.length > 0)
        {
            awaySpreadOdds = spreadOddsArray[0];
            awaySpreadOddsMap.put(dataEventId, awayMoneyLineOdds);
            homeSpreadOdds = spreadOddsArray[1];
            homeSpreadOddsMap.put(dataEventId, homeMoneyLineOdds);
        }
    }
}
