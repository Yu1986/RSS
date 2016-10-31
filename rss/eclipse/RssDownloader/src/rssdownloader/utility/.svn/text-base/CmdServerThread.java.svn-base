/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import rssdownloader.DownloaderTimerTask;

/**
 *
 * @author Jie Yu
 */
public class CmdServerThread extends Thread {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private DownloaderTimerTask downloaderTask;
    
    private LinkedList cmdList;
    
    public abstract class Command {
        public String name;
        public String help;
        
        public Command(String name, String help) {
            this.name = name;
            this.help = help;
        }
        
        abstract void cmdExe(int argc, String argv[]);    
    }
    
    /*
    public class Cmds {
        public String name;
        public String help;
        public CmdExe cmdRun;

        public Cmds(String name, String help, CmdExe cmdRun) {
            this.name = name;
            this.help = help;
            this.cmdRun = cmdRun;
        }
    }
    */
    
    private void CmdServerCmds() { 
        cmdList.addLast(
            new CmdHelp("help", 
            "help\n"      
                + "     Print this message\n\n"));
        cmdList.addLast(
            new CmdQuit("quit",
            "quit\n"      
                + "     quit the RSS Downloader Service\n\n"));
        cmdList.addLast(
            new CmdStart("start",
            "start\n"      
                + "     start the RSS Downloader Service\n\n"));
        cmdList.addLast(
            new CmdStop("stop",
            "stop\n"      
                + "     stop the RSS Downloader Service\n\n"));
        cmdList.addLast(
            new CmdRun("run",
            "run\n"      
                + "     run the downloader thread immediately\n\n"));
        cmdList.addLast(
            new CmdListSource("listsource",
            "listsource\n"
                + "     List RSS Sources\n\n"));
        cmdList.addLast(
            new CmdAddSource("addsource", 
            "addsource" + "\t [name] [url] [category] [prefix]\n"
                + "     Add RSS Source\n\n"));
        cmdList.addLast(
            new CmdRmSource("rmsource",
            "rmsource"  + "\t [name]\n"
                + "     Remove RSS Source\n\n"));
        cmdList.addLast(
            new CmdHost("host",
            "host"      + "\t [hostname] [port] [keyspace]\n"
                + "     Set Database host infor\n\n"));
        cmdList.addLast(         
            new CmdSet("set",
            "set"       + "\t [property] [value]\n"
                + "     Set the properties of the server\n"
                        + "\t [property]:\n"
                        + "\t port:             the port number for cmdine interface, effect when server restarted\n"
                        + "\t rssrate:          the update rate for RSS source, the unit is minute\n"
                        + "\t hamdistance:      the minimum of the Hamming distance between similar RSS Items\n"
                        + "\t autoflushrate:    the rate for flushing RSS Items from RAM to Database, the unit is hour\n\n"));
        cmdList.addLast(
            new CmdStatus("status",
            "status\n"    
                + "     Show the current status of the RSS Item Hash Table\n\n"));
        cmdList.addLast(
            new CmdStatusDb("statusdb",
            "statusdb\n"  
                + "     Show the current status of the Database\n\n"));
        cmdList.addLast(
            new CmdDuplicate("duplicate",
            "duplicate" + "\t [operation] [parameter]\n"
                + "     Show the duplicated RSS Items\n"
                        + "\t [operation]:\n"
                        + "\t list: list the duplicated RSS Items in the Ram\n"
                        + "\t      param: none\n"
                        + "\t show: [the simhash value for the primary RSS ]: show the details of the duplicated RSS\n"
                        + "\t      param: the primary SimHash value\n\n"));
    }
    
    public CmdServerThread(Socket s, DownloaderTimerTask downloaderTask) throws IOException {
        cmdList = new LinkedList();
        CmdServerCmds();
        client = s;
        this.downloaderTask = downloaderTask;
        in = new BufferedReader(new InputStreamReader(client.getInputStream(), "GB2312"));
        out = new PrintWriter(client.getOutputStream(),true);
    }
    
    @Override
    public void run() {
        try {
            String line = "";
            out.println("    ==== Rss Downloader ====");
            out.println("    Press 'help' for help! ");
            while (!line.equals("exit")) {
                out.printf(">");
                line = in.readLine();
                cmdline(line);
            }
            out.println("byebye");
            client.close();
        } catch (IOException e) {
        }
    }
    
    private void cmdline(String cmdStr) {
        String cmdsPre[] = cmdStr.split(" |\t");
        String argv[] = new String[10];
        int argc = 0;
        for (String s: cmdsPre) {
            if (!s.equals("")) {
                argv[argc++] = s;
            }
        }
        
        if (argc == 0) {
            return;
        }
        
        argv[0] = argv[0].toLowerCase();
        
        boolean unknownCmd = true;
        Iterator itr = cmdList.iterator();
        while (itr.hasNext()) {
            Command cmd = (Command)itr.next();
            if (argv[0].equals(cmd.name)) {
                unknownCmd = false;
                cmd.cmdExe(argc, argv);
            }
        }
        
        if (unknownCmd && (!argv[0].equals("exit"))) {
            out.println("Unknow Command!");
        }
    }
    
    class CmdHelp extends Command {
        public CmdHelp(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            String helpMsg = "Help Message:\n";
        
            Iterator itr = cmdList.iterator();
            while (itr.hasNext()) {
                helpMsg += ((Command)itr.next()).help;
            }
            out.println(helpMsg);
        }
    }
    
    class CmdQuit extends Command {
        public CmdQuit(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
        	if (downloaderTask.sleep_flag) {
        		System.exit(0);
        	} else {
        		downloaderTask.quit_flag = true;
        	}
        }
    }
    
    class CmdStart extends Command {
        public CmdStart(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            downloaderTask.runFlag = true;
        }
    }
    
    class CmdStop extends Command {
        public CmdStop(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            downloaderTask.runFlag = false;
        }
    }
    
    class CmdRun extends Command {
        public CmdRun(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            downloaderTask.cleanSleepTimer();
        }
    }
           
    class CmdListSource extends Command {
        public CmdListSource(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            out.println(downloaderTask.getRssSourceList());
        }
    }
    
    class CmdAddSource extends Command {
        public CmdAddSource(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            if (argc < 4) {
                out.println("Wrong Parameters Number!");
                out.println(help);
                return;
            } else if (argc == 4) {
                argv[4] = "";
            }
            boolean result = downloaderTask.addRssSource(argv[1], argv[2], argv[3], argv[4]);
            if (!result) {
                out.println("RSS Add Failed! Duplicated RSS Source Name Found");
            }
        }
    }
    
    class CmdRmSource extends Command {
        public CmdRmSource(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            if (argc != 2) {
                out.println("Wrong Parameters Number!");
                out.println(help);
                return;
            }
            boolean result = downloaderTask.rmRssSource(argv[1]);
            if (!result) {
                System.out.println("RSS Add Failed! Can not find specialfied RSS Source");
            }
        }
    }
    
    class CmdHost extends Command {
        public CmdHost(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            if (argc != 4) {
                out.println("Wrong Parameters Number!");
                out.println(help);
                return;
            }
            String result = downloaderTask.setHost(argv[1], argv[2], argv[3]);
            out.println(result);
        }
    }
    
    class CmdSet extends Command {
        public CmdSet(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            if (argc != 3) {
                out.println("Wrong Parameters Number!");
                out.println(help);
                return;
            }
            if (argv[1].equals("port")) {
                downloaderTask.setCmdPort(argv[2]);
            } else if (argv[1].equals("rssrate")) {
                downloaderTask.setRssUpdateRate(argv[2]);
            } else if (argv[1].equals("hamdistance")) {
                downloaderTask.setSimilarDistance(argv[2]);
            } else if (argv[1].equals("autoflushrate")) {
                downloaderTask.setHashTableCleanDelay(argv[2]);
            } else {
                out.println("Unsupported property setting, press 'help' for help");
            }
        }
    }
    
    class CmdStatus extends Command {
        public CmdStatus(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            if (argc != 1) {
                out.println("Wrong Parameters Number!");
                out.println(help);
                return;
            }
            out.println(downloaderTask.showStatus());
        }
    }
    
    class CmdStatusDb extends Command {
        public CmdStatusDb(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            if (argc != 1) {
                out.println("Wrong Parameters Number!");
                out.println(help);
                return;
            }
            out.println(downloaderTask.showStatusDb());
        }
    }
    
    class CmdDuplicate extends Command {
        public CmdDuplicate(String name, String help) {
            super(name, help);
        }
        
        @Override
        public void cmdExe(int argc, String argv[]) {
            if ((argc > 3) || (argc < 2)) {
                out.println("Wrong Parameters Number!");
                out.println(help);
                return;
            }
            if (argv[1].equals("list")) {
                out.println(downloaderTask.listDuplicate());
            } else if (argv[1].equals("show")) {
                out.println(downloaderTask.showDuplicate(Long.parseLong(argv[2])));
            } else {
                out.println("Unsupported operation, press 'help' for help");
            }
        }
    }
}
