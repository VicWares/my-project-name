package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2021 Dan Farris
 * version 211211
 * Build .dmg with
 * jpackage --verbose --name SmartPack --input target --main-jar Covers.jar --main-class com.wintrisstech.Main.class
 *******************************************************************/

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class Main extends JComponent
{
    private static String version = "211211";
    private XSSFWorkbook sportDataWorkbook;
    private HashMap<String, String> weekNumberMap = new HashMap<>();
    private HashMap<String, String> cityNameMap = new HashMap<>();
    private HashMap<String, String> xRefMap = new HashMap<>();
    public DataCollector dataCollector = new DataCollector();
    public WebSiteReader webSiteReader = new WebSiteReader();
    public ExcelReader excelReader = new ExcelReader();
    public ExcelBuilder excelBuilder = new ExcelBuilder();
    public ExcelWriter excelWriter = new ExcelWriter();
    private Elements weekElements;
    private Elements seasonMatchupElements;
    private Elements consensusElements;
    private int globalMatchupIndex = 3;
    private Elements oddsElements;
    private String MLhomeOdds;
    private String MLawayOdds;

    public static void main(String[] args) throws IOException, ParseException
    {
        System.out.println("SharpMarkets, version " + version + ", Copyright 2021 Dan Farris");
        Main main = new Main();
        main.initialize();//Get out of static context
    }

    private void initialize() throws IOException
    {
        fillCityNameMap();//Builds full city name map to correct for Covers variations in team city names
        fillWeekNumberMap();
        dataCollector.setCityNameMap(cityNameMap);
        String weekNumber = JOptionPane.showInputDialog("Enter NFL week number");
        weekNumber = "15";
        String weekDate = weekNumberMap.get(weekNumber);
        System.out.println("Main46..................................................... week number => " + weekNumber);
        Elements nflElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups");
        Elements weekElements = nflElements.select(".cmg_game_data, .cmg_matchup_game_box");
        System.out.println("Main49*****************************************************data-game elements for week.size() => " + weekElements.size());
        xRefMap = buildXref(weekElements);
        dataCollector.collectThisWeekMatchups(weekElements);
        sportDataWorkbook = excelReader.readSportData();
        oddsElements = webSiteReader.readCleanWebsite("https://www.covers.com/sport/football/nfl/odds");//Info from log-in date through the present NFL week
        dataCollector.collectThisWeekOdds(oddsElements, xRefMap);
        System.out.println("M65 homeOdds " + dataCollector.getMLhomeOdds() + " awayOdds " + dataCollector.getMLawayOdds());
        //for (String matchup : matchuplist)////Process all matchups in this week...INNER LOOP*******************************************INNNER LOOP**********SELECT INDIVIDUAL MATCHUP ID FOR PROCESSING*******************************************************************INNER LOOP
        {
            String matchup = "83592";
            System.out.println("***INNNER LOOP*****matchup => " + matchup);
            System.out.println("\n*> Main61, working new game: " + dataCollector.getGameIdentifierMap().get(matchup) + ", ID => " + matchup + ", Date => " + dataCollector.getGameDatesMap().get(matchup));
            System.out.println("Main62*************************************************************************************************************");
            consensusElements = webSiteReader.readCleanWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + matchup);
            dataCollector.collectConsensusData(consensusElements, matchup);
            excelBuilder.setThisWeekAwayTeamsMap(dataCollector.getThisWeekAwayTeamsMap());
            excelBuilder.setHomeTeamsMap(dataCollector.getThisWeekHomeTeamsMap());
            excelBuilder.setGameDatesMap(dataCollector.getGameDatesMap());
            excelBuilder.setAtsHomesMap(dataCollector.getAtsHomesMap());
            excelBuilder.setAtsAwaysMap(dataCollector.getAtsAwaysMap());
            excelBuilder.setOuOversMap(dataCollector.getOuOversMap());
            excelBuilder.setOuUndersMap(dataCollector.getOuUndersMap());
            excelBuilder.setCompleteHomeTeamName(dataCollector.getHomeTeamCompleteName());
            excelBuilder.setCompleteAwayTeamName(dataCollector.getAwayTeamCompleteName());
            excelBuilder.setGameIdentifier(dataCollector.getGameIdentifierMap().get(matchup));
            excelBuilder.buildExcel(sportDataWorkbook, matchup, globalMatchupIndex, dataCollector.getGameIdentifierMap().get(matchup));
            globalMatchupIndex++;
        }
        excelWriter.openOutputStream();
        excelWriter.writeSportData(sportDataWorkbook);
        excelWriter.closeOutputStream();
        System.out.println("Proper Finish...HOORAY!");
    }

    public HashMap<String, String> buildXref(Elements weekElements)
    {
        for (Element e : weekElements) {
            String dataLinkString = e.attr("data-link");
            String[] dlsa = dataLinkString.split("/");
            String dataLink = dlsa[5];
            String dataEvent = e.attr("data-event-id");
            xRefMap.put(dataEvent, dataLink);
        }
        return xRefMap;
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

    private void fillWeekNumberMap()
    {
        weekNumberMap.put("1", "2021-09-09");//Season start...Week 1
        weekNumberMap.put("2", "2021-09-16");
        weekNumberMap.put("3", "2021-09-23");
        weekNumberMap.put("4", "2021-09-30");
        weekNumberMap.put("5", "2021-10-07");
        weekNumberMap.put("6", "2021-10-14");
        weekNumberMap.put("7", "2021-10-21");
        weekNumberMap.put("8", "2021-10-28");
        weekNumberMap.put("9", "2021-11-04");
        weekNumberMap.put("10", "2021-11-11");
        weekNumberMap.put("11", "2021-11-18");
        weekNumberMap.put("12", "2021-11-25");
        weekNumberMap.put("13", "2021-12-02");
        weekNumberMap.put("14", "2021-12-09");
        weekNumberMap.put("15", "2021-12-16");
        weekNumberMap.put("16", "2021-12-23");
        weekNumberMap.put("17", "2022-01-02");
        weekNumberMap.put("18", "2022-01-09");
        weekNumberMap.put("19", "2022-02-06");
    }
}
