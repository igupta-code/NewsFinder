import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class NewsApiReader {
    private static String urlString;
    // 0 = New Yorker, 1 = Forbes, 2 = NYpost
    public static final String[][] sources = new String[3][4]; // {"newyorker.com", "wsj.com", "nypost.com"};
    public static final String[][] urls = new String[3][3];
    // j = 0 --> sources, j = 1 -->
    public static final String API_KEY = "46e2c3a4dce646d48371cc4391690830";
    public static final int HITS = 3;


    public static void request(String q, int source) {
        try {
            sources[0][0] = "newyorker.com";
            sources[1][0] = "forbes.com";
            sources[2][0] = "nypost.com";


            // New yorker url
            String urlString = "https://newsapi.org/v2/everything?domains=" + sources[source][0] + "&q=" + q + "&sortBy=relevance" +"&pageSize=" + HITS + "&apiKey=" + API_KEY;


            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                processJson(response.toString(), source);

            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processJson(String json, int source) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        if (jsonObject.get("status").getAsString().equals("ok")) {
            JsonArray articles = jsonObject.getAsJsonArray("articles");
            for (int i = 0; i < articles.size(); i++) {
                JsonObject article = articles.get(i).getAsJsonObject();
                String title = article.get("title").getAsString();
                String description = article.has("description") && !article.get("description").isJsonNull() ? article.get("description").getAsString() : "No description available";
                String url = article.get("url").getAsString();
                sources[source][i+1] = title;
                urls[source][i] = url;

                System.out.println("Title: " + title);
                System.out.println("Description: " + description);
                System.out.println("URL: " + url);
                System.out.println("------------------------------------");
            }
        } else {
            System.out.println("API Error: " + jsonObject.get("message").getAsString());
        }
    }


}