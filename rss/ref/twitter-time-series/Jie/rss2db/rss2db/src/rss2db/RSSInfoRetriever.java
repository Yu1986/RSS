/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rss2db;

import java.util.List;
import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSImage;
import org.gnu.stealthp.rsslib.RSSItem;

/**
 *
 * @author eu3c
 */
public class RSSInfoRetriever {
    public static String getRssInfo(RSSHandler handler, Db db) {
        String rssInfo = new String();
        
        rssInfo += "Hello";
        
        // Retriever channel 
        RSSChannel channel = handler.getRSSChannel(); 
        
        String titleInfo = channel.getTitle(); 
        String linkInfo = channel.getLink(); 
        String descriptionInfo = channel.getDescription(); 
        String languageInfo = channel.getLanguage(); 
        String copyrightInfo = channel.getCopyright(); 
        String generatorInfo = channel.getGenerator(); 
        RSSImage channelImage = channel.getRSSImage(); 
        String channelImageUrl = channelImage.getUrl(); 
         
         
        rssInfo += "Channel Title: " + titleInfo+"\n"; 
        rssInfo += "Channel Info: "+linkInfo+"\n"; 
        rssInfo += "Channel Descrition: "+descriptionInfo+"\n"; 
        rssInfo += "Channel Language： "+languageInfo+"\n"; 
        rssInfo += "Channel CopyRight： "+copyrightInfo+"\n"; 
        rssInfo += "Channel Generator Info： "+generatorInfo+"\n"; 
        rssInfo += "Channel Image URL: "+channelImageUrl+"\n"; 
         
        List channelItems = channel.getItems(); 
        int itemSize= channelItems.size(); 
        if(itemSize >=1){ 
            rssInfo += "\n"; 
            rssInfo += "There are "+itemSize+" items in this channel"; 
            rssInfo += "\n"; 
            for (int i=0;i<itemSize;i++){ 
                int itemNo = i+1; 
                RSSItem item = (RSSItem)channelItems.get(i); 
                 
                rssInfo += "\n"; 
                rssInfo += "Abstruct"+itemNo+":"; 
                 
                String itemAuthor = item.getAuthor(); 
                String itemTitle = item.getTitle(); 
                String itemDescription = item.getDescription(); 
                String itemLink = item.getLink(); 
                String itemPubDate = item.getPubDate(); 
                 
                rssInfo += "Author： "+itemAuthor+"\n"; 
                rssInfo += "Title： "+itemTitle+"\n"; 
                rssInfo += "Description： "+itemDescription+"\n"; 
                rssInfo += "Link: "+itemLink+"\n"; 
                rssInfo += "Date: "+itemPubDate+"\n";
                db.addToDB(itemLink, "bing.com", itemTitle, itemDescription);
                 
                rssInfo += "\n"; 
            } 
        } 
        
        System.out.println(rssInfo);
        return rssInfo;
    }
}
