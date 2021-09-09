package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2021 Dan Farris
 * version 210907
 * * Launch with Covers.command
 *******************************************************************/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.select.Elements;
import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
public class Main extends JComponent
{
    private static String version = "210907";
    private String nflRandomWeekURL = "https://www.covers.com/sports/nfl/matchups";
    private XSSFWorkbook sportDataWorkbook;
    private String deskTopPath = "/Users/vicwintriss/git/Covers/SportData.xlsx"; //System.getProperty("user.home") + "/Desktop";/* User's desktop path */
    private HashMap<String, String> weekList = new HashMap<>();
    public DataCollector dataCollector = new DataCollector();
    public WebSiteReader webSiteReader = new WebSiteReader();
    public ExcelReader excelReader = new ExcelReader();
    public ExcelBuilder excelBuilder = new ExcelBuilder();
    public ExcelWriter excelWriter = new ExcelWriter();
    private Elements thisWeekElements;
    private Elements nflHistoryElements;
    private Elements thisMatchupConsensusElements;
    private int globalMatchupIndex = 3;
    private String thisSeason = "2021";
    public static void main(String[] args) throws IOException, ParseException
    {
        System.out.println("SharpMarkets, version " + version + ", Copyright 2021 Dan Farris");
        Main main = new Main();
        main.initialize();//Get out of static context
    }
    private void initialize() throws IOException
    {
        ArrayList<String> thisSeasonDates = new ArrayList<>();
        fillSeasonDatesList(thisSeasonDates);//Puts all week dates into thisSeasonDates
        dataCollector.setThisSeason(thisSeason);//2021 for now TODO:make user selectable
        nflHistoryElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups");//Gets all NFL season beginning date history and this season year info.
        dataCollector.collectSeasonInfo(nflHistoryElements);//Builds HashMaps and ArrayLists for this season from Covers.com
        for (String thisWeekDate : thisSeasonDates)//Process all matchups in this season....OUTER LOOP
        {
            System.out.println("************************************** NEW NFL WEEK => " + thisWeekDate + ", NFL SEASON => " + thisSeason + " ***************************************");
            thisWeekElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + thisWeekDate);//Get all of this week's games info
            dataCollector.collectThisWeekMatchups(thisWeekElements);
            sportDataWorkbook = excelReader.readSportData();
            for (String thisMatchupID : dataCollector.getThisWeekMatchupIDs())////Process all matchups in this week...INNER LOOP
            {
                thisMatchupConsensusElements = webSiteReader.readCleanWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + thisMatchupID);
                dataCollector.collectConsensusData(thisMatchupConsensusElements, thisMatchupID);
                excelBuilder.setThisWeekAwayTeamsMap(dataCollector.getThisWeekAwayTeamsMap());
                excelBuilder.setHomeTeamsMap(dataCollector.getThisWeekHomeTeamsMap());
                excelBuilder.setGameDatesMap(dataCollector.getGameDatesMap());
                excelBuilder.setAtsHomesMap(dataCollector.getAtsHomesMap());
                excelBuilder.setAtsAwaysMap(dataCollector.getAtsAwaysMap());
                excelBuilder.setOuOversMap(dataCollector.getOuOversMap());
                excelBuilder.setOuUndersMap(dataCollector.getOuUndersMap());
                excelBuilder.setCompleteHomeTeamName(dataCollector.getHomeTeamCompleteName());
                excelBuilder.setCompleteAwayTeamName(dataCollector.getAwayTeamCompleteName());
                excelBuilder.setGameIdentifier(dataCollector.getGameIdentifierMap().get(thisMatchupID));
                excelBuilder.buildExcel(sportDataWorkbook, thisMatchupID, globalMatchupIndex, dataCollector.getGameIdentifierMap().get(thisMatchupID));
                globalMatchupIndex++;
            }
            excelWriter.openOutputStream();
            excelWriter.writeSportData(sportDataWorkbook);
            excelWriter.closeOutputStream();
        }
        System.out.println("Proper Finish...HOORAY!");
    }
    private void fillSeasonDatesList(ArrayList<String> thisSeasonDates)
    {
        thisSeasonDates.add("2021-09-09");//Season start
        thisSeasonDates.add("2021-09-16");
        thisSeasonDates.add("2021-09-23");
        thisSeasonDates.add("2021-09-30");
        thisSeasonDates.add("2021-10-07");
        thisSeasonDates.add("2021-10-14");
        thisSeasonDates.add("2021-10-21");
        thisSeasonDates.add("2021-10-28");
        thisSeasonDates.add("2021-11-04");
        thisSeasonDates.add("2021-11-11");
        thisSeasonDates.add("2021-11-18");
        thisSeasonDates.add("2021-11-25");
        thisSeasonDates.add("2021-12-02");
        thisSeasonDates.add("2021-12-09");
        thisSeasonDates.add("2021-12-16");
        thisSeasonDates.add("2021-12-23");
        thisSeasonDates.add("2022-01-02");
        thisSeasonDates.add("2022-01-09");
        thisSeasonDates.add("2022-02-06");
    }
}
