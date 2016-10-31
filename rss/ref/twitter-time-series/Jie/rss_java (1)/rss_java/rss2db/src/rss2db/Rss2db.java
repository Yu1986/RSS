/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rss2db;

import java.io.IOException;
import java.net.URL;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSParser;


/**
 *
 * @author 
 */
public class Rss2db {
    
    public static final String remoteRSS="http://api.bing.com/rss.aspx?Source=News&Market=en-US&Version=2.0&Query=top+stories";

    public static Db db;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        System.out.println("Hello RSS\n");
        
        int ret;
        
        try {
            
            db = new Db();
            db.connectDb();
            
            RSSHandler remoteRSSHandler = new RSSHandler();
            RSSParser.parseXmlFile(new URL(remoteRSS), remoteRSSHandler, false);
            RSSInfoRetriever.getRssInfo(remoteRSSHandler, db);
            
            db.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    
}
