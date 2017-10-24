package trial;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.oddy.utility.Skyline;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ASUS
 */
public class Scraper {

    public void removeCollection(String collectionName, MongoDatabase db) {
        MongoCollection<Document> computes = db.getCollection(collectionName);
        computes.drop();
    }

    public void readCompute(String colName, MongoDatabase db) {
        MongoCollection<Document> computes = db.getCollection(colName);
//        Document byRegion = new Document("regions", "america_north");

        FindIterable<Document> document = computes.find();
        MongoCursor<Document> cursor = document.iterator();
        while (cursor.hasNext()) {
//            Document doc = cursor.next();
            System.out.println(cursor.next());
        }
    }

    public void scrape(String remoteUrl, String group, String region, MongoDatabase db) {

        MongoCollection collection = db.getCollection(group);

        try {
            URL url = new URL(remoteUrl);
            org.jsoup.nodes.Document doc = Jsoup.parse(url, 3000);

            ArrayList<String> downServers = new ArrayList<>();
            Elements tables = doc.select("section[data-group='" + group + "'] div.serv_table table.sortable"); //select the first table.
            Element table = tables.get(0);
            Elements rows = table.select("tr");
            System.out.println("Scrape from cloutharmony.com Region : " + region + " Service : " + group);
            for (int i = 0; i < 1; i++) { //first row is the col names so skip it.
                Element row = rows.get(i);
                Elements colsTd = row.select("th");

                for (int j = 0; j < colsTd.size(); j++) {
                    Element col = colsTd.get(j);

                    downServers.add(col.text());
//                    System.out.println(colsTd.get(j));
                }

            }

            for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
                Element row = rows.get(i);

                Elements cols = row.select("td");
                Document docs = new Document();

                for (int j = 0; j < cols.size(); j++) {

                    Element col = cols.get(j);

                    docs.put(downServers.get(j), col.text());

                }

                docs.put("regions", region);
//                System.out.println("Read : "+i+1);
                collection.insertOne(docs);

            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        
        Skyline skyline = new Skyline();
        skyline.filterProvider();
        
        List<String> paramList = new ArrayList<>();
        MongoConnector connector = new MongoConnector();
        Scraper s = new Scraper();
        MongoDatabase db = connector.getDatabase();

        paramList.add("asia");
        paramList.add("eu");
        paramList.add("oceania");
//        paramList.add("africa");
        paramList.add("america_north");
        paramList.add("america_south");

        List<String> names = new ArrayList<>();
        names.add("compute");
        names.add("storage");
        names.add("cdn");
        names.add("dns");
        names.add("paas");
        
        for (String name : names) {
            s.removeCollection(name, db);
        }
        for (String region : paramList) {
            String remoteUrl = "https://cloudharmony.com/status-in-" + region;
            for (String name : names) {
                //   s.readCompute(name, db);

                s.scrape(remoteUrl, name, region, db);
            }
        }

        System.out.println("Finished scraping");
//        s.readCompute("compute", db);
    }
}
