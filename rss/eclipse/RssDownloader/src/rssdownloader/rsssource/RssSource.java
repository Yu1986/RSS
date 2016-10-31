/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.rsssource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jie Yu
 */
public class RssSource {
    private String name; // the unique name for this Rss Source
    private String url;	 // the RSS URL for this Rss Source
    private String category; // the category this RSS belong to
    private String lastBuildDate; // the last build date for one release
    private String titlePrefix; // the RSS Items in some RSS source has fixed prefix
    							// we can use this parameter to remove prefix when downloading
    private long rssItemNum; // how many RSS Items have been downloaded from this source

    public RssSource(String name, String url, String category, String prefix) {
        this.name = name;
        this.url = url;
        this.category = category;
        this.lastBuildDate = "";
        this.titlePrefix = prefix;
        rssItemNum = 0;
    }
    
    public void printInfo() {
        //System.out.println("name: " + name);
        System.out.println("url: " + url);
    }
    
    public String get_url() {
        return url;
    }
    
    public String get_name() {
        return name;
    }
    
    public String get_category() {
        return category;
    }
    
    public String get_prefix() {
        return titlePrefix;
    }
    
    public boolean isLastBuildDateSame(String dateStr) {
        //System.out.println("Last Build Date: " + dateStr);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss"); 
            Date date = simpleDateFormat.parse(dateStr);
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
        if (lastBuildDate.equals(dateStr)) {
            return true;
        } else {
            lastBuildDate = dateStr;
            return false;
        }
    }
    
    public void incRssItemNum() {
        rssItemNum++;
    }
    
    public long getRssItemNum() {
        return rssItemNum;
    }
    
}
