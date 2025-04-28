import javax.swing.*;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NewsFeed {
    private Scanner input = new Scanner(System.in);


    // Constructor
    public NewsFeed(){
//        System.out.println("Enter a one-word query about a topic you are interested in: ");
//        String q = input.nextLine();
//        for(int i = 0; i < 3; i++){
//            NewsApiReader.request(q, i);
//        }

        Front front = new Front(this);
    }

    // Gemini code that gets articles
    public static String getArticleText(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url).get();

            Elements articleElements = doc.select("div.article-body p"); // Example: selecting <p> tags within a div with class "article-body"
            if (articleElements.isEmpty()) {
                articleElements = doc.select("article p"); // Another common possibility
            }
            if (articleElements.isEmpty()) {
                // Add more selectors based on your inspection of the target website's HTML
                System.err.println("Warning: Could not find article content using common selectors. Inspect the target website's HTML.");
                return "";
            }

            StringBuilder text = new StringBuilder();
            for (Element paragraph : articleElements) {
                text.append(paragraph.text()).append("\n\n");
            }
            filterArticle(text.toString().trim());
            return text.toString().trim();

        } catch (IOException e) {
            System.err.println("Error fetching or parsing URL: " + url + " - " + e.getMessage());
            throw e;
        }
    }

    public static String filterArticle(String article){
        article = article.toLowerCase();
        article = article.replaceAll("[^a-z\\s]", "");
        List<String> words = Arrays.asList(article.split("\\s+"));
        System.out.println("------------");
        System.out.println(words);
        System.out.println("------------");
        return article;
    }

    // Main method
    public static void main(String[] args) {
        NewsFeed news = new NewsFeed();

        String articleUrl = "https://www.forbes.com/sites/johnhyatt/2025/04/28/from-elon-musks-assistant-to-spacex-investor-meet-elissa-butterfield/";
        // ny post works
        // new yorker works
        // wsj doesn't --forbes
        try {
            String articleText = getArticleText(articleUrl);
            System.out.println("Article Text:\n" + articleText);
        } catch (IOException e) {
            // Handle the exception
        }
    }
}
