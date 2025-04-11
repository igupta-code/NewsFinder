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
    // 0 = New Yorker, 1 = WSJ, 2 = some right source
    private int[] sources = new int[2];
    private String q,
        source;

    public NewsApiReader(String querry, String source){
        this.q = querry;
        this.source = source;
    }


    private static final String API_KEY = "46e2c3a4dce646d48371cc4391690830"; // Replace with your actual API key
    //private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";
    private static final String BASE_URL = "https://newsapi.org/v2";

    public static void main(String[] args) {
        try {

            String country = "us"; // Example: United States
            String category = "general"; // Example: Technology
            String q = "trump";
           // String urlString = BASE_URL + "?country=" + country + "&category=" + category + "&apiKey=" + API_KEY;

            // WSJ url
            // String urlString = "https://newsapi.org/v2/everything?domains=wsj.com&q=q&apiKey=" + API_KEY;

            // New yorker url
            String urlString = "https://newsapi.org/v2/everything?domains=newyorker.com&q=q&apiKey=" + API_KEY;

            // The new york post or the free press -- not on api
            // String urlString = "https://newsapi.org/v2/everything?domains=wsj.com/opinion&q=q&apiKey=" + API_KEY;





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

                processJson(response.toString());

            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        if (jsonObject.get("status").getAsString().equals("ok")) {
            JsonArray articles = jsonObject.getAsJsonArray("articles");
            for (int i = 0; i < articles.size(); i++) {
                JsonObject article = articles.get(i).getAsJsonObject();
                String title = article.get("title").getAsString();
                String description = article.has("description") && !article.get("description").isJsonNull() ? article.get("description").getAsString() : "No description available";
                String url = article.get("url").getAsString();
                String publishedAt = article.get("publishedAt").getAsString();

                System.out.println("Title: " + title);
                System.out.println("Description: " + description);
                System.out.println("URL: " + url);
                System.out.println("Published At: " + publishedAt);
                System.out.println("------------------------------------");
            }
        } else {
            System.out.println("API Error: " + jsonObject.get("message").getAsString());
        }
    }
}