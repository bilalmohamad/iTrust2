package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.utils.ConvertCSVUtil;

/**
 * Tests the Passenger R0 calculations.
 * Test values were calculated using Microsoft Excel Formulas to ensure accurate test values.
 * 
 * @author Bilal Mohamad
 *
 */
public class PassengerTest {

    /** Filename of the first 14 entries of the passenger-data.csv file */
    public static final String SHORT_FILE = "passenger-data-short.csv";
    
    /** Filename of the passenger-data.csv file */
    public static final String LONG_FILE  = "passenger-data.csv";
    
    /**
	 * Ensures that the R0 value is calculated correctly.
	 * The value should be >1 which would indicate the disease is spreading.
	 */
	@Test
	public void testR0() {
		
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
	    
	    
	    // Checks the R0 calculation. 
	    assertEquals(1.75, Passenger.averageRNaught(), 0.1);
	}

	/**
     * Ensures that the R0 value is calculated correctly.
     * The value should be >1 which would indicate the disease is spreading.
     */
	@Test
	public void testR0ShortFile() {
		
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
        
        
        // Checks the R0 calculation. 
        assertEquals(1.75, Passenger.averageRNaught(), 0.1);
	}
}
