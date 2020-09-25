package fr.insalyon.waso.sma;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.insalyon.waso.util.JsonHttpClient;
import fr.insalyon.waso.util.JsonServletHelper;
import fr.insalyon.waso.util.exception.ServiceException;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author WASO Team
 */
public class ServiceMetier {

    protected final String somClientUrl;
    protected final String somPersonneUrl;
    protected final String somContactUrl;
    protected final String somStructureUrl;
    protected final String somProduitUrl;
    protected final String somContratUrl;
    protected final JsonObject container;

    protected JsonHttpClient jsonHttpClient;

    public ServiceMetier(String somClientUrl, String somPersonneUrl, String somContactUrl, String somStructureUrl, String somProduitUrl, String somContratUrl, JsonObject container) {
        this.somClientUrl = somClientUrl;
        this.somPersonneUrl = somPersonneUrl;
        this.somContactUrl = somContactUrl;
        this.somStructureUrl = somStructureUrl;
        this.somProduitUrl = somProduitUrl;
        this.somContratUrl = somContratUrl;
        this.container = container;

        this.jsonHttpClient = new JsonHttpClient();
    }

    public void release() {
        try {
            this.jsonHttpClient.close();
        } catch (IOException ex) {
            // Ignorer
        }
    }

    public void getListeClient() throws ServiceException {
        try {

            // 1. Obtenir la liste des Clients
            
            JsonObject clientContainer = this.jsonHttpClient.post(
                    this.somClientUrl,
                    new JsonHttpClient.Parameter("SOM", "getListeClient")
            );

            if (!JsonHttpClient.checkJsonObject(clientContainer)) {
                throw JsonServletHelper.ServiceObjectMetierCallException(this.somClientUrl, "Client", "getListeClient");
            }

            JsonArray jsonOutputClientListe = clientContainer.getAsJsonArray("clients");


            // 2. Obtenir la liste des Personnes
            
            JsonObject personneContainer = this.jsonHttpClient.post(
                    this.somPersonneUrl,
                    new JsonHttpClient.Parameter("SOM", "getListePersonne")
            );

            if (!JsonHttpClient.checkJsonObject(personneContainer)) {
                throw JsonServletHelper.ServiceObjectMetierCallException(this.somPersonneUrl, "Personne", "getListePersonne");
            }


            // 3. Indexer la liste des Personnes
            
            HashMap<Integer, JsonObject> personnes = new HashMap<Integer, JsonObject>();

            for (JsonElement p : personneContainer.getAsJsonArray("personnes")) {

                JsonObject personne = p.getAsJsonObject();

                personnes.put(personne.get("id").getAsInt(), personne);
            }


            // 4. Construire la liste des Personnes pour chaque Client (directement dans le JSON)

            for (JsonElement clientJsonElement : jsonOutputClientListe.getAsJsonArray()) {

                JsonObject client = clientJsonElement.getAsJsonObject();

                JsonArray personnesID = client.get("personnes-ID").getAsJsonArray();

                JsonArray outputPersonnes = new JsonArray();

                for (JsonElement personneID : personnesID) {
                    JsonObject personne = personnes.get(personneID.getAsInt());
                    outputPersonnes.add(personne);
                }

                client.add("personnes", outputPersonnes);

            }


            // 5. Ajouter la liste de Clients au conteneur JSON

            this.container.add("clients", jsonOutputClientListe);

        } catch (IOException ex) {
            throw JsonServletHelper.ServiceMetierExecutionException("getListeClient", ex);
        }
    }

    void rechercherClientParDenomination(String denomination, String ville) throws ServiceException {
         try {

            // 1. Obtenir la liste des Clients concernés
            
            JsonObject clientContainer = this.jsonHttpClient.post(
                    this.somClientUrl,
                    new JsonHttpClient.Parameter("SOM", "rechercherClientParDenomination"),
                    new JsonHttpClient.Parameter("denomination", denomination),
                    new JsonHttpClient.Parameter("ville", ville)
            );

            if (!JsonHttpClient.checkJsonObject(clientContainer)) {
                throw JsonServletHelper.ServiceObjectMetierCallException(this.somClientUrl, "Client", "rechercherClientParDenomination");
            }

            JsonArray jsonOutputClientListe = clientContainer.getAsJsonArray("clients");


            // 2. Obtenir la liste des Personnes
            
            JsonObject personneContainer = this.jsonHttpClient.post(
                    this.somPersonneUrl,
                    new JsonHttpClient.Parameter("SOM", "getListePersonne")
            );

            if (!JsonHttpClient.checkJsonObject(personneContainer)) {
                throw JsonServletHelper.ServiceObjectMetierCallException(this.somPersonneUrl, "Personne", "getListePersonne");
            }


            // 3. Indexer la liste des Personnes
            
            HashMap<Integer, JsonObject> personnes = new HashMap<Integer, JsonObject>();

            for (JsonElement p : personneContainer.getAsJsonArray("personnes")) {

                JsonObject personne = p.getAsJsonObject();

                personnes.put(personne.get("id").getAsInt(), personne);
            }


            // 4. Construire la liste des Personnes pour chaque Client (directement dans le JSON)

            for (JsonElement clientJsonElement : jsonOutputClientListe.getAsJsonArray()) {

                JsonObject client = clientJsonElement.getAsJsonObject();

                JsonArray personnesID = client.get("personnes-ID").getAsJsonArray();

                JsonArray outputPersonnes = new JsonArray();

                for (JsonElement personneID : personnesID) {
                    outputPersonnes = this.jsonHttpClient.post(
                    this.somPersonneUrl,
                    new JsonHttpClient.Parameter("SOM", "getPersonneParId"),
                    new JsonHttpClient.Parameter("id-personne", personneID.getAsString())
                            
                    ).getAsJsonArray("personnes");
                }

                client.add("personnes", outputPersonnes);

            }


            // 5. Ajouter la liste de Clients au conteneur JSON

            this.container.add("clients", jsonOutputClientListe);

        } catch (IOException ex) {
            throw JsonServletHelper.ServiceMetierExecutionException("getListeClient", ex);
        }
    }

    void rechercherClientParNumero(Integer numero) throws ServiceException {
        try {

            // 1. Obtenir la liste des Clients concernés
            
            JsonObject clientContainer = this.jsonHttpClient.post(
                    this.somClientUrl,
                    new JsonHttpClient.Parameter("SOM", "rechercherClientParNumero"),
                    new JsonHttpClient.Parameter("numero", String.valueOf(numero))
            );

            if (!JsonHttpClient.checkJsonObject(clientContainer)) {
                throw JsonServletHelper.ServiceObjectMetierCallException(this.somClientUrl, "Client", "rechercherClientParNumero");
            }

            JsonArray jsonOutputClientListe = clientContainer.getAsJsonArray("clients");


            // 2. Obtenir la liste des Personnes
            
            JsonObject personneContainer = this.jsonHttpClient.post(
                    this.somPersonneUrl,
                    new JsonHttpClient.Parameter("SOM", "getListePersonne")
            );

            if (!JsonHttpClient.checkJsonObject(personneContainer)) {
                throw JsonServletHelper.ServiceObjectMetierCallException(this.somPersonneUrl, "Personne", "getListePersonne");
            }


            // 3. Indexer la liste des Personnes
            
            HashMap<Integer, JsonObject> personnes = new HashMap<Integer, JsonObject>();

            for (JsonElement p : personneContainer.getAsJsonArray("personnes")) {

                JsonObject personne = p.getAsJsonObject();

                personnes.put(personne.get("id").getAsInt(), personne);
            }


            // 4. Construire la liste des Personnes pour chaque Client (directement dans le JSON)

            for (JsonElement clientJsonElement : jsonOutputClientListe.getAsJsonArray()) {

                JsonObject client = clientJsonElement.getAsJsonObject();

                JsonArray personnesID = client.get("personnes-ID").getAsJsonArray();

                JsonArray outputPersonnes = new JsonArray();

                for (JsonElement personneID : personnesID) {
                    outputPersonnes = this.jsonHttpClient.post(
                    this.somPersonneUrl,
                    new JsonHttpClient.Parameter("SOM", "getPersonneParId"),
                    new JsonHttpClient.Parameter("id-personne", personneID.getAsString())
                            
                    ).getAsJsonArray("personnes");
                }

                client.add("personnes", outputPersonnes);

            }


            // 5. Ajouter la liste de Clients au conteneur JSON

            this.container.add("clients", jsonOutputClientListe);

        } catch (IOException ex) {
            throw JsonServletHelper.ServiceMetierExecutionException("getListeClient", ex);
        }
    }



}
