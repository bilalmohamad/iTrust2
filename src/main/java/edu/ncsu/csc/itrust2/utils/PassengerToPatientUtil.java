package edu.ncsu.csc.itrust2.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * This Util class contains a static method that will convert the existing
 * Passengers in the database and create Users and Patients in the database
 * based on those Passengers.
 *
 * @author Bilal Mohamad (bmohama)
 *
 */
public class PassengerToPatientUtil {

    /**
     * This method will convert a single passenger into a User and a Patient in
     * the database.
     * 
     * @param convertPassenger
     *            the Passenger to convert
     */
    public static void passengerToPatient ( final Passenger convertPassenger ) {

        // Creates a User to add to the database
        final String username = convertPassenger.getPassengerId();
        final String password = "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.";

        final User passengerUser = new User( username, password, Role.ROLE_PATIENT, 1 );

        passengerUser.save();

        // Creates a Patient to add to the database
        final Patient passengerPatient = new Patient();
        passengerPatient.setSelf( passengerUser );

        // Retrieves the First and Last name of the user
        final ArrayList<String> firstAndLast = getFirstAndLast( convertPassenger.getName() );
        final String firstName = firstAndLast.get( 0 );
        final String lastName = firstAndLast.get( 1 );

        // System.out.println(firstName + " " + lastName);
        passengerPatient.setFirstName( firstName );
        passengerPatient.setLastName( lastName );

        passengerPatient.save();
    }

    /**
     * Converts all the Passengers in the database to Patients
     */
    public static void passengerToPatientAll () {

        final List<Passenger> passengerList = Passenger.getPassengers();

        for ( int i = 0; i < passengerList.size(); i++ ) {
            passengerToPatient( passengerList.get( i ) );
        }
    }

    /**
     * Retrieves the first and last name of the Passenger
     * 
     * @param name
     *            the full name of the Passenger
     * 
     * @return an ArrayList of Strings containing the first name in index 0 the
     *         last name in index 1
     */
    private static ArrayList<String> getFirstAndLast ( final String name ) {

        final ArrayList<String> firstAndLast = new ArrayList<String>();

        final Scanner nameScanner = new Scanner( name );

        if ( !name.contains( "," ) ) {
            nameScanner.useDelimiter( " " );
        }
        else {
            nameScanner.useDelimiter( ", " );
        }

        try {

            // Gets the first name
            String first = "";
            if ( nameScanner.hasNext() ) {
                first = nameScanner.next();
            }

            if ( first.contains( "," ) ) {
                final int i = first.indexOf( "," );
                first = first.substring( 0, i );
            }

            // Gets the last name
            String last = "";
            if ( nameScanner.hasNext() ) {
                last = nameScanner.next();
            }

            firstAndLast.add( first );
            firstAndLast.add( last );
        }
        catch ( final Exception e ) {
            nameScanner.close();
            throw new IllegalArgumentException( "Name not valid." );
        }

        nameScanner.close();
        return firstAndLast;
    }
}
