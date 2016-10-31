/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import rssdownloader.utility.CmdServer;


/**
 *
 * @author Jie Yu
 */
public class RssDownloader {
    public  static DownloaderTimerTask downloaderTask;
    private static Timer timer;
    private static CmdServer cmdServer;
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
    
        // TODO code application logic here
    	if ((args.length == 1) &&
    			args[0].equals("debug")) {
    		downloaderTask = new DownloaderTimerTask(true);
       	} else {
       		downloaderTask = new DownloaderTimerTask(false);
       	}

        downloaderTask.start();
        
        cmdServer = new CmdServer(downloaderTask);
        try {
            cmdServer.run();
        } catch(IOException ex) {
        }
    }
}
