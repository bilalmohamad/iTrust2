package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.itrust2.models.enums.SeverityCode;

/**
 * Class representing a Passenger object. This goes beyond the basic information
 * stored as part of a User and contains relevant information for a passenger in
 * our medical records system.
 *
 * @author Marwah Mahate
 *
 */
@Entity
@Table ( name = "Passengers" )
public class Passenger extends DomainObject<Passenger> implements Serializable {

    /**
     * Randomly generated serial id
     */
    private static final long serialVersionUID = -5029851962470825647L;

    /**
     * Get all Passenger in the database
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<Passenger> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @return all patients in the database
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Passenger> getPassengers () {
        return (List<Passenger>) getAll( Passenger.class );
    }

    /**
     * Helper method to pass to the DomainObject class that performs a specific
     * query on the database.
     *
     * @param where
     *            List of Criterion to and together and search for records by
     * @return The list of all passengers found matching the Criterion provided
     */
    @SuppressWarnings ( "unchecked" )
    private static List<Passenger> getWhere ( final List<Criterion> where ) {
        return (List<Passenger>) getWhere( Passenger.class, where );
    }

    /**
     * Get a specific passenger by passengerId
     *
     * @param passengerId
     *            the passengerId of the passenger to get
     * @return the passenger with the queried passengerId
     */
    public static Passenger getByPassengerId ( final String passengerId ) {
        try {
            return getWhere( eqList( "passengerId", passengerId ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }
    
    
    /**
     * This method will count the number of passengers infected based on the date.
     * 
     * @return a map with each the key being each of the dates followed by an integer containing the
     * 			the frequency of the date in the database
     */
    @SuppressWarnings("deprecation")
	public static SortedMap<LocalDate, Integer> countByDate () {
    	List<Passenger> passengers = getPassengers();
    	
    	SortedMap<LocalDate, Integer> countByDate = new TreeMap<LocalDate, Integer>();
    	
    	for (Passenger passenger : passengers) {
    		Integer x = countByDate.get(passenger.getStartDateOfSymptoms());
    		if (x == null) {
    			x = new Integer(0);
    		}
			countByDate.put(passenger.getStartDateOfSymptoms(), new Integer(x.intValue() + 1));
		}
    	
//    	LocalDate firstDate = countByDate.firstKey();
//    	LocalDate lastDate = countByDate.lastKey();
//    	
//    	for (Passenger passenger : passengers) {
//			
//		}
    	return countByDate;
    }
    
    /**
     * This method will determine the R0 value for each day.
     * 
     * @return a map of each date as the key followed by the R0 for that date as the value
     */
    @SuppressWarnings("deprecation")
	public static SortedMap<LocalDate, Double> dailyRNaught() {
    	SortedMap<LocalDate, Integer> counts = Passenger.countByDate();
    	
    	SortedMap<LocalDate, Double> rCounts = new TreeMap<LocalDate, Double>();
    	
    	Integer peakCount = Integer.MIN_VALUE;
    	
//    	LocalDate peakDate = null;
    	
    	Integer prevCount = new Integer(0);
    	
    	LocalDate firstDate = counts.firstKey();
    	
    	for (Map.Entry<LocalDate, Integer> entry : counts.entrySet()) {
			if (firstDate.equals(entry.getKey())) {
				prevCount = entry.getValue();
				continue;
			}
			if (peakCount > entry.getValue()) {
				break;
			}
			
			
			Double rNaught = new Double(entry.getValue().intValue() / prevCount.intValue());
			
			rCounts.put(entry.getKey(), rNaught);
			
			prevCount = entry.getValue();
			
			peakCount = entry.getValue();
		}
    	
    	return rCounts;
    }
    
    
    /**
     * Calculates the average R0 value of the data in the database
     * 
     * @return the average R0 value
     */
    public static double averageRNaught() {
    	SortedMap<LocalDate, Double> rCounts = Passenger.dailyRNaught();
    	
    	Integer days = rCounts.size();
    	
    	double sum = 0;
    	
    	for (Map.Entry<LocalDate, Double> entry : rCounts.entrySet()) {
    		sum += entry.getValue().doubleValue();
    	}
    	
    	return sum / days;
    }
    
    
    /**
     * Empty constructor necessary for Hibernate.
     */
    public Passenger () {

    }
//    
//    /**
//     * Create a new Passenger based off of a user record
//     *
//     * @param self
//     *            the user record
//     */
//    public Passenger ( final User self ) {
//        setSelf( self );
//    }
//    
//    /**
//     * Create a new Passenger based of a user record (found via the username)
//     *
//     * @param self
//     *            the username
//     */
//    public Passenger ( final String self ) {
//        this( User.getByNameAndRole( self, Role.ROLE_PATIENT ) );
//    }

    // /**
    // * Create a new passenger based off of the PassengerForm
    // *
    // * @param form
    // * the filled-in passenger form with passenger information
    // * @throws ParseException
    // * if there is an issue in parsing the date
    // */
    // public Passenger ( final PassengerForm form ) {
    // setName( form.getName() );
    // setPassengerId( form.getPassengerId() );
    // setSeverityCode( SeverityCode.parse( form.getSeverityCode() ) );
    // final LocalDate startDate = LocalDate.parse(
    // form.getStartDateOfSymptoms() );
    // setStartDateOfSymptoms( startDate );
    // setId( form.getId() );
    //
    // }

    /**
     * The passenger id from csv file
     */
    @Id
    @Length ( max = 30 )
    private String       passengerId;

    /**
     * The name of passenger from csv file
     */
     @Length ( max = 100 )
     private String       name;

    /**
     * The severity code of the passenger from csv file
     */
    @Enumerated ( EnumType.STRING )
    private SeverityCode severityCode;

    /**
     * The start date of passenger symptoms from csv file
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate    startDateOfSymptoms;

    /**
     * Get the id of this passenger
     *
     * @return the id of this passenger
     */
    public String getPassengerId () {
        return passengerId;
    }

    /**
     * Set the id of this passenger
     *
     * @param pId
     *            the id to set this passenger to
     */
    public void setPassengerId ( final String pId ) {
        if ( pId == null || pId.length() > 30 || !pId.matches( "[a-zA-Z\\d' -]+" ) ) {
            throw new IllegalArgumentException( "id must contain 1-30 characters (alphanumeric, -, ', or space)" );
        }
        this.passengerId = pId;
    }

    /**
     * Get the name of this passenger
     *
     * @return the name of this passenger
     */
    public String getName() {
//        return getFirstName() + " " + getLastName();
    	return name;
    }

    /**
     * Set the name of this passenger
     *
     * @param name
     *            the name to set this passenger to
     */
    public void setName ( final String name ) {
        if ( name == null || name.length() > 100 ) {
            throw new IllegalArgumentException( "Name must contain 1-30 characters (alphanumeric, -, ', or space)" );
        }
        
        this.name = name;
//        final Scanner nameScanner = new Scanner(name);
//        
//        try {
//        	String first = nameScanner.next();
//        	String last = nameScanner.next();
//        	this.setFirstName(first);
//        	this.setLastName(last);
//        } catch (Exception e) {
//        	throw new IllegalArgumentException( "Name not valid." );
//        }
        
    }

    /**
     * Get the severity code of this passenger
     *
     * @return the severity code of this passenger
     */
    public SeverityCode getSeverityCode () {
        return severityCode;
    }

    /**
     * Set the severity code of this passenger
     *
     * @param severityCode
     *            the severity code to set this passenger to
     */
    public void setSeverityCode ( final SeverityCode severityCode ) {
        this.severityCode = severityCode;
    }

    /**
     * Get the date of birth of this patient
     *
     * @return the date of birth of this patient
     */
    public LocalDate getStartDateOfSymptoms () {
        return startDateOfSymptoms;
    }

    /**
     * Set the date of birth of this patient
     *
     * @param startDateOfSymptoms
     *            the start date of symptoms to set this patient to
     */
    public void setStartDateOfSymptoms ( final LocalDate startDateOfSymptoms ) {
        this.startDateOfSymptoms = startDateOfSymptoms;
    }

    /**
     * The id of this passenger
     */
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long id;

    /**
     * Set the id of this passenger
     *
     * @param id
     *            the id to set this passenger to
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the id of this passenger
     *
     * @return the id of this passenger
     */
    @Override
    public Long getId () {
        return this.id;
    }

    /**
     * Deletes all Passenger objects in the database.
     */
    public static void deleteAll () {
        DomainObject.deleteAll( Passenger.class );
    }

    @Override
    public boolean equals ( final Object other ) {
        if ( !( other instanceof Passenger ) ) {
            return false;
        }

        final Passenger otherPassenger = (Passenger) other;
        return this.getPassengerId().equals( otherPassenger.getPassengerId() );
    }

}
