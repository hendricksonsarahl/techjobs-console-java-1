package org.launchcode.techjobs.console;

import java.util.*;

/**
 * Created by LaunchCode and Sarah Hendrickson
 */
public class TechJobs {

    private static Scanner in = new Scanner(System.in);

    public static void main (String[] args) {

        // Initialize our field map with key/name pairs
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        // Top-level menu options
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("1", "Search");
        actionChoices.put("2", "List");
        actionChoices.put("0", "Quit");

        System.out.println("Welcome to LaunchCode's TechJobs App!");

        // Allow the user to search until they manually quit
        while (true) {
            // User can select "0" and quit the program from top-level menu in console
            String actionChoice = getUserSelection("View jobs by:", actionChoices);
            if (actionChoice.equals("0")){
                break;
            }
            else if (actionChoice.equals("2")) {

                String columnChoice = getUserSelection("List", columnChoices);

                if (columnChoice.equals("all")) {
                    printJobs(JobData.findAll());
                } else {

                    ArrayList<String> results = JobData.findAll(columnChoice);
                    System.out.println("\n*** All " + columnChoices.get(columnChoice) + " Values ***");
                    // Sort the list alphabetically
                    Collections.sort(results);
                    // Print list of skills, employers, etc
                    for (String item : results) {
                        System.out.println(item);
                    }
                }

            } else { // choice is "search"

                // How does the user want to search (e.g. by skill or employer)
                String searchField = getUserSelection("Search by:", columnChoices);

                // What is their search term?
                System.out.println("\nSearch term: ");
                String searchTerm = in.nextLine();

                if (searchField.equals("all")) {
                    printJobs(JobData.findByValue(searchTerm));
                } else {
                    printJobs(JobData.findByColumnAndValue(searchField, searchTerm));
                }

            }
        }
    }

    // ﻿Returns the key of the selected item from the choices Dictionary
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {

        Integer choiceIdx;
        Boolean validChoice = false;
        String[] choiceKeys = new String[choices.size()];

        // Put the choices in an ordered structure so we can
        // associate an integer with each one
        Integer i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }

        do {

            System.out.println("\n" + menuHeader);

            // Print available choices
            for (Integer j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }

            choiceIdx = in.nextInt();
            in.nextLine();

            // Validate user's input
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }

        } while(!validChoice);

        return choiceKeys[choiceIdx];
    }

    // Print a list of jobs
    private static void printJobs(ArrayList<HashMap<String, String>> someJobs) {
        /*
        Printed output should look like:
        *****
        position type: Data Scientist / Business Intelligence
        name: Sr. IT Analyst (Data/BI)
        employer: Bull Moose Industries
        location: Saint Louis
        core competency: Statistical Analysis
        *****
         */

        // If search returned 0 results, print message. Otherwise, iterate over the loop printing each job
        // entry when the user selects "1" to list the jobs, and then "0" to list them all
        if (someJobs.size() == 0) {
            System.out.println("No jobs match your search, please try different search terms or try back later!");
        } else {
            for (HashMap<String, String> job : someJobs) {
                System.out.println("*****");
                for (Map.Entry<String, String> category : job.entrySet()) {
                    System.out.println(category.getKey() + ": " + category.getValue());
                }
                System.out.println("*****\n");
            }
        }
    }
}
