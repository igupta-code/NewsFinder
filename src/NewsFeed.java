import javax.swing.*;
import java.sql.SQLOutput;
import java.util.Scanner;

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

    // Main method
    public static void main(String[] args){
        NewsFeed news = new NewsFeed();
        // This should be your main

    }




}
