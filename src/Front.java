import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class Front extends JFrame {

    private JTextField keywordTextField;
    private JButton searchButton;
    private JPanel sourcesPanel; // Panel to hold all source columns
    // private Map<String, List<String>> newsData;
    // private String[][] sources;
    private NewsFeed backEnd;

    public Front(NewsFeed backEnd) {
        // Connecting back to front end
        this.backEnd = backEnd;

        setTitle("News Feed Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Increased size to accommodate columns
        setLocationRelativeTo(null);

        // Initialize data
//        newsData = new String[2][2];
//
//        newsData.put("Source A", List.of("Article A1", "Article A2", "Article A3"));
//        newsData.put("Source B", List.of("Article B1", "Article B2", "Article B3"));
//        newsData.put("Source C", List.of("Article C1", "Article C2", "Article C3"));

        // Create components
        keywordTextField = new JTextField(20);
        searchButton = new JButton("Search");
        sourcesPanel = new JPanel(new GridLayout(1, 3, 0, 10)); // 1 row, 3 columns, with gaps

        // Layout the input panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Keyword:"));
        inputPanel.add(keywordTextField);
        inputPanel.add(searchButton);

        // Display initial sources and articles
        displaySourcesAndArticles();

        // Add components to the main frame
        // getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(sourcesPanel), BorderLayout.CENTER); // Add scroll pane for all sources

        // Add action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = keywordTextField.getText().trim();
                filterAndDisplayArticles(keyword);
            }
        });

        setVisible(true);
    }

    private void displaySourcesAndArticles() {
        sourcesPanel.removeAll(); // Clear previous content

        for(int i = 0; i < 3; i++){

            // Prints border and source titles
            String sourceName = NewsApiReader.sources[i][0];
            JPanel sourceColumn = new JPanel();
            sourceColumn.setLayout(new BorderLayout());
            sourceColumn.setBorder(BorderFactory.createTitledBorder(sourceName));

            // Prints articles
            JList<String> articleList = new JList<>(Arrays.copyOfRange(NewsApiReader.sources[i], 1, 3));
            sourceColumn.add(new JScrollPane(articleList), BorderLayout.CENTER);

            sourcesPanel.add(sourceColumn);

        }
//        for (Map.Entry<String, List<String>> entry : dataToShow.entrySet()) {
//            String sourceName = entry.getKey();
//            List<String> articles = entry.getValue();
//
//            JPanel sourceColumn = new JPanel();
//            sourceColumn.setLayout(new BorderLayout());
//            sourceColumn.setBorder(BorderFactory.createTitledBorder(sourceName)); // Add border for source name
//
//            JList<String> articleList = new JList<>(articles.toArray(new String[0]));
//            sourceColumn.add(new JScrollPane(articleList), BorderLayout.CENTER);
//
//            sourcesPanel.add(sourceColumn);
//            columnCount++;
//
//            // If we have less than 3 sources, add empty panels to fill the grid
//            while (columnCount < 3 && dataToShow.size() < 3) {
//                sourcesPanel.add(new JPanel()); // Empty panel
//                columnCount++;
//            }
//        }

        sourcesPanel.revalidate();
        sourcesPanel.repaint();
    }

    private void filterAndDisplayArticles(String keyword) {
        displaySourcesAndArticles();
//        Map<String, List<String>> filteredData = new HashMap<>();
//
//        for (Map.Entry<String, List<String>> entry : newsData.entrySet()) {
//            String sourceName = entry.getKey();
//            List<String> allArticles = entry.getValue();
//            List<String> filteredArticles = new ArrayList<>();
//
//            for (String article : allArticles) {
//                if (article.toLowerCase().contains(keyword.toLowerCase())) {
//                    filteredArticles.add(article);
//                }
//            }
//
//            if (!filteredArticles.isEmpty()) {
//                filteredData.put(sourceName, filteredArticles);
//            }
//        }
//
//        displaySourcesAndArticles(filteredData);
    }

    // Get rid of this later
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new Front();
//            }
//        });
//    }
}