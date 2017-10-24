/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trial;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author ASUS
 */
public class MongoConnector {
    private final String C_CONN_ATLAS = "mongodb://admin:admin@cluster0-shard-00-00-cfus9.mongodb.net:27017,cluster0-shard-00-01-cfus9.mongodb.net:27017,cluster0-shard-00-02-cfus9.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";
    private final String C_CONN_SIAKAD = "mongodb://admin:admin@101.50.1.164:27017/admin";
    private MongoDatabase database = null;
    
    public MongoConnector(){
        MongoClientURI uri = new MongoClientURI(C_CONN_SIAKAD);
        MongoClient mongoClient = new MongoClient(uri);
        this.database = mongoClient.getDatabase("ahp");
    }
    
    public MongoDatabase getDatabase(){
        return this.database;
    }
}
