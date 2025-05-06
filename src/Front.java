import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
        setSize(1600, 1200); // Increased size to accommodate columns
        setLocationRelativeTo(null);


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
        // added
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(sourcesPanel), BorderLayout.CENTER); // Add scroll pane for all sources

        // Add action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = keywordTextField.getText().trim();
                for(int i = 0; i < 3; i++){
                    NewsApiReader.request(keyword, i);
                }
                displaySourcesAndArticles();
            }
        });

        setVisible(true);
    }

    private void displaySourcesAndArticles() {
        sourcesPanel.removeAll(); // Clear previous content

        // Prints all three Columns
        for(int i = 0; i < 3; i++){

            // Prints border and source titles
            String sourceName = NewsApiReader.sources[i][0];
            JPanel sourceColumn = new JPanel();
            sourceColumn.setLayout(new BorderLayout());
            sourceColumn.setBorder(BorderFactory.createTitledBorder(sourceName));

            // Prints articles in the Column
            JList<String> articleList;
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for(int j = 0; j < NewsApiReader.HITS; j++){
                listModel.addElement(NewsApiReader.sources[i][j+1]);
                // Get frequent words
                String url = NewsApiReader.urls[i][j];
                try {String[] frequentWords = NewsFeed.getRepeatedWords(url);}
                catch (IOException e){ System.exit(0);}




            }
            articleList = new JList<>(listModel);
            sourceColumn.add(articleList, BorderLayout.CENTER);

            sourcesPanel.add(sourceColumn);
        }

        sourcesPanel.revalidate();
        sourcesPanel.repaint();
    }

}
