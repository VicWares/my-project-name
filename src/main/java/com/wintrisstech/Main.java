package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2021 Dan Farris
 * version 210902
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
    private static String version = "210905";
    private String nflRandomWeekURL = "https://www.covers.com/sports/nfl/matchups";
    private XSSFWorkbook sportDataWorkbook;
    private String deskTopPath = "/Users/vicwintriss/git/Covers/SportData.xlsx"; //System.getProperty("user.home") + "/Desktop";/* User's desktop path */
    private HashMap<String, String> weekList = new HashMap<>();
    //private InfoPrinter infoPrinter = new InfoPrinter();
    public DataCollector dataCollector = new DataCollector();
    public WebSiteReader webSiteReader = new WebSiteReader();
    public SportDataReader sportDataReader = new SportDataReader();
    public Aggregator aggregator = new Aggregator();
    public SportDataWriter sportDataWriter = new SportDataWriter();
    private Elements thisWeekElements;
    private Elements nflHistoryElements;
    private Elements thisMatchupConsensusElements;
    private int globalMatchupIndex = 3;
    private String thisSeason = "2021";
    private String completeAwayTeamName;
    private String completeHomeTeamName;
    public static void main(String[] args) throws IOException, ParseException
    {
        System.out.println("(1) Starting SharpMarkets, version " + version + ", Copyright 2021 Dan Farris");
        Main main = new Main();
        main.initialize();//Get out of static context
    }
    private void initialize() throws IOException
    {
        ArrayList<String> thisSeasonDates = new ArrayList<>();
        fillSeasonDatesList(thisSeasonDates);//Puts all week dates into thisSeasonDates
        dataCollector.setThisSeason(thisSeason);
        nflHistoryElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + thisSeason);//Gets all NFL season beginning date history and this season year info.
        dataCollector.collectAllSeasonDates(nflHistoryElements);//Builds a String array of all past and current NFL season year dates available from Covers.com
        for (String thisWeekDate : thisSeasonDates)//Process all matchups in this season
        {
            System.out.println("************************************** NEW NFL WEEK => " + thisWeekDate + ", NFL SEASON => " + thisSeason + " ***************************************");
            processAllWeeks(thisWeekDate);
        }
        System.out.println("Proper Finish...HOORAY!");
    }
    private void processAllWeeks(String thisWeek) throws IOException
    {
        thisWeekElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + thisWeek);//Get all of this week's games info
        dataCollector.collectThisSeasonWeeks(nflHistoryElements);
        dataCollector.collectThisWeekMatchups(thisWeekElements);
        sportDataWorkbook = sportDataReader.readSportData();
        for (String s : dataCollector.getThisWeekMatchupIDs())
        {
            String thisMatchupID = s;
            processAllMatchups(thisMatchupID);
        }
        sportDataWriter.writeSportData(sportDataWorkbook);
    }
    private void processAllMatchups(String thisMatchupID) throws IOException
    {

        thisMatchupConsensusElements = webSiteReader.readCleanWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + thisMatchupID);
        dataCollector.collectConsensusData(thisMatchupConsensusElements, thisMatchupID);
        aggregator.setThisWeekAwayTeamsMap(dataCollector.getThisWeekAwayTeamsMap());
        aggregator.setThisWeekHomeTeamsMap(dataCollector.getThisWeekHomeTeamsMap());
        aggregator.setThisWeekGameDatesMap(dataCollector.getThisWeekGameDatesMap());
        aggregator.setAtsHomesMap(dataCollector.getAtsHomesMap());
        aggregator.setAtsAwaysMap(dataCollector.getAtsAwaysMap());
        aggregator.setOuOversMap(dataCollector.getOuOversMap());
        aggregator.setOuUndersMap(dataCollector.getOuUndersMap());
        aggregator.buildSportDataUpdate(sportDataWorkbook, thisMatchupID, globalMatchupIndex);
        aggregator.setCompleteHomeTeamName(dataCollector.getHomeTeamCompleteName());
        aggregator.setCompleteAwayTeamName(dataCollector.getAwayTeamCompleteName());
        globalMatchupIndex++;
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
