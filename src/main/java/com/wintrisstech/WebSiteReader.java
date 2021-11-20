package com.wintrisstech;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static org.jsoup.Jsoup.connect;

/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 2111120
 * Reads/cleans input URL and returns all Elements and Document
 *******************************************************************/
public class WebSiteReader
{
    private Document nflRandomMatchupsDoc;
    private Elements nflRandomMatchupsElements;
    private Document dirtyDoc;
    public Elements readCleanWebsite(String urlToRead) throws IOException
    {
        dirtyDoc = Jsoup.parse(String.valueOf(connect(urlToRead).get()));
        return getDirtyDoc().getAllElements();
    }
    public Document getDirtyDoc() {
        return dirtyDoc;
    }
}



