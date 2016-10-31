/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rssdownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSItem;
import org.gnu.stealthp.rsslib.RSSParser;
import rssdownloader.db.Db;
import rssdownloader.rssItem.RssItem;
import rssdownloader.rssItem.RssItemList;
import rssdownloader.rsssource.RssSource;
import rssdownloader.rsssource.RssSourceList;
import rssdownloader.simhash.SimHash;
import rssdownloader.utility.Html;
import rssdownloader.utility.Statistic;

/**
 *
 * @author Jie Yu
 */
public class DownloaderTimerTask extends Thread {
    
	public static long RSS_UPDATE_INTERVAL; 
	
	public boolean debug;
	public boolean sleep_flag;
	public boolean quit_flag;
	
    public static String CONFIG_FILE = "rss.cfg";
    public  Properties props;
    public boolean runFlag;
    
    public Db db;
    public RssSourceList rssSource;
    public RssItemList rssItemList;
    public Date startTime;
    
    public long rssNum;
	public long dupRssNum;
    
    private long sleepTimer;
    private Statistic statistic;
    

    public DownloaderTimerTask(boolean debug) {
    	this.debug = debug;
    	sleep_flag = false;
    	quit_flag = false;
        runFlag = true;
        loadCfg();
        connectDb();
        RSS_UPDATE_INTERVAL = 60 * 
                Integer.parseInt(props.getProperty("RssUpdateInterval"));
        rssSource = new RssSourceList();
        rssItemList = new RssItemList(
                Integer.parseInt(props.getProperty("RssSimilarDistance")),
                Integer.parseInt(props.getProperty("RssSameDistance")),
                Integer.parseInt(props.getProperty("RssHashTblCleanDelay")),
                db);
        loadRssSourceFromDb();
        statistic = new Statistic(this);
        statistic.loadStatistic();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); 
        //startTime = simpleDateFormat.format(new Date());
        startTime = new Date();
    }
    
    @Override
    public void run() {
        while (true) {
            if (runFlag) {
            	rssNum = 0;
                dupRssNum = 0;
            	eachRun();
            	statistic.updateStatisticMain(rssNum, dupRssNum);
            	if (quit_flag) {
            		System.exit(0);
            	}
                sleep_flag = true;
                sleepTimer = RSS_UPDATE_INTERVAL;
                try {
                    while (sleepTimer > 0) {
                        sleep(1000);
                        sleepTimer--;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(DownloaderTimerTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                sleep_flag = false;
            } else {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DownloaderTimerTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
    public void eachRun() {
        RssSource rssSourceField;
        RssItem rssItem;
        
        Locale.setDefault(Locale.US); 
        
        // Clean Hash Table
        rssItemList.cleanHashTable();
        
        rssSource.setFirstRssSource();
        while (((rssSourceField = rssSource.getNextRssSource()) != null) &&
        		(!quit_flag)) {
        	if (debug) {
        		Date now = new Date();
        		System.out.println("Retrieve:" + rssSourceField.get_name() + "(url: " +
            					rssSourceField.get_url() + ")");
        		System.out.println(now.toLocaleString());
        	}
        	
            try {
                RSSHandler remoteRSSHandler = new RSSHandler();
                RSSParser.parseXmlFile(new URL(rssSourceField.get_url()), remoteRSSHandler, false);

                RSSChannel channel = remoteRSSHandler.getRSSChannel();
                
                /**
                 * NOTICE:
                 *   We can use last build date to prevent duplicated retrieve, but dont how every RSS
                 *   source use this information, 
                 *   so for safe, disable this function temeperately.
                 *   the duplicated RSS can be discard by rss item processing
                 */
                //String lastBuildDate = channel.getLastBuildDate();
                //if (!rssSourceField.isLastBuildDateSame(lastBuildDate)) {
                if (true) {
                    //rssSourceField.printInfo();

                    List channelItems = channel.getItems(); 
                    int itemSize= channelItems.size(); 
                    if(itemSize >=1){
                        for (int i=0;i<itemSize;i++){
                            RSSItem item = (RSSItem)channelItems.get(i); 
                            
                            String itemAuthor = item.getAuthor(); 
                            String itemTitle = item.getTitle(); 
                            String itemDescription = item.getDescription(); 
                            String itemLink = item.getLink(); 
                            String itemPubDate = item.getPubDate(); 
                            
                            itemTitle = itemTitle.replaceFirst(rssSourceField.get_prefix(), "");
                            
                            String itemDescriptionRmTag = Html.removeTag(itemDescription);
                            
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z"); 
                            Date pubDate = simpleDateFormat.parse(itemPubDate);
                            Date downloadDate = new Date();
                            
                            String simHashRaw = itemTitle + ". " + itemDescriptionRmTag;
                            
                            long itemSimHash = SimHash.computeOptimizedSimHashForString(simHashRaw);

                            rssItem = new RssItem(itemTitle, itemDescriptionRmTag, itemAuthor, 
                                    itemLink, pubDate, downloadDate, itemSimHash, rssSourceField.get_name());
                            RssItemList.ItemAddReturn ret = rssItemList.addRssItem(rssItem);
                            if ((ret != RssItemList.ItemAddReturn.Error) &&
                            		(ret != RssItemList.ItemAddReturn.Same)) {
                                rssSourceField.incRssItemNum();
                                if (ret == RssItemList.ItemAddReturn.DupItem) {
                                	dupRssNum += 1;
                                }
                                rssNum += 1;
                            }
                        } 
                    }
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
            } catch (Throwable t) {
                //System.out.println(t.getMessage()+t.toString());
            }
        }
    }
    
    private void loadCfg() {
        String path =  new File("").getAbsolutePath();
        path = path + "/";

        try {
            props = new Properties();
            props.load(new FileInputStream(path + CONFIG_FILE));
        } catch (IOException ex) {
        }
        
        if (props.getProperty("DBHostName") == null) {
            // Load default setting
            props.setProperty("DBHostName", "localhost");
            props.setProperty("DBHostPort", "9160");
            props.setProperty("DBKeyspace", "RSS");
            props.setProperty("CmdPort", "10000");
            props.setProperty("RssUpdateInterval", "20");
            props.setProperty("RssSimilarDistance", "20");
            props.setProperty("RssSameDistance", "5");
            props.setProperty("RssHashTblCleanDelay", "72");
            storeCfg();
        }
    }
    
    private void storeCfg() {
        String path =  new File("").getAbsolutePath();
        path = path + "/";
        System.out.println("Load Database Config: " + path + CONFIG_FILE + "\n");

        try {
            props.store(new FileOutputStream(path + CONFIG_FILE), "");
        } catch (IOException ex) {
        }
    }
    
    private void connectDb() {
        db = new Db(props.getProperty("DBHostName"), 
                props.getProperty("DBHostPort"), 
                props.getProperty("DBKeyspace"));
    }
    
    private void loadRssSourceFromDb() {
        LinkedList rssSourceList = db.readRssSource();
        Iterator itr = rssSourceList.iterator();
        while (itr.hasNext()) {
            RssSource rssSrc = (RssSource) itr.next();
            rssSource.addRssSource(rssSrc.get_name(), rssSrc.get_url(), 
                    rssSrc.get_category(), rssSrc.get_prefix());
        }
    }
    
    public String getRssSourceList() {
        return rssSource.getRssSourceList();
    }
    
    public boolean addRssSource(String name, String url, String category, String prefix) {
        RssSource newSource = rssSource.addRssSource(name, url, category, prefix);
        if (newSource != null) {
            db.addRssSource(newSource);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean rmRssSource(String name) {
        db.rmRssSource(name);
        return rssSource.rmRssSource(name);
    }
    
    public String setHost(String hostname, String hostport, String keyspace) {
        if (db.connect(hostname, hostport, keyspace)) {
            props.setProperty("DBHostName", hostname);
            props.setProperty("DBHostPort", hostport);
            props.setProperty("DBKeyspace", keyspace);
            storeCfg();
            return "Set Database Host done\n";
        } else {
            return "Can not connect to the Host\n";
        }
    }
    
    public void setRssUpdateRate(String rssRate) {
        RSS_UPDATE_INTERVAL = 60 * Integer.parseInt(rssRate);
        props.setProperty("RssUpdateInterval", rssRate);
        storeCfg();
    }
    
    public void setCmdPort(String port) {
        props.setProperty("CmdPort", port);
        storeCfg();
    }
    
    public void setSimilarDistance(String distance) {
        rssItemList.setDistance(Integer.parseInt(distance));
        props.setProperty("RssSimilarDistance", distance);
        storeCfg();
    }
    
    public void setHashTableCleanDelay(String delay) {
        rssItemList.setCleanDelay(Integer.parseInt(delay));
        props.setProperty("RssHashTblCleanDelay", delay);
        storeCfg();
    }
    
    public String showConfig() {
        String statusStr = "";
        
        statusStr += "Command Interface Port: " + props.getProperty("CmdPort") + "\n";
        statusStr += "Database Server: " + props.getProperty("DBHostName") + "\n";
        statusStr += "Database Port: " + props.getProperty("DBHostPort") + "\n";
        statusStr += "Database Keyspace: " + props.getProperty("DBKeyspace") + "\n";
        statusStr += "Rss Source Update Rate: " + props.getProperty("RssUpdateInterval") + " minutes\n";
        statusStr += "Similar Rss Item Hamming Distance: " + props.getProperty("RssSimilarDistance") + "\n";
        statusStr += "RssItem Hash Table Flush Rate: " + props.getProperty("RssHashTblCleanDelay") + " hours\n";
        
        return statusStr;
    }
    
    public String showStatus() {
        String statusStr = "====    RssDownloader Status    ====\n";
        //statusStr += "Server Started Since: " + startTime + "\n";
        //statusStr += "Till now, the RSS Downloader has been run " + totalRunTime + " times\n";
        
        statusStr += showConfig();
        
        statusStr += rssItemList.showStatus();
        statusStr += rssSource.showStatus();
        
        return statusStr;
    }
    
    public String showStatusDb() {
        return db.showStatus();
    }
    
    public String listDuplicate() {
        return rssItemList.listDuplicate();
    }
    
    public String showDuplicate(long primarySimhash) {
        return rssItemList.showDuplicate(primarySimhash);
    }
    
    public void cleanSleepTimer() {
        sleepTimer = 0;
    }
}
