import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
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

    // Function returns a map of words that matches each word to its number of repeats
    public static String[] getRepeatedWords(String url) throws IOException {
        // Gemini code that gets articles: uses jSoup to reads in article from the given url
        try {
            Document doc = Jsoup.connect(url).get();
            // Sets up css Queries
            Elements articleElements = doc.select("div.article-body p"); // Example: selecting <p> tags within a div with class "article-body"
            if (articleElements.isEmpty())
                articleElements = doc.select("article p"); // Another common possibility
            if (articleElements.isEmpty()) {
                System.err.println("Warning: Could not find article content using common selectors. Inspect the target website's HTML.");
                return new String[0];
            }

            StringBuilder text = new StringBuilder();
            for (Element paragraph : articleElements) {
                text.append(paragraph.text()).append("\n\n");
            }
            // Filters stop words out of retrieved article
            return filterArticle(text.toString().trim());

        } catch (IOException e) {
            System.err.println("Error fetching or parsing URL: " + url + " - " + e.getMessage());
            throw e;
        }
    }

    public static String[] filterArticle(String article){
        // Takes out special characters, makes lowercase, and converts to list
        article = article.toLowerCase();
        article = article.replaceAll("[^a-z\\s]", "");
        List<String> words = Arrays.asList(article.split("\\s+"));

        // Filters out the stop words (super common neutral words) from article and returns
        words = words.stream().filter(token ->  !token.isEmpty() && !STOP_WORDS.contains(token)).collect(Collectors.toList());
        System.out.println(words);
        getMostFrequentWords(words);
        return getMostFrequentWords(words);
    }

    public static String[] getMostFrequentWords(List<String> article) {
        if (article == null || article.isEmpty()) {
            return new String[0]; // Return an empty list for null or empty input
        }

        // Count word frequencies using a HashMap
        int maxFrequency = 0;
        Map<String, Integer> wordFrequencies = new HashMap<>();
        for (String word : article) {
            int frequency = wordFrequencies.getOrDefault(word, 0) + 1;
            wordFrequencies.put(word, frequency);
            if (frequency > maxFrequency)
                maxFrequency = frequency;
        }

        // Find the maximum frequency
        // Assumption on size of array: no article repeat a word so often that it's a 4th
        String[] sortedWords = new String[maxFrequency];

        for(Map.Entry<String, Integer> frequency : wordFrequencies.entrySet()) {
           sortedWords[frequency.getValue() - 1] = frequency.getKey();
        }
        System.out.println("maxF = " + maxFrequency + " ---- " + sortedWords[maxFrequency-1]);
        return sortedWords;
    }

    // Main method
    public static void main(String[] args) {
        NewsFeed news = new NewsFeed();
    }
}
