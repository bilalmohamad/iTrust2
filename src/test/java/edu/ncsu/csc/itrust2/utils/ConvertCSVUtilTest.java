package edu.ncsu.csc.itrust2.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.Passenger;

/**
 * This file tests the ConvertCSVUtil.java file.
 *
 * @author Bilal Mohamad
 * @author Tanvi Thummar
 *
 */
public class ConvertCSVUtilTest {

    /** Filename of the first 14 entries of the passenger-data.csv file */
    public static final String SHORT_FILE = "passenger-data-short.csv";

    /** Filename of the passenger-data.csv file */
    public static final String LONG_FILE  = "passenger-data.csv";

    /**
     * Tests with the shorter file containing only 14 entries
     */
    @Test
    public void testConvertCSVShort () {

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

        final ArrayList<Passenger> p = ConvertCSVUtil.convertCSV( fileString );

        assertEquals( 14, p.size() );

    }

    /**
     * Tests with the all entries
     */
    @Test
    public void testConvertCSV () {
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

        final ArrayList<Passenger> p = ConvertCSVUtil.convertCSV( fileString );

        assertEquals( 1209, p.size() );
    }
}
