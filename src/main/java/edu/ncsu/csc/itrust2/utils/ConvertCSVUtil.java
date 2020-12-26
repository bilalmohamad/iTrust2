package edu.ncsu.csc.itrust2.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import edu.ncsu.csc.itrust2.models.enums.SeverityCode;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;

/**
 * This file takes in a CSV file and converts it into manipulable data, an
 * ArrayList of Passenger objects.
 *
 * @author Bilal Mohamad
 * @author Tanvi Thummar
 *
 */
public class ConvertCSVUtil {

    /**
     * This method converts a CSV file into an ArrayList of Passenger objects
     *
     * @param fileString
     *            String form of the CSV file to convert
     * @return an ArrayList of containing a list of all the passengers in the
     *         list
     */
    @SuppressWarnings("resource")
	public static ArrayList<Passenger> convertCSV ( final String fileString ) {

        final Scanner input = new Scanner( fileString );

        final ArrayList<Passenger> passengerList = new ArrayList<Passenger>();

        // Keeps reading until there is no more data in the CSV file
        while ( input.hasNextLine() ) {
            final String line = input.nextLine();

            final Passenger p = new Passenger();

            // First divide the line from " in order to retrieve the name.
            final String[] parts = line.split( "\"" );

            // If we get more or less than 3 parts means the format of the file
            // is incorrect.
            if ( parts.length != 3 ) {

                throw new IllegalArgumentException();
            }

            // We know that the first part of the separated line is the id,
            // set it and check there is nothing else.
            final String[] part1 = parts[0].split( "," );

            // If we get more or less than 2 parts (one comma at end of this
            // part) here,
            // means the format of the file is incorrect.
            if ( part1.length != 2 ) {

                throw new IllegalArgumentException();
            }
            
            // Create the user
            // Use passengerId as the username
            // Use the same password for all passengers
//            User u = new User(part1[0], "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", 
//            		Role.ROLE_PATIENT, 1);
//            
//            p.setSelf(u);

            // Sets the id.
            p.setPassengerId( part1[0] );

            // We know the second part of the the separated line is the name.
            // So, set the name.
            p.setName( parts[1] );

            // We know that the third part of the separated line is the severity
            // code and date,
            // set it and check there is nothing else.
            final String[] part3 = parts[2].split( ", " );

            // If we get more or less than 3 parts (one comma in beginning of
            // this part) here,
            // means the format of the file is incorrect.
            if ( part3.length != 3 ) {

                throw new IllegalArgumentException();
            }

            // Set the severity code.
            final String code = part3[1];

            if ( code.equals( "M" ) ) {
                p.setSeverityCode( SeverityCode.M );
            }
            else if ( code.equals( "S" ) ) {
                p.setSeverityCode( SeverityCode.S );
            }
            else if ( code.equals( "C" ) ) {
                p.setSeverityCode( SeverityCode.C );
            }
            else if ( code.equals( "N" ) ) {
                p.setSeverityCode( SeverityCode.N );
            }

            // Set the date.
            final LocalDate date = formatDate( part3[2] );
            p.setStartDateOfSymptoms( date );

            // Add the Passenger to the list.
            passengerList.add( p );
        }

        input.close();
        return passengerList;
    }

    // /**
    // * This method is used to remove the quotation marks in the name string
    // *
    // * @param name
    // * the string to format
    // * @return the formatted string
    // */
    // private static String formatName ( final String name ) {
    //
    // return name.substring( 1, name.length() - 1 );
    // }

    /**
     * This method retrieves the remaining string in the Scanner that contains
     * the Date and Time. It converts this string into a LocalDate object.
     *
     * @param date
     *            a String containing the date and the time
     * @return a LocalDate object retreived from the date string
     */
    private static LocalDate formatDate ( final String date ) {

        final Scanner scan = new Scanner( date );
        // System.out.println(date);
        scan.useDelimiter( "/|\\ " );

        final int year = scan.nextInt();
        final int month = scan.nextInt();
        final int day = scan.nextInt();

        final LocalDate formatDate = LocalDate.of( year, month, day );

        scan.close();
        return formatDate;
    }
}
