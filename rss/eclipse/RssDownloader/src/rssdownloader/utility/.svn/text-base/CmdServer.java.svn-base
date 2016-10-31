/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import rssdownloader.DownloaderTimerTask;

/**
 *
 * @author Jie Yu
 */
public class CmdServer extends ServerSocket {
    private static int SERVER_PORT;
    public DownloaderTimerTask downloaderTask;
    
    public CmdServer(DownloaderTimerTask downloaderTask) throws IOException {
        super(Integer.parseInt(downloaderTask.props.getProperty("CmdPort")));
        this.downloaderTask = downloaderTask;
    }
    
    public void run() throws IOException {
        try
        {
            while (true) {
                Socket socket = accept();
                CmdServerThread cmdServerThread = new CmdServerThread(socket, downloaderTask);
                cmdServerThread.start();
            }
        } catch (IOException e) {
        } finally {
            close();
        }
        
    }
}
