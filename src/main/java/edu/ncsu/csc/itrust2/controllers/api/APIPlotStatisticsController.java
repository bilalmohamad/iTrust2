/**
 *
 */
package edu.ncsu.csc.itrust2.controllers.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.SeverityCode;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.utils.GraphDataUtil;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides REST API end-points for the plot statics functionality.
 * It passes on data to front end so the plots can be made.
 *
 * @author Tanvi Thummar (tdthumma)
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APIPlotStatisticsController extends APIController {

    /**
     * Returns a list of numbers, number of infected patients per day. The
     * string can have the number of patients and the date.
     *
     * @return List of numbers as Strings
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    @GetMapping ( BASE_PATH + "/plotStatistics/infectedPatientsPerDay" )
    public List<String> infectedPatientsPerDay () {

        List<String> infectionsPerDay = new ArrayList<String>();

        // Get the map of dates and respective number of infected patients on
        // that day.
        final SortedMap<LocalDate, Integer> mapOfInfectedPerDay = GraphDataUtil.numberInfectedPerDay();

        // Return null if there are passengers in the database.
        if ( mapOfInfectedPerDay == null ) {
            return null;
        }

        // Convert the map to a list of strings.
        // String contains the date and number of infected patients in the
        // following format:
        // Date,Number
        infectionsPerDay = GraphDataUtil.localDateMapToString( mapOfInfectedPerDay );

        // Log the event.
        LoggerUtil.log( TransactionType.PLOT_PATIENT_STATISTICS, LoggerUtil.currentUser(),
                "HCP asked for a plot for number of infected patients per day." );

        return infectionsPerDay;
    }

    /**
     * Returns a list of numbers, number of new infections per day. The string
     * can have the number of patients and the date.
     *
     * @return List of numbers as Strings
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    @GetMapping ( BASE_PATH + "/plotStatistics/newInfectionsPerDay" )
    public List<String> newInfectionsPerDay () {

        List<String> newInfectionsPerDay = new ArrayList<String>();

        // Get the map of dates and respective number of new infections on
        // that day.
        final SortedMap<LocalDate, Integer> mapOfNewInfectionsPerDay = GraphDataUtil.newInfectionsPerDay();

        // Return null if there are passengers in the database.
        if ( mapOfNewInfectionsPerDay == null ) {
            return null;
        }

        // Convert the map to a list of strings.
        // String contains the date and number of infected patients in the
        // following format:
        // Date,Number
        newInfectionsPerDay = GraphDataUtil.localDateMapToString( mapOfNewInfectionsPerDay );

        // Log the event.
        LoggerUtil.log( TransactionType.PLOT_PATIENT_STATISTICS, LoggerUtil.currentUser(),
                "HCP asked for a plot for number of new infections per day." );

        return newInfectionsPerDay;
    }

    /**
     * Returns a list of numbers, number of patients based on severity. The list
     * has the number of patients by severity in the following order: Not
     * Infected, Mild, Severe, Critical
     *
     * @return List of numbers, number of patients based on severity
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    @GetMapping ( BASE_PATH + "/plotStatistics/patientsBySeverity" )
    public List<Integer> patientsBySeverity () {

        final List<Integer> patientsBySeverity = new ArrayList<Integer>();

        // Get the map of severity codes and respective number of patients.
        final SortedMap<SeverityCode, Integer> mapBySeverity = GraphDataUtil.patientBySeverity();

        // Return null if there are passengers in the database.
        if ( mapBySeverity == null ) {
            return null;
        }

        // Convert the map to a list of integers.
        // Add the number of patients by severity in the list
        // in the following order:
        // Not Infected, Mild, Severe, Critical
        patientsBySeverity.add( mapBySeverity.get( SeverityCode.N ) );
        patientsBySeverity.add( mapBySeverity.get( SeverityCode.M ) );
        patientsBySeverity.add( mapBySeverity.get( SeverityCode.S ) );
        patientsBySeverity.add( mapBySeverity.get( SeverityCode.C ) );

        // Log the event.
        LoggerUtil.log( TransactionType.PLOT_PATIENT_STATISTICS, LoggerUtil.currentUser(),
                "HCP asked for a plot for number of patients by severity." );

        return patientsBySeverity;
    }
}
