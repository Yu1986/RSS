package rssdownloader.utility;

import java.util.Date;
import java.util.HashMap;

import rssdownloader.DownloaderTimerTask;
import rssdownloader.db.Db;
import rssdownloader.rsssource.RssSource;
import rssdownloader.rsssource.RssSourceList;

public class Statistic {
	public HashMap<String, Long> main; 
	public HashMap<String, Long> sourceLastSta;
	public HashMap<String, Long> sourceSta;
	
	private DownloaderTimerTask downloaderTask;
	private Db db;
	
	private long lastRunTime;
	private long lastRunNum;
	private long totalRunNum;
	
	private long hourlyRssNum;
	private long hourlDupRssNum;
	private Date hourlyLastTime;
	
	public Statistic(DownloaderTimerTask downloaderTask) {
		this.downloaderTask = downloaderTask;
		db = downloaderTask.db;
	}
	
	public void loadStatistic() {
		main = db.readStatistic("main");
		sourceLastSta = db.readStatistic("source");
		sourceSta = new HashMap<String, Long>();
		if (main.containsKey("totalRunTime")) {
			lastRunTime = main.get("totalRunTime");
		} else {
			lastRunTime = 0;
		}
		if (main.containsKey("totalRunNum")) {
			lastRunNum = main.get("totalRunNum");
		} else {
			lastRunNum = 0;
		}
		totalRunNum = 0;
	}
	
	public void updateStatisticMain(long rssNumThisTime, long dupRssNumThisTime) {
		long totalRunTime;
		long rssNum;
		long dupRssNum;

		totalRunNum += 1;
		
		if (totalRunNum <= 1) {
			hourlyRssNum = rssNumThisTime;
			hourlDupRssNum = dupRssNumThisTime;
			hourlyLastTime = new Date();
			hourlyLastTime.setMinutes(0);
			hourlyLastTime.setSeconds(0);
		} else {
			Date now = new Date();
			now.setMinutes(0);
			now.setSeconds(0);
			if (now.after(hourlyLastTime)) {
				HashMap<String, Long> hourlyStatistic = new HashMap<String, Long>();
				hourlyStatistic.put(hourlyLastTime.toLocaleString() + "_RssItem", hourlyRssNum);
				hourlyStatistic.put(hourlyLastTime.toLocaleString() + "_DupRssItem", hourlDupRssNum);
				db.updateStatistic("HourlyStatistic", hourlyStatistic);
				hourlyRssNum = rssNumThisTime;
				hourlDupRssNum = dupRssNumThisTime;
				hourlyLastTime = (Date) now.clone();
			} else {
				hourlyRssNum += rssNumThisTime;
				hourlDupRssNum += dupRssNumThisTime;
			}
		}
		
		if (downloaderTask.debug) {
			System.out.println("Run: " + totalRunNum);
		}
		
		if (main.containsKey("rssNum")) {
			rssNum = main.get("rssNum");
		} else {
			rssNum = 0;
		}
		
		if (main.containsKey("dupRssNum")) {
			dupRssNum = main.get("dupRssNum");
		} else {
			dupRssNum = 0;
		}

		Date now = new Date();
		long t1 = now.getTime();
		long t2 = downloaderTask.startTime.getTime();
		totalRunTime = (t1 - t2) / (1000 * 60);
		totalRunTime += lastRunTime;
		
		main.put("totalRunTime", totalRunTime);
		main.put("totalRunNum", lastRunNum + totalRunNum);
		main.put("rssNum", rssNum + rssNumThisTime);
		main.put("dupRssNum", dupRssNum + dupRssNumThisTime);
		
		db.updateStatistic("main", main);
		
		RssSourceList rssSource = downloaderTask.rssSource;
		RssSource rssSourceField;
		rssSource.setFirstRssSource();
		sourceSta.clear();
        while ((rssSourceField = rssSource.getNextRssSource()) != null) {
        	String name = rssSourceField.get_name();
        	if (sourceLastSta.containsKey(name)) {
        		sourceSta.put(name, sourceLastSta.get(name) + rssSourceField.getRssItemNum());
        	} else {
        		sourceSta.put(name, rssSourceField.getRssItemNum());
        	}
        }
        
        db.updateStatistic("source", sourceSta);
	}
}
