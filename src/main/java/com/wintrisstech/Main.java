package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2021 Dan Farris
 * version 211031
 * Build .dmg with
 * jpackage --verbose --name SmartPack --input target --main-jar Covers.jar --main-class com.wintrisstech.Main.class
 *******************************************************************/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
public class Main extends JComponent
{
    private static String version = "211031";
    private String nflRandomWeekURL = "https://www.covers.com/sports/nfl/matchups";
    private XSSFWorkbook sportDataWorkbook;
    private String deskTopPath = "/Users/vicwintriss/git/Covers/SportData.xlsx"; //System.getProperty("user.home") + "/Desktop";/* User's desktop path */
    private HashMap<String, String> weekList = new HashMap<>();
    private HashMap<String,String> cityNameMap = new HashMap<>();
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
    private Elements thisMatchupOddsElements;
    private Elements oddsElements;
    private Elements american;

    public static void main(String[] args) throws IOException, ParseException
    {
        System.out.println("SharpMarkets, version " + version + ", Copyright 2021 Dan Farris");
        Main main = new Main();
        main.initialize();//Get out of static context
    }
    private void initialize() throws IOException
    {
        fillCityNameMap();//Correct for Covers variations in team city names
        dataCollector.setCityNameMap(cityNameMap);
        ArrayList<String> thisSeasonDates = new ArrayList<>();
        fillSeasonDatesList(thisSeasonDates);//Puts all week dates into thisSeasonDates
        dataCollector.setThisSeason(thisSeason);//2021 for now TODO:make user selectable
        nflHistoryElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups");//Gets all NFL season beginning date history and this season year info.
        dataCollector.collectSeasonInfo(nflHistoryElements);//Builds HashMaps and ArrayLists for this season from Covers.com
        //for (String thisWeekDate : thisSeasonDates)//Process all matchups in this season....OUTER LOOP
        {
            String thisWeekDate = JOptionPane.showInputDialog("Enter NFL week code");
            thisWeekDate = "2021-10-14";
            System.out.println("47 Main NEW WEEK => " + thisWeekDate);
            thisWeekElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + thisWeekDate);//Get all of this week's games info
            dataCollector.collectThisWeekMatchups(thisWeekElements);
            oddsElements = webSiteReader.readCleanWebsite("https://www.covers.com/sport/football/nfl/odds");//Info as of the NFL week effective when logging into Covers
            Document oddsDoc = webSiteReader.getDirtyDoc();
            OddsCollector.collectThisWeekOdds(thisWeekDate, oddsElements, oddsDoc);
            //sportDataWorkbook = excelReader.readSportData();
            ArrayList<String> thisWweekMatchupIDs = dataCollector.getThisWeekMatchupIDs();
            //for (String thisMatchupId : thisWweekMatchupIDs)////Process all matchups in this week...INNER LOOP
            {
                String thisMatchupId = "83428";
                System.out.println(dataCollector.getGameIdentifierMap().get(thisMatchupId) + ", matchupID => " + thisMatchupId);
                thisMatchupConsensusElements = webSiteReader.readCleanWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + thisMatchupId);
                dataCollector.collectConsensusData(thisMatchupConsensusElements, thisMatchupId);
                excelBuilder.setThisWeekAwayTeamsMap(dataCollector.getThisWeekAwayTeamsMap());
                excelBuilder.setHomeTeamsMap(dataCollector.getThisWeekHomeTeamsMap());
                excelBuilder.setGameDatesMap(dataCollector.getGameDatesMap());
                excelBuilder.setAtsHomesMap(dataCollector.getAtsHomesMap());
                excelBuilder.setAtsAwaysMap(dataCollector.getAtsAwaysMap());
                excelBuilder.setOuOversMap(dataCollector.getOuOversMap());
                excelBuilder.setOuUndersMap(dataCollector.getOuUndersMap());
                excelBuilder.setCompleteHomeTeamName(dataCollector.getHomeTeamCompleteName());
                excelBuilder.setCompleteAwayTeamName(dataCollector.getAwayTeamCompleteName());
                excelBuilder.setGameIdentifier(dataCollector.getGameIdentifierMap().get(thisMatchupId));
                excelBuilder.buildExcel(sportDataWorkbook, thisMatchupId, globalMatchupIndex, dataCollector.getGameIdentifierMap().get(thisMatchupId));
                thisMatchupOddsElements = webSiteReader.readCleanWebsite("https://www.covers.com/sport/football/nfl/odds");
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
        thisSeasonDates.add("2021-09-09");//Season start...Week 1
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
    private void fillCityNameMap()
    {
        cityNameMap.put("Minneapolis", "Minnesota");//Minnesota Vikings
        cityNameMap.put("Tampa", "Tampa Bay");//Tampa Bay Buccaneers
        cityNameMap.put("Tampa Bay", "Tampa Bay");//Tampa Bay Buccaneers
        cityNameMap.put("Arlington", "Dallas");//Dallas Cowboys
        cityNameMap.put("Dallas", "Dallas");//Dallas Cowboys
        cityNameMap.put("Orchard Park", "Buffalo");//Buffalo Bills
        cityNameMap.put("Buffalo", "Buffalo");//Buffalo Bills
        cityNameMap.put("Charlotte", "Carolina");//Carolina Panthers
        cityNameMap.put("Carolina", "Carolina");//Carolina Panthers
        cityNameMap.put("Arizona", "Arizona");//Arizona Cardinals
        cityNameMap.put("Tempe", "Arizona");//Arizona Cardinals
        cityNameMap.put("Foxborough", "New England");//New England Patriots
        cityNameMap.put("New England", "New England");//New England Patriots
        cityNameMap.put("East Rutherford", "New York");//New York Giants and New York Jets
        cityNameMap.put("New York", "New York");//New York Giants and New York Jets
        cityNameMap.put("Landover", "Washington");//Washington Football Team
        cityNameMap.put("Washington", "Washington");//Washington Football Team
        cityNameMap.put("Nashville", "Tennessee");//Tennessee Titans
        cityNameMap.put("Miami", "Miami");//Miami Dolphins
        cityNameMap.put("Baltimore", "Baltimore");//Baltimore Ravens
        cityNameMap.put("Cincinnati", "Cincinnati");//Cincinnati Bengals
        cityNameMap.put("Cleveland", "Cleveland");//Cleveland Browns
        cityNameMap.put("Pittsburgh", "Pittsburgh");//Pittsburgh Steelers
        cityNameMap.put("Houston", "Houston");//Houston Texans
        cityNameMap.put("Indianapolis", "Indianapolis");//Indianapolis Colts
        cityNameMap.put("Jacksonville", "Jacksonville");//Jacksonville Jaguars
        cityNameMap.put("Tennessee", "Tennessee");//Tennessee Titans
        cityNameMap.put("Denver", "Denver");//Denver Broncos
        cityNameMap.put("Kansas City", "Kansas City");//Kansas City Chiefs
        cityNameMap.put("Las Vegas", "Las Vegas");//Los Angeles Chargers and Los angeles Rams
        cityNameMap.put("Philadelphia", "Philadelphia");//Philadelphia Eagles
        cityNameMap.put("Chicago", "Chicago");//Chicago Bears
        cityNameMap.put("Detroit", "Detroit");//Detroit Lions
        cityNameMap.put("Green Bay", "Green Bay");//Green Bay Packers
        cityNameMap.put("Minnesota", "Minnesota");
        cityNameMap.put("Atlanta", "Atlanta");//Atlanta Falcons
        cityNameMap.put("New Orleans", "New Orleans");//New Orleans Saints
        cityNameMap.put("Los Angeles", "Los Angeles");//Los Angeles Rams
        cityNameMap.put("San Francisco", "San Francisco");//San Francisco 49ers
        cityNameMap.put("Seattle", "Seattle");//Seattle Seahawks

    }

}
