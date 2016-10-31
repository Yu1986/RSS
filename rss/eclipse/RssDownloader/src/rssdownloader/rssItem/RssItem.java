/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.rssItem;

import java.util.Date;

/**
 *
 * @author Jie Yu
 */
public class RssItem {
    private String title; // the title for this RSS Item
    private String description; // the brief content for this RSS Item
    private String author; // the anthor for this RSS Item
    private String url; // the URL for details of this RSS Item
    private Date pubDate; // the Publish date for this RSS Item
    private Date downloadDate; // the downloaded Date for this RSS Item
    private long simHash; // The simHash Value for this RSS Item. It is calculated based on RSS Item title and description
    private String rssSource; // the name of its RSS Source, it can be seen as foreign key to RSS Source
    private long distanceSum; // If this is a primary RSS Item which has many duplicated RSS Items, this is sum of the total hamming distance between each duplicated RSS Item. otherwise it is no meaning

    public RssItem(String title, String description, String author, 
            String url, Date pubDate, Date downloadDate, long simHash, String rssSource) {
        if (title != null) {
            this.title = title;
        } else {
            this.title = "";
        }
        
        if (description != null) {
            this.description = description;
        } else {
            this.description = "";
        }
        
        if (author != null) {
            this.author = author;
        } else {
            this.author = "";
        }
        
        if (url != null) {
            this.url = url;
        } else {
            this.url = "";
        }
        
        this.pubDate = pubDate;
        this.downloadDate = downloadDate;
        this.simHash = simHash;
        
        if (rssSource != null) {
            this.rssSource = rssSource;
        } else {
            this.rssSource = "";
        }
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getRssSource() {
        return rssSource;
    }
    
    public long getSimHash() {
        return simHash;
    }
    
    public Date getPubDate() {
        return pubDate;
    }
    
    public Date getDownloadDate() {
        return downloadDate;
    }
    
    public void setDistanceSum(long dis) {
        distanceSum = dis;
    }
    
    public void addDistanceSum(long dis) {
        distanceSum += dis;
    }
    
    public long getDistanceSum() {
        return distanceSum;
    }
    
}
