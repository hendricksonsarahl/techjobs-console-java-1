package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode and Sarah Hendrickson
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        //create a copy of allJobs that will be returned instead of the static allJobs in case of future CSV changes
        ArrayList<HashMap<String, String >> allJobsCopy = new ArrayList<>(allJobs);
        return allJobsCopy;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of the field to search for
     * @return List of all jobs matching the criteria
     */

    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        // convert values to lowercase to search all matches accurately
        value = value.toLowerCase();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);
            aValue = aValue.toLowerCase();
            if (aValue.contains(value)) {
                jobs.add(row);
            }
        }


        return jobs;
    }

    /**
     * Returns results of search the jobs data value, using
     * inclusion of the search term, searching ALL columns.
     *
     * @param value Value of the field to search for
     * @return List of all jobs matching the criteria, excluding duplicates
     */

    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();
        // instantiate a new ArrayList called jobs
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        // make the user's entry lowercase to check against data
        value = value.toLowerCase();
        // Iterate over all data in allJobs
        for (HashMap<String, String> row: allJobs) {
            // For each key in each row
            for (String column : row.keySet()) {
                // Get the value and convert to lowercase
                String aValue = row.get(column);
                aValue = aValue.toLowerCase();
                // If you find the value in the aValue string AND jobs doesn't have the row already
                if (aValue.contains(value) && !jobs.contains(row)) {
                    // Go ahead and add the row
                    jobs.add(row);
                }

            }

        }

        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Error1: Failed to load job data");
            e.printStackTrace();
        }
    }

}
