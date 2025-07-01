package br.com.grupodois.cadastroturmas.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static final String URL = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "cadastro_logs";

    private static final MongoClient client = MongoClients.create(URL); // cria uma única instância

    public static MongoDatabase getDatabase() {
        return client.getDatabase(DATABASE_NAME);
    }
}
