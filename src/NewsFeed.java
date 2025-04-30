import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NewsFeed {
    private Scanner input = new Scanner(System.in);
    public static final Set<String> STOP_WORDS = loadStopWords("test_files/stopwords.txt");
    


    // Constructor
    public NewsFeed(){
        Front front = new Front(this);
    }

    public static Set<String> loadStopWords(String filePath) {
        Set<String> stopWords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath), 1024)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each stop word is on a new line in the file
                String word = line.trim().toLowerCase(); // Trim whitespace and convert to lowercase for consistency
                if (!word.isEmpty()) {
                    stopWords.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading stop words file: " + filePath + " - " + e.getMessage());
            // You might want to handle this exception differently, e.g., return an empty set or throw it.
        }
        return stopWords;
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

    public static List<String> filterArticle(String article){
        // Takes out special characters, makes lowercase, and converts to list
        article = article.toLowerCase();
        article = article.replaceAll("[^a-z\\s]", "");
        List<String> words = Arrays.asList(article.split("\\s+"));
        System.out.println("------------");
        System.out.println(words);
        System.out.println("------------");

        // Filters out most common words from article
        
        words = words.stream().filter(token ->  !token.isEmpty() && !STOP_WORDS.contains(token)).collect(Collectors.toList());
        System.out.println(words);
        return words.stream().filter(token ->  !token.isEmpty() && !STOP_WORDS.contains(token)).collect(Collectors.toList());

        // return article;




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
