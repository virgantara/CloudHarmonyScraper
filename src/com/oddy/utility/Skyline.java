/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddy.utility;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.oddy.skyline.SkylineObject;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import trial.MongoConnector;

/**
 *
 * @author ASUS
 */
public class Skyline {

    public void filterProvider() {
        List<SkylineObject> providers = new ArrayList<>();
        MongoConnector conn = new MongoConnector();
        MongoDatabase db = conn.getDatabase();

        MongoCollection collection = db.getCollection("cloudscore");

        FindIterable<Document> document = collection.find();
        MongoCursor<Document> cursor = document.iterator();

        while (cursor.hasNext()) {
            Document d = cursor.next();
            SkylineObject p = new SkylineObject();
            p.setCost(Double.parseDouble(d.get("cost").toString()));
            p.setAvailability(Double.parseDouble(d.get("availability").toString()));
            p.setName(d.get("name").toString());

            providers.add(p);
        }

        List<SkylineObject> skylines = blockNestedLoop(providers);

        List<Document> list = new ArrayList<>();

        MongoCollection collSkyline = db.getCollection("skyline");
        for (SkylineObject obj : skylines) {
            collSkyline.deleteOne(new Document("name", obj.getName()));
            Document doc = new Document("name", obj.getName())
                    .append("cost", obj.getCost())
                    .append("availability", obj.getAvailability());

            list.add(doc);
            System.out.println(obj.toString());
        }

        collSkyline.insertMany(list);
    }

    public List<SkylineObject> blockNestedLoop(List<SkylineObject> providers) {
        List<SkylineObject> skylineWindow = new ArrayList<>();
        skylineWindow.add(providers.get(0));

        for (int i = 0; i < providers.size(); i++) {

            SkylineObject p = providers.get(i);

            boolean isDominate = false;
            for (int j = 0; j < skylineWindow.size(); j++) {
                SkylineObject q = skylineWindow.get(j);
                if (p.equals(q)) {
                    continue;
                }

                isDominate = Utils.isDominate(p, q);

                if (isDominate) {
                    skylineWindow.remove(q);
                    skylineWindow.add(p);

                    break;
                } else if (!isDominate) {

                    boolean isDominateOneD = false;
                    for (int k = j; k < skylineWindow.size(); k++) {
                        SkylineObject r = skylineWindow.get(k);

                        if (Utils.isDominate(r, p)) {
                            isDominateOneD = false;
                        } else if (Utils.isDominate(p, r)) {
                            skylineWindow.add(p);
                            skylineWindow.remove(r);
                            isDominateOneD = false;
                        } else if (p.getCost() <= r.getCost() && r.getAvailability() >= p.getAvailability()) {

                            isDominateOneD = true;

                        } else if (p.getAvailability() >= r.getAvailability() && r.getCost() <= p.getCost()) {
                            isDominateOneD = true;
                        }
                    }

                    if (isDominateOneD) {
                        skylineWindow.add(p);
                        break;
                    } else {
                        break;
                    }
                }

            }

        }

        for (int i = skylineWindow.size() - 1; i >= 0; i--) {
            SkylineObject p = skylineWindow.get(i);
            for (int j = skylineWindow.size() - 1; j >= 0; j--) {
                SkylineObject q = skylineWindow.get(j);

                if (!p.equals(q) && Utils.isDominate(p, q)) {
                    skylineWindow.remove(q);
                }
            }

        }
        return skylineWindow;
    }
}
