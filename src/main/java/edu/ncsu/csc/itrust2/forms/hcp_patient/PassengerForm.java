package edu.ncsu.csc.itrust2.forms.hcp_patient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.Passenger;

/**
 * Form for user to fill out to add a Passenger to the system.
 *
 * Sample passenger from CSV file 3b9aca00, "Reuchlin, Jonkheer, J. G.", M,
 * 2020/02/16 21:26:34
 *
 * @author Marwah Mahate
 *
 */
public class PassengerForm {

    /**
     * Constructor generates empty sets.
     */
    public PassengerForm () {
    }

    /**
     * Populate the patient form from a patient object
     *
     * @param passenger
     *            the passenger object to set the form with
     */
    public PassengerForm ( final Passenger passenger ) {
        if ( null == passenger ) {
            return; /* Nothing to do here */
        }

        setId( passenger.getId() );
        setName( passenger.getName() );
        setPassengerId( passenger.getPassengerId() );

        if ( null != passenger.getSeverityCode() ) {
            setSeverityCode( passenger.getSeverityCode().toString() );
        }

        setStartDateOfSymptoms( passenger.getStartDateOfSymptoms().toString() );
    }

    /**
     * Retrieves the id
     * @return id
     */
    public Long getId () {
        return id;
    }

    /**
     * Sets the current id to the entered parameter 
     * @param id the Long containing the new id
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Retrieves the name
     * @return name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the current name to the entered parameter 
     * @param name the String containing the new name
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Retrieves the passengerId
     * @return passengerId
     */
    public String getPassengerId () {
        return passengerId;
    }

    /**
     * Sets the current passengerId to the entered parameter 
     * @param passengerId the String containing the new passengerId
     */
    public void setPassengerId ( final String passengerId ) {
        this.passengerId = passengerId;
    }

    /**
     * Retrieves the severityCode
     * @return severityCode
     */
    public String getSeverityCode () {
        return severityCode;
    }

    /**
     * Sets the current severityCode to the entered parameter 
     * @param severityCode the String containing the new severityCode
     */
    public void setSeverityCode ( final String severityCode ) {
        this.severityCode = severityCode;
    }

    /**
     * Retrieves the startDateOfSymptoms
     * @return startDateOfSymptoms
     */
    public String getStartDateOfSymptoms () {
        return startDateOfSymptoms;
    }

    /**
     * Sets the current startDateOfSymptoms to the entered parameter 
     * @param startDateOfSymptoms the String containing the new startDateOfSymptoms
     */
    public void setStartDateOfSymptoms ( final String startDateOfSymptoms ) {
        this.startDateOfSymptoms = startDateOfSymptoms;
    }

    /** The id of the passenger **/
    private Long   id;

    /** The name of the passenger **/
    @NotEmpty
    @Length ( max = 10 )
    private String name;

    /** The id of the passenger **/
    @NotEmpty
    @Length ( max = 30 )
    private String passengerId;

    /** The severity code of residence of the passenger **/
    @NotEmpty
    @Length ( min = 1, max = 1 )
    private String severityCode;

    /** The start date of symptoms for the passenger **/
    @NotEmpty
    @Length ( min = 19, max = 19 )
    private String startDateOfSymptoms;

}
