package edu.ncsu.csc.itrust2.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.models.persistent.Patient;

/**
 * This file tests the PassengerToPatientUtil file
 * 
 * @author Bilal Mohamad (bmohama)
 *
 */
public class PassengerToPatientUtilTest {
	
    /** Filename of the first 14 entries of the passenger-data.csv file */
    public static final String SHORT_FILE = "passenger-data-short.csv";
    
    /** Filename of the passenger-data.csv file */
    public static final String LONG_FILE  = "passenger-data.csv";

	
    /**
     * This method tests the passengerToUserAll method
     */
	@Test
	public void testPassengerToUserAll() {
		
    	setUp();
    	
//    	List<User> userList = User.getUsers();
//    	List<Passenger> pList = Passenger.getPassengers();
//    	int preUserCount = User.getUsers().size();
    	
    	PassengerToPatientUtil.passengerToPatientAll();
    	
    	List<Patient> patientList = Patient.getPatients();    	
    	assertEquals(18, patientList.size());
    	
//    	for (int i = 0; i < userList.size(); i++) {
//    		System.out.println(userList.get(i).getId() + "&User");
//    	}
//    	
//    	for (int i = 0; i < patientList.size(); i++) {
//    		System.out.println(patientList.get(i).getFirstName() + "$Patient");
//    	}
	}
	
	
    /**
     * This method tests the passengerToUserAll method. (Uses the LONG_FILE)
     */
	@Test
	public void testPassengerToUserAllLongFile() {
		
    	setUpLongFile();
    	
    	PassengerToPatientUtil.passengerToPatientAll();
    	
    	List<Patient> patientList = Patient.getPatients();    	
    	assertEquals(1213, patientList.size());
	}
	
	
	/**
	 * Sets up the data for testing. (Uses the SHORT_FILE)
	 */
	private void setUp() {
		
    	Passenger.deleteAll();
    	Patient.deleteAll();
    	
    	// This test includes 4 Passengers from HibernateData.generatePassengers (18 total)
    	addDataShortFile();
    	try {
			HibernateDataGenerator.generatePassengers();
		} catch (NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
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
    
    
	/**
	 * Sets up the data for testing. (Uses the LONG_FILE)
	 */
    private void setUpLongFile() {
    	
    	Passenger.deleteAll();
    	Patient.deleteAll();
    	
		// Reads the file to create a list of Passengers
	    Scanner s = null;
	    try {
	        s = new Scanner( new File( LONG_FILE ) );
	    }
	    catch ( final FileNotFoundException e ) {
	        e.printStackTrace();
	    }
	
	    String fileString = "";
	    while ( s.hasNextLine() ) {
	        fileString = fileString + s.nextLine() + "\n";
	    }
	
	    final ArrayList<Passenger> plist = ConvertCSVUtil.convertCSV( fileString );
	    assertEquals( 1209, plist.size() );
	    
	    for (int i = 0; i < plist.size(); i++) {
	    	plist.get(i).save();
	    }
	    
    	// This test includes 4 Passengers from HibernateData.generatePassengers (1213 total)
    	try {
			HibernateDataGenerator.generatePassengers();
		} catch (NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
    }
}
