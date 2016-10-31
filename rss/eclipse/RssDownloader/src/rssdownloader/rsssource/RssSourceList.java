/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.rsssource;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Jie Yu
 */
public class RssSourceList {
    private LinkedList rssList;
    private Iterator rssListItr;

    public RssSourceList() {
        rssList = new LinkedList();
      
        /*
        rssList.add(new RssSource("cnn.com",
                "http://rss.cnn.com/rss/edition_business360.rss",
                "General",
                ""));
        
        rssList.add(new RssSource("usatoday.com",
                "http://rssfeeds.usatoday.com/UsatodaycomMoney-Healey",
                "General",
                "Test Drive:"));
        
        rssList.add(new RssSource("marketwatch.com",
                "http://feeds.marketwatch.com/marketwatch/financial/",
                "General",
                ""));
        */
        /*
        addsc cnn.com http://rss.cnn.com/rss/edition_business360.rss General
        addsc usatoday.com http://rssfeeds.usatoday.com/UsatodaycomMoney-Healey General
        addsc marketwatch.com http://feeds.marketwatch.com/marketwatch/financial/ General
        rssList.add(new RssSource("cnn.com",
                "http://feeds2.feedburner.com/time/nation",
                "General",
                0));
        */
    }
    
    
    
    public void setFirstRssSource(){
        rssListItr = rssList.iterator();
    }
    
    public RssSource getNextRssSource() {
        if (rssListItr.hasNext()) {
            return (RssSource)rssListItr.next();
        } else {
            return null;
        }
    }
    
    public String getRssSourceList() {
        Iterator itr = rssList.iterator();
        String infor = "";
        while (itr.hasNext()) {
            RssSource rssSource = (RssSource)itr.next();
            infor += rssSource.get_name() + ":\n";
        }
        
        return infor;
    }
    
    public RssSource addRssSource(String name, String url, String category, String prefix) {
        Iterator itr = rssList.iterator();
        while (itr.hasNext()) {
            RssSource rssSource = (RssSource)itr.next();
            if (rssSource.get_name().equals(name)) {
                return null;
            }
        }
        RssSource newSource = new RssSource(name, url, category, prefix);
        rssList.add(newSource);
        return newSource;
        
    }
    
    public boolean rmRssSource(String name) {
        Iterator itr = rssList.iterator();
        while (itr.hasNext()) {
            RssSource rssSource = (RssSource)itr.next();
            if (rssSource.get_name().equals(name)) {
                rssList.remove(rssSource);
                return true;
            }
        }
        return false;
    }
    
    public String showStatus() {
        String statusStr = "";
        
        Iterator itr = rssList.iterator();
        while (itr.hasNext()) {
            RssSource rssSource = (RssSource)itr.next();
            statusStr += "The RSS Source \"" + rssSource.get_name() + "\" has " 
                    + rssSource.getRssItemNum() + " RSS Items in the Hash Table\n";
        }
        
        return statusStr;
    }
}
