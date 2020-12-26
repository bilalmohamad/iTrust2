package edu.ncsu.csc.itrust2.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.ncsu.csc.itrust2.models.enums.SeverityCode;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;

/**
 * This file contains the static methods that will be used to create a SortedMap
 * to return to the APIPlotStatisticsController.java file. The SortedMaps will
 * contain data points that will be used to create a graph.
 *
 * @author Bilal Mohamad (bmohama)
 * @author Marwah Mahate (msmahate)
 *
 */
public class GraphDataUtil {

    /**
     * Returns a SortedMap containing the number of infected patients per
     * LocalDate based on the Passengers in the database. This method
     * accumulates the total number of infected patients.
     *
     * @return a SortedMap of the infected patients per day
     */
    public static SortedMap<LocalDate, Integer> numberInfectedPerDay () {

        final SortedMap<LocalDate, Integer> newInfections = newInfectionsPerDay();

        if ( newInfections == null ) {
            return null;
        }

        final SortedMap<LocalDate, Integer> numberInfected = new TreeMap<LocalDate, Integer>();

        int sum = 0;

        final Iterator<Entry<LocalDate, Integer>> iter = newInfections.entrySet().iterator();

        while ( iter.hasNext() ) {
            final Entry<LocalDate, Integer> currentEntry = iter.next();

            sum += currentEntry.getValue();

            numberInfected.put( currentEntry.getKey(), sum );
        }

        return numberInfected;
    }

    /**
     * Returns a SortedMap containing the number of new infections per LocalDate
     * based on the Passengers in the database. This method is very similar to
     * the countByDate method from the Passenger.java class, but does not
     * account for the non-infected passengers.
     *
     * @return a SortedMap of the new infections per day
     */
    @SuppressWarnings ( "deprecation" )
    public static SortedMap<LocalDate, Integer> newInfectionsPerDay () {

        final List<Passenger> passengers = Passenger.getPassengers();

        if ( passengers.size() == 0 ) {
            return null;
        }

        final SortedMap<LocalDate, Integer> newInfected = new TreeMap<LocalDate, Integer>();

        for ( final Passenger passenger : passengers ) {

            // Skips the passenger if they are not infected.
            if ( passenger.getSeverityCode() == SeverityCode.N ) {
                continue;
            }

            Integer frequency = newInfected.get( passenger.getStartDateOfSymptoms() );
            if ( frequency == null ) {
                frequency = new Integer( 0 );
            }

            newInfected.put( passenger.getStartDateOfSymptoms(), new Integer( frequency.intValue() + 1 ) );
        }

        return newInfected;
    }

    /**
     * Returns a SortedMap containing the number of patients by severity based
     * on the Passengers in the database
     *
     * @return a SortedMap of the patients by severity
     */
    public static SortedMap<SeverityCode, Integer> patientBySeverity () {

        final List<Passenger> passengers = Passenger.getPassengers();

        if ( passengers.size() == 0 ) {
            return null;
        }

        int c = 0;
        int m = 0;
        int n = 0;
        int s = 0;

        for ( final Passenger passenger : passengers ) {

            if ( passenger.getSeverityCode() == SeverityCode.C ) {
                c++;
            }
            else if ( passenger.getSeverityCode() == SeverityCode.M ) {
                m++;
            }
            else if ( passenger.getSeverityCode() == SeverityCode.N ) {
                n++;
            }
            else if ( passenger.getSeverityCode() == SeverityCode.S ) {
                s++;
            }
        }

        final SortedMap<SeverityCode, Integer> bySeverity = new TreeMap<SeverityCode, Integer>();

        // Initializes each of the SeveryCode types to ensure that each code
        // will have an entry
        // even if it is not found in database
        bySeverity.put( SeverityCode.C, c );
        bySeverity.put( SeverityCode.M, m );
        bySeverity.put( SeverityCode.N, n );
        bySeverity.put( SeverityCode.S, s );

        return bySeverity;
    }

    /**
     * Converts a SortedMap that contains a LocalDate object as the key to an
     * ArrayList
     *
     * @param dataMap
     *            the SortedMap to convert
     *
     * @return an ArrayList of Strings containing the data from the SortedMap
     */
    public static ArrayList<String> localDateMapToString ( final SortedMap<LocalDate, Integer> dataMap ) {

        final ArrayList<String> dataList = new ArrayList<String>();

        final Iterator<Entry<LocalDate, Integer>> iter = dataMap.entrySet().iterator();

        while ( iter.hasNext() ) {

            final Entry<LocalDate, Integer> currentEntry = iter.next();

            final String dataPoint = currentEntry.getKey() + "," + currentEntry.getValue();

            dataList.add( dataPoint );
        }

        return dataList;
    }
}
