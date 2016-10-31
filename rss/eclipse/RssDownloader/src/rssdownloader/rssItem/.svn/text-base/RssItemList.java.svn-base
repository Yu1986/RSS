/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.rssItem;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import rssdownloader.db.Db;
import rssdownloader.simhash.SimHash;

/**
 *
 * @author Jie Yu
 */
public class RssItemList {
	
	public enum ItemAddReturn {
		Error,
		DupItem,
		Same,
		Ok
	};
    
    public static long SIMILAR_DISTANCE;
    public static long SAME_DISTANCE;
    public static long CLEAN_DEALY; // 3 days
    private HashMap<Long, RssItem> rssHashTable;
    private HashMap<Long, LinkedList<RssItem>> rssDuplicated;
    private HashMap<Long, RssItem> rssTotal;
    private Db db;
    

    public RssItemList(long similarDistance, long sameDistance, long hashTblCleanDelay, Db db) {
        rssHashTable = new HashMap<Long, RssItem>();
        rssDuplicated = new HashMap<Long, LinkedList<RssItem>>();
        rssTotal = new HashMap<Long, RssItem>();
        SIMILAR_DISTANCE = similarDistance;
        SAME_DISTANCE = sameDistance;
        CLEAN_DEALY = 60 * 60 * 1000 * hashTblCleanDelay;
        this.db = db;
    }
    
    public void setDistance(long similarDistance) {
        SIMILAR_DISTANCE = similarDistance;
    }
    
    public void setCleanDelay(long hashTblCleanDelay) {
        CLEAN_DEALY = 60 * 60 *1000 * hashTblCleanDelay;
    }
    
    public ItemAddReturn addRssItem(RssItem rssItem) {
        if (!rssTotal.containsKey(rssItem.getSimHash())) {
        	rssTotal.put(rssItem.getSimHash(), null);
            Iterator<Entry<Long, RssItem>> itr = rssHashTable.entrySet().iterator();
            while (itr.hasNext()) {
                Entry<Long, RssItem> entry =  itr.next();
                long simHash = entry.getKey();
                long hammingDistance = SimHash.hammingDistance(simHash, rssItem.getSimHash());
                if (hammingDistance < SAME_DISTANCE) {
                	return ItemAddReturn.Same;
                } else if (hammingDistance < SIMILAR_DISTANCE) {
                    processDuplicatedRss(simHash, rssItem);
                    return ItemAddReturn.DupItem;
                }
            }
            rssHashTable.put(rssItem.getSimHash(), rssItem);
            db.addRssItem(rssItem);
            return ItemAddReturn.Ok;
        } else {
            return ItemAddReturn.Error;
        }
    }
    
    /**
     * The recent RSS items are stored into RAM(rssHashTable), only when its publish
     * date is beyond CLEAN_DEALY days, we move it out from rssHashTable.
     */
    public void cleanHashTable() {
        Date now = new Date();
        long nowlong = now.getTime();
        Iterator<Entry<Long, RssItem>> itr = rssHashTable.entrySet().iterator();
        
        while (itr.hasNext()) {
        	Entry<Long, RssItem> entry = itr.next();
        	RssItem value= entry.getValue();
        	Long key =entry.getKey();
            Date pubDate = value.getPubDate();
            Date downloadDate = value.getDownloadDate();
            if (((pubDate.getTime() + CLEAN_DEALY) < nowlong) && 
                    ((downloadDate.getTime() + CLEAN_DEALY) < nowlong)) {
                // TODO save the removed RSS items into database
                /*
                db.addRssItem((RssItem)value);
                LinkedList duplicated = (LinkedList)rssDuplicated.get(key);
                if (duplicated != null) {
                    db.addDupRssItem(((RssItem)value).getSimHash(), duplicated);
                }
                * 
                */
                // it is a old RSS Item, need to remove it from rssHashTable
                rssHashTable.remove(key);
                rssDuplicated.remove(key);
                rssTotal.remove(key);
            }
        }
    }
    
    private void updateDistance(RssItem primaryRss, LinkedList<RssItem> duplicated) {
    	Iterator<RssItem> itr = duplicated.iterator();
    	
    	while (itr.hasNext()) {
    		RssItem rss = itr.next();
    		long hammingDistance = SimHash.hammingDistance(primaryRss.getSimHash(), rss.getSimHash());
    		rss.setDistanceSum(hammingDistance);
    	}
    }
    
    /**
     * When we found 2 duplicated RssItems, we just simply put the new one into 
     * duplicated list, and let the older one be the best choice.
     * When we found 3 duplicated RssItems, we calculate the sum of HammingDistances 
     * of these three points, and let the one with the shortest HammingDistance be 
     * our first choice.
     * When we found more duplicated RssItems, we just need to calculate the sum of 
     * HammingDistance between the duplicated RssItems and the new RssItems, if the 
     * sum distance is shorted than the old first Rss choice, we will use the new
     * RssItems as our first choice, and put the old one into duplicated Rss List
     */
    private void processDuplicatedRss(long simHashFa, RssItem rssNew) {
        if (rssDuplicated.containsKey(simHashFa)) {
        	LinkedList<RssItem> duplicated = rssDuplicated.get(simHashFa);
            if (duplicated.size() == 1) {
                // we found 3 dupicated Rss Items
                RssItem rssDup = (RssItem)duplicated.getFirst();
                long simHashDup = rssDup.getSimHash();
                long simHashNew = rssNew.getSimHash();
                long distanceFa = SimHash.hammingDistance(simHashFa, simHashDup) + 
                        SimHash.hammingDistance(simHashFa, simHashNew);
                long distanceDup = SimHash.hammingDistance(simHashDup, simHashFa) + 
                        SimHash.hammingDistance(simHashDup, simHashNew);
                long distanceNew = SimHash.hammingDistance(simHashNew, simHashDup) + 
                        SimHash.hammingDistance(simHashNew, simHashFa);
                if ((distanceFa <= distanceDup) && (distanceFa <= distanceNew)) {
                    // distanceFa has the shortest value
                    RssItem rssFa = (RssItem)rssHashTable.get(simHashFa);
                    rssFa.setDistanceSum(distanceFa);
                    duplicated.addLast(rssNew);
                    updateDistance(rssFa, duplicated);
                    db.addDupRssItem(rssFa, duplicated);
                } else if ((distanceDup <= distanceFa) && (distanceDup <= distanceNew)) {
                    // distanceDup has the shortest value
                    RssItem rssFa = (RssItem)rssHashTable.get(simHashFa);
                    db.rmRssItem((RssItem)rssHashTable.get(simHashFa));
                    rssHashTable.remove(simHashFa);
                    rssDup.setDistanceSum(distanceDup);
                    rssHashTable.put(rssDup.getSimHash(), rssDup);
                    db.addRssItem(rssDup);
                    duplicated.removeFirst();
                    duplicated.addLast(rssFa);
                    duplicated.addLast(rssNew);
                    rssDuplicated.remove(simHashFa);
                    db.rmDupRssItem(simHashFa);
                    rssDuplicated.put(rssDup.getSimHash(), duplicated);
                    updateDistance(rssDup, duplicated);
                    db.addDupRssItem(rssDup, duplicated);
                } else {
                    // distanceNew has the shortest value
                    RssItem rssFa = (RssItem)rssHashTable.get(simHashFa);
                    db.rmRssItem((RssItem)rssHashTable.get(simHashFa));
                    rssHashTable.remove(simHashFa);
                    rssNew.setDistanceSum(distanceNew);
                    rssHashTable.put(rssNew.getSimHash(), rssNew);
                    db.addRssItem(rssNew);
                    duplicated.addLast(rssFa);
                    rssDuplicated.remove(simHashFa);
                    db.rmDupRssItem(simHashFa);
                    rssDuplicated.put(rssNew.getSimHash(), duplicated);
                    updateDistance(rssNew, duplicated);
                    db.addDupRssItem(rssNew, duplicated);
                }
            } else {
                Iterator<RssItem> itr = duplicated.iterator();
                long distanceSum = 0;
                long newRssSimHashValue = rssNew.getSimHash();
                while (itr.hasNext()) {
                    RssItem rssDup = itr.next();
                    distanceSum += SimHash.hammingDistance(newRssSimHashValue, rssDup.getSimHash());
                }
                RssItem rssFa = (RssItem)rssHashTable.get(simHashFa);
                if (rssFa.getDistanceSum() <= distanceSum) {
                    rssFa.addDistanceSum(SimHash.hammingDistance(simHashFa, newRssSimHashValue));
                    duplicated.addLast(rssNew);
                    updateDistance(rssFa, duplicated);
                    db.addDupRssItem(rssFa, duplicated);
                } else {
                    db.rmRssItem((RssItem)rssHashTable.get(simHashFa));
                    rssHashTable.remove(simHashFa);
                    rssNew.setDistanceSum(distanceSum + 
                            SimHash.hammingDistance(simHashFa, newRssSimHashValue));
                    rssHashTable.put(newRssSimHashValue, rssNew);
                    db.addRssItem(rssNew);
                    duplicated.addLast(rssFa);
                    rssDuplicated.remove(simHashFa);
                    db.rmDupRssItem(simHashFa);
                    rssDuplicated.put(newRssSimHashValue, duplicated);
                    updateDistance(rssNew, duplicated);
                    db.addDupRssItem(rssNew, duplicated);
                }
            }
            
        } else {
            LinkedList<RssItem> duplicated = new LinkedList<RssItem>();
            duplicated.addLast(rssNew);
            rssDuplicated.put(simHashFa, duplicated);
            updateDistance(rssHashTable.get(simHashFa), duplicated);
            db.addDupRssItem(rssHashTable.get(simHashFa), duplicated);
        }
    }
    
    public String showStatus() {
        String statusStr = "";
        
        statusStr += "the size of primary Hash Table is: " + rssHashTable.size() + "\n";
        statusStr += "the size of duplicated Hash Table is: " + rssDuplicated.size() + "\n";
        
        return statusStr;
    }
    
    public String listDuplicate() {
        String statusStr = "";
        
        Iterator<Entry<Long, LinkedList<RssItem>>> itr = rssDuplicated.entrySet().iterator();
        
        while (itr.hasNext()) {
        	Entry<Long, LinkedList<RssItem>> entry = itr.next();
            LinkedList<RssItem> duplist = entry.getValue();
            long dupPrimarySimHash = (Long)entry.getKey();
            RssItem primaryRssItem = (RssItem)rssHashTable.get(dupPrimarySimHash);
            statusStr += "-------------------------\n";
            statusStr += "Duplicated RSS: " + dupPrimarySimHash + "\n";
            statusStr += "The title of Primary Rss Item: " + primaryRssItem.getTitle() + "\n";
            statusStr += "There are also " + duplist.size() + " similar RSS Items in this group\n";
        }
        
        return statusStr;
    }
    
    public String showDuplicate(long primarySimhash) {
        String statusStr = "";
        
        RssItem primaryRssItem = (RssItem)rssHashTable.get(primarySimhash);
        statusStr += "The Primary Rss Item: " + primarySimhash + "\n";
        statusStr += "Title: " + primaryRssItem.getTitle() + "\n";
        statusStr += "Brief: " + primaryRssItem.getDescription() + "\n";
        statusStr += "Source: " + primaryRssItem.getRssSource() + "\n";
        statusStr += "Publish Date: " + primaryRssItem.getPubDate() + "\n";
        
        LinkedList<RssItem> duplist = rssDuplicated.get(primarySimhash);
        statusStr += "There are also " + duplist.size() + " similar RSS Items in this group\n";
        
        Iterator<RssItem> itr = duplist.iterator();
        while (itr.hasNext()) {
            RssItem rssDup = (RssItem)itr.next();
            statusStr += "-------------------------\n";
            statusStr += "Duplicated RSS: " + rssDup.getSimHash() + "\n";
            statusStr += "Title: " + rssDup.getTitle() + "\n";
            statusStr += "Brief: " + rssDup.getDescription() + "\n";
            statusStr += "Source: " + rssDup.getRssSource() + "\n";
            statusStr += "Publish Date: " + rssDup.getPubDate() + "\n";
        }
        
        return statusStr;
    }
}
