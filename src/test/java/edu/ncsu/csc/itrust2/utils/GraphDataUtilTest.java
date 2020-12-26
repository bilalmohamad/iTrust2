package edu.ncsu.csc.itrust2.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.SortedMap;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.enums.SeverityCode;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;

/**
 * This file tests the GraphDataUtil.java file.
 *
 * @author Bilal Mohamad (bmohama)
 * @author Marwah Mahate (msmahate)
 *
 */
public class GraphDataUtilTest {
	
    /** Filename of the first 14 entries of the passenger-data.csv file */
    public static final String SHORT_FILE = "passenger-data-short.csv";
    
    
    /**
     * Tests the numberInfectedPerDay method.
     * The database is already pre-populated with data from the
     * generatePassengers method from the HibernateDataGenerator.java file.
     */
    @Test
    public void testNumberInfectedPerDay() {
    	
    	Passenger.deleteAll();
    	
    	// This test includes 4 Passengers from HibernateData.generatePassengers
    	addDataShortFile();
    	try {
			HibernateDataGenerator.generatePassengers();
		} catch (NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
    	
    	SortedMap<LocalDate, Integer> testMap = GraphDataUtil.numberInfectedPerDay();
    	assertEquals(13, testMap.size());
    	
    	// Tests the localDateMapToString method
    	ArrayList<String> testList = GraphDataUtil.localDateMapToString(testMap);
    	int index = 0;
    	
    	// Iterator used to iterate through each of the entries in the SortedMap
    	Iterator<Entry<LocalDate, Integer>> iter = testMap.entrySet().iterator();
    	
    	// The current entry in the SortedMap (the first entry)
    	Entry<LocalDate, Integer> currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 2), currentEntry.getKey());
    	assertEquals(1, currentEntry.getValue().intValue());
    	assertEquals(LocalDate.of(2020, 2, 2) + "," + "1", testList.get(index));
    	index++;
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 3), currentEntry.getKey());
    	assertEquals(2, currentEntry.getValue().intValue());
    	assertEquals(LocalDate.of(2020, 2, 3) + "," + "2", testList.get(index));
    	index++;
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 4), currentEntry.getKey());
    	assertEquals(5, currentEntry.getValue().intValue());
    	assertEquals(LocalDate.of(2020, 2, 4) + "," + "5", testList.get(index));
    	index++;
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 5), currentEntry.getKey());
    	assertEquals(7, currentEntry.getValue().intValue());
    	assertEquals(LocalDate.of(2020, 2, 5) + "," + "7", testList.get(index));
    	index++;
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 8), currentEntry.getKey());
    	assertEquals(8, currentEntry.getValue().intValue());
    	assertEquals(LocalDate.of(2020, 2, 8) + "," + "8", testList.get(index));
    	index++;
    	
    	// Skips to the last entry in the database
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	index += 7;
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.now(), currentEntry.getKey());
    	assertEquals(18, currentEntry.getValue().intValue());
    	assertEquals(LocalDate.now() + "," + "18", testList.get(index));
    }
    
    
    /**
     * Tests the newInfectionsPerDay method.
     * The database is already pre-populated with data from the
     * generatePassengers method from the HibernateDataGenerator.java file. 
     */
    @Test
    public void testNewInfectionsPerDay() {
    	
    	Passenger.deleteAll();
    	
    	// This test includes 4 Passengers from HibernateData.generatePassengers
    	addDataShortFile();
    	
    	try {
			HibernateDataGenerator.generatePassengers();
		} catch (NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
    	
    	SortedMap<LocalDate, Integer> testMap = GraphDataUtil.newInfectionsPerDay();
    	assertEquals(13, testMap.size());
    	
    	// Iterator used to iterate through each of the entries in the SortedMap
    	Iterator<Entry<LocalDate, Integer>> iter = testMap.entrySet().iterator();
    	
    	// The current entry in the SortedMap (the first entry)
    	Entry<LocalDate, Integer> currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 2), currentEntry.getKey());
    	assertEquals(1, currentEntry.getValue().intValue());
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 3), currentEntry.getKey());
    	assertEquals(1, currentEntry.getValue().intValue());
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 4), currentEntry.getKey());
    	assertEquals(3, currentEntry.getValue().intValue());
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 5), currentEntry.getKey());
    	assertEquals(2, currentEntry.getValue().intValue());
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.of(2020, 2, 8), currentEntry.getKey());
    	assertEquals(1, currentEntry.getValue().intValue());
    	
    	// Skips to the last entry in the database
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	iter.next();
    	
    	currentEntry = iter.next();
    	assertEquals(LocalDate.now(), currentEntry.getKey());
    	assertEquals(1, currentEntry.getValue().intValue());
    	
//    	while (iter.hasNext()) {
//    		Entry<LocalDate, Integer> e = iter.next();
//    		
//    		System.out.println(e.getKey());
//    		System.out.println(e.getValue());
//    	}
    }
    
    
    /**
     * Tests the patientBySeverity method.
     * The database is already pre-populated with data from the
     * generatePassengers method from the HibernateDataGenerator.java file. 
     */
    @Test
    public void testPatientBySeverity() {
    	
    	Passenger.deleteAll();
    	
    	// This test includes 4 Passengers from HibernateData.generatePassengers
    	addDataShortFile();
    	
    	try {
			HibernateDataGenerator.generatePassengers();
		} catch (NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
    	
    	SortedMap<SeverityCode, Integer> testMap = GraphDataUtil.patientBySeverity();
    	assertEquals(4, testMap.size());
    	
    	// Iterator used to iterate through each of the entries in the SortedMap
    	Iterator<Entry<SeverityCode, Integer>> iter = testMap.entrySet().iterator();
    	
    	// The current entry in the SortedMap (the first entry)
    	Entry<SeverityCode, Integer> currentEntry = iter.next();
    	assertEquals(SeverityCode.M, currentEntry.getKey());
    	assertEquals(10, currentEntry.getValue().intValue());
    	
    	currentEntry = iter.next();
    	assertEquals(SeverityCode.S, currentEntry.getKey());
    	assertEquals(3, currentEntry.getValue().intValue());
    	
    	currentEntry = iter.next();
    	assertEquals(SeverityCode.C, currentEntry.getKey());
    	assertEquals(5, currentEntry.getValue().intValue());
    	
    	currentEntry = iter.next();
    	assertEquals(SeverityCode.N, currentEntry.getKey());
    	assertEquals(0, currentEntry.getValue().intValue());
    	
    	// There should not be any more entries in the SortedMap
    	assertEquals(false, iter.hasNext());
    	
//    	while (iter.hasNext()) {
//			Entry<SeverityCode, Integer> e = iter.next();
//			
//			System.out.println(e.getKey());
//			System.out.println(e.getValue());
//    	}
    }
    
    
    /**
     * Adds data to the database for testing based on the SHORT_FILE
     */
    private void addDataShortFile() {
    	
		// Reads the file to create a list of Passengers
        Scanner s = null;
        try {
            s = new Scanner( new File( SHORT_FILE ) );
        }
        catch ( final FileNotFoundException e ) {
            e.printStackTrace();
        }

        String fileString = "";
        while ( s.hasNextLine() ) {
            fileString = fileString + s.nextLine() + "\n";
        }

        final ArrayList<Passenger> plist = ConvertCSVUtil.convertCSV( fileString );
        assertEquals( 14, plist.size() );
        
        for (int i = 0; i < plist.size(); i++) {
        	plist.get(i).save();
        }
    }
}
