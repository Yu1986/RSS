/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.db;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import rssdownloader.rssItem.RssItem;
import rssdownloader.rsssource.RssSource;

/**
 *
 * @author Jie Yu
 */
public class Db {
	
	private static final int maxColumnCount = 100000;
	private static final int maxRowCount = 100000;
	
    private Cluster myCluster;
    private Keyspace ksp;
    private StringSerializer stringSerializer;
    private DateSerializer dateSerializer;
    private LongSerializer longSerializer;
    private ColumnFamilyTemplate<String, String> templateRssSource;
    private boolean connectStatus;
    
    public Db(String host, String port, String keyspace) {
        
        String hostname = host + ":" + port;
        
        myCluster = HFactory.getOrCreateCluster("test-cluster", hostname);
        connectStatus = !myCluster.getConnectionManager().getHosts().toString().equals("[]");
        
        if (connectStatus) {
            ksp = HFactory.createKeyspace(keyspace, myCluster);


            templateRssSource = new ThriftColumnFamilyTemplate<String, String>(ksp,
                                                                             "RssSource",
                                                                             StringSerializer.get(),
                                                                             StringSerializer.get());
            stringSerializer = new StringSerializer();
            dateSerializer = new DateSerializer();
            longSerializer = new LongSerializer();
        }
    }
    
    public boolean connect(String host, String port, String keyspace) {
        
        String hostname = host + ":" + port;
        
        HFactory.shutdownCluster(myCluster);
        
        myCluster = HFactory.getOrCreateCluster("test-cluster", hostname);
        connectStatus = !myCluster.getConnectionManager().getHosts().toString().equals("[]");
        
        if (connectStatus) {
            ksp = HFactory.createKeyspace(keyspace, myCluster);


            templateRssSource = new ThriftColumnFamilyTemplate<String, String>(ksp,
                                                                             "RssSource",
                                                                             StringSerializer.get(),
                                                                             StringSerializer.get());
            stringSerializer=new StringSerializer();
        }
        
        return connectStatus;
    }
    
    public void addRssSource(RssSource rssSource) {
        if (!connectStatus) {
            return;
        }
        
        ColumnFamilyUpdater<String, String> updater = templateRssSource.createUpdater(rssSource.get_name());
        updater.setString("url", rssSource.get_url());
        updater.setString("category", rssSource.get_category());
        updater.setString("titlePrefix", rssSource.get_prefix());
        try {
            templateRssSource.update(updater);
        } catch (HectorException e) {
            // do something ...
        }
    }
    
    private String getFormatedDate(Date itemDate) {
        itemDate.setHours(8);
        itemDate.setMinutes(0);
        itemDate.setSeconds(0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        String timeStr = df.format(itemDate); 
        return timeStr;
    }
    
    public void addRssItem(RssItem rssItem) {
        if (!connectStatus) {
            return;
        }
        
        String timeStr = getFormatedDate(new Date(rssItem.getPubDate().getTime())); 
        
        Mutator<String> mutator = HFactory.createMutator(ksp, stringSerializer);

        List<HColumn<String, String>> asList = Arrays.asList();
        List<HColumn<String, String>> arrayList = new ArrayList(asList);
        
        List<HColumn<String, Date>> asList2 = Arrays.asList();
        List<HColumn<String, Date>> arrayList2 = new ArrayList(asList2);
        
        String simHashStr = "" + rssItem.getSimHash();
        arrayList.add(HFactory.createStringColumn("title", rssItem.getTitle()));
        arrayList.add(HFactory.createStringColumn("author", rssItem.getAuthor()));
        arrayList.add(HFactory.createStringColumn("brief", rssItem.getDescription()));
        arrayList.add(HFactory.createStringColumn("content", ""));
        arrayList.add(HFactory.createStringColumn("source", rssItem.getRssSource()));
        arrayList.add(HFactory.createStringColumn("url", rssItem.getUrl()));
        HColumn<String, Date> dateColumn = HFactory.createColumn("pubdate", rssItem.getPubDate());
        arrayList2.add(dateColumn);
       
        mutator.insert(timeStr, "RssItem", HFactory.createSuperColumn(simHashStr,
            arrayList,
            stringSerializer, stringSerializer, stringSerializer));
        mutator.insert(timeStr, "RssItem", HFactory.createSuperColumn(simHashStr,
                arrayList2,
                stringSerializer, stringSerializer, dateSerializer));
        
        /*
        String simHashStr = "" + rssItem.getSimHash();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        String timeStr = df.format(rssItem.getPubDate()); 
        String distance = "" + rssItem.getDistanceSum();
        
        Mutator<String> mutator = HFactory.createMutator(ksp, stringSerializer);
        mutator.addInsertion(simHashStr, "RssItem",
        HFactory.createStringColumn("title", rssItem.getTitle()))
            .addInsertion(simHashStr, "RssItem", HFactory.createStringColumn("author", rssItem.getAuthor()))
            .addInsertion(simHashStr, "RssItem", HFactory.createStringColumn("description", rssItem.getDescription()))
            .addInsertion(simHashStr, "RssItem", HFactory.createStringColumn("source", rssItem.getRssSource()))
            .addInsertion(simHashStr, "RssItem", HFactory.createStringColumn("url", rssItem.getUrl()))
            .addInsertion(simHashStr, "RssItem", HFactory.createStringColumn("pubdate", timeStr))
            .addInsertion(simHashStr, "RssItem", HFactory.createStringColumn("distance", distance));
        mutator.execute();
        * 
        */
    }
    
    private void dupSaveColumn(long primarySimHash, RssItem rss, Mutator<String> mutator) {
    	
    	List<HColumn<String, String>> asList = Arrays.asList();
        List<HColumn<String, String>> arrayList = new ArrayList(asList);
        
        List<HColumn<String, Date>> asList2 = Arrays.asList();
        List<HColumn<String, Date>> arrayList2 = new ArrayList(asList2);
    	String simHashFaStr = "" + primarySimHash;
    	String simHashStr = "" + rss.getSimHash();
        arrayList.add(HFactory.createStringColumn("title", rss.getTitle()));
        arrayList.add(HFactory.createStringColumn("author", rss.getAuthor()));
        arrayList.add(HFactory.createStringColumn("brief", rss.getDescription()));
        arrayList.add(HFactory.createStringColumn("content", ""));
        arrayList.add(HFactory.createStringColumn("source", rss.getRssSource()));
        arrayList.add(HFactory.createStringColumn("url", rss.getUrl()));
        String disStr = "" + rss.getDistanceSum();
        arrayList.add(HFactory.createStringColumn("distance", disStr));
        HColumn<String, Date> dateColumn = HFactory.createColumn("pubdate", rss.getPubDate());
        arrayList2.add(dateColumn);
        //arrayList.add(HFactory.createStringColumn("pubdate", timeStr));
        mutator.insert(simHashFaStr, "RssDuplicatedItem", HFactory.createSuperColumn(simHashStr,
            arrayList,
            stringSerializer, stringSerializer, stringSerializer));
        mutator.insert(simHashFaStr, "RssDuplicatedItem", HFactory.createSuperColumn(simHashStr,
            arrayList2,
            stringSerializer, stringSerializer, dateSerializer));
    }
    
    public void addDupRssItem(RssItem primaryRss, LinkedList<RssItem> dupRssList) {
        if (!connectStatus) {
            return;
        }
       
        Mutator<String> mutator = HFactory.createMutator(ksp, stringSerializer);
        
        dupSaveColumn(primaryRss.getSimHash(), primaryRss, mutator);
        
        Iterator<RssItem> itr = dupRssList.iterator();
        while (itr.hasNext()) {
            dupSaveColumn(primaryRss.getSimHash(), itr.next(), mutator);
        }
    }
    
    public void rmRssSource(String name) {
        templateRssSource.deleteRow(name);
    }
    
    public void rmRssItem(RssItem rssItem) {
        String simHashStr = "" + rssItem.getSimHash();
        
        String timeStr = getFormatedDate(new Date(rssItem.getPubDate().getTime())); 
        
        Mutator<String> mutator = HFactory.createMutator(ksp, stringSerializer);
        mutator.addDeletion(timeStr, "RssItem", simHashStr, stringSerializer);
        mutator.execute();
    }
    
    public void rmDupRssItem(long primarySimHash) {
        String simHashStr = "" + primarySimHash;
        
        Mutator<String> mutator = HFactory.createMutator(ksp, stringSerializer);
        mutator.addDeletion(simHashStr, "RssDuplicatedItem");
        mutator.execute();
    }
    
    public LinkedList readRssSource() { 
        LinkedList rssSourceList = new LinkedList();
        if (!connectStatus) {
            return rssSourceList;
        }
        
        RangeSlicesQuery<String, String, String> rangeSlicesQuery =
        HFactory.createRangeSlicesQuery(ksp, stringSerializer, stringSerializer, stringSerializer);
        rangeSlicesQuery.setColumnFamily("RssSource");
        //rangeSlicesQuery.setKeys("", "");
        rangeSlicesQuery.setRange(null, null, false, maxColumnCount);
        rangeSlicesQuery.setRowCount(maxRowCount);
        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        
        List<Row<String, String, String>> resultList = result.get().getList();
        Iterator<Row<String, String, String>> itrRow = resultList.iterator();
        
        while (itrRow.hasNext()) {
            Row<String, String, String> row = itrRow.next();
            String key = row.getKey();
            ColumnSlice cs = row.getColumnSlice();
            List<HColumn<String,String>> columnList = cs.getColumns();
            Iterator<HColumn<String,String>> itrColumn = columnList.iterator();
            String category = "";
            String titlePrefix = "";
            String url = "";
            while (itrColumn.hasNext()) {
                HColumn<String,String> column = itrColumn.next();
                String name = column.getName();
                String value = column.getValue();
                if (name.equals("category")) {
                    category = value;
                } else if (name.equals("titlePrefix")) {
                    titlePrefix = value;
                } else if (name.equals("url")) {
                    url = value;
                }
            }
            RssSource rssSource = new RssSource(key, url, category, titlePrefix);
            rssSourceList.addLast(rssSource);
        }

        return rssSourceList;
    }
    
    public HashMap<String, Long> readStatistic(String key) {
    	HashMap<String, Long> statistic = new HashMap<String, Long>();
    	if (!connectStatus) {
            return statistic;
        }
    	
    	RangeSlicesQuery<String, String, Long> rangeSlicesQuery =
    	        HFactory.createRangeSlicesQuery(ksp, stringSerializer, stringSerializer, longSerializer);
    	rangeSlicesQuery.setColumnFamily("Statistic");
    	rangeSlicesQuery.setKeys(key, key);
    	rangeSlicesQuery.setRange(null, null, false, maxColumnCount);
    	rangeSlicesQuery.setRowCount(maxRowCount);
    	QueryResult<OrderedRows<String, String, Long>> result = rangeSlicesQuery.execute();
    	
    	List<Row<String, String, Long>> resultList = result.get().getList();
    	Iterator<Row<String, String, Long>> itrRow = resultList.iterator();
    	
    	while (itrRow.hasNext()) {
    	    Row<String, String, Long> row = itrRow.next();
    	    ColumnSlice<String, Long> cs = row.getColumnSlice();
    	    List<HColumn<String,Long>> columnList = cs.getColumns();
    	    Iterator<HColumn<String,Long>> itrColumn = columnList.iterator();
    	    while (itrColumn.hasNext()) {
    	        HColumn<String,Long> column = itrColumn.next();
    	        statistic.put(column.getName(), column.getValue());
    	    }
    	}
    	
    	return statistic;
    }
    
    public void updateStatistic(String key, HashMap<String, Long> statistic) {
        
    	if (!connectStatus) {
            return;
        }
    	
        Mutator<String> mutator = HFactory.createMutator(ksp, stringSerializer);
        Iterator<Entry<String, Long>> itr = statistic.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, Long> entry = itr.next();
            mutator.addInsertion(key, "Statistic", HFactory.createColumn(entry.getKey(), entry.getValue(), 
            		stringSerializer, longSerializer));
        }
        mutator.execute();
    }
    
    public String showStatus() {
        String statusStr = "";
        
        return statusStr;
    }
}
