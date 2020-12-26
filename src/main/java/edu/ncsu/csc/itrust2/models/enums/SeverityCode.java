package edu.ncsu.csc.itrust2.models.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum of the severity codes.
 *
 * @author Marwah Mahate msmahate
 *
 */
public enum SeverityCode {
	/**
     * mild
     */
    M ( "mild" ),
    /**
     * severe
     */
    S ( "severe" ),
    /**
     * critical
     */
    C ( "critical" ),
    /**
     * not infected
     */
    N ( "not infected" );
	
    /**
     * Name of the severity code
     */
    private String name;

    /**
     * Create a SeverityCode from its Name
     *
     * @param name
     *            Name of the SeverityCode
     */
    private SeverityCode ( final String name ) {
        this.name = name;
    }

    /**
     * Gets the Name of this SeverityCode
     *
     * @return Name of the SeverityCode
     */
    public String getName () {
        return name;
    }

    /**
     * Returns a map from field name to value, which is more easily serialized
     * for sending to front-end.
     *
     * @return map from field name to value for each of the fields in this enum
     */
    public Map<String, Object> getInfo () {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", name() );
        map.put( "name", getName() );
        return map;
    }


    /**
     * Finds a SeverityCode enum from the string provided.
     *
     * @param sev Name of the SeverityCode to find
     * @return The SeverityCode enum, or not infected.
     */
    public static SeverityCode parse ( final String sev ) {
        for ( final SeverityCode mySev : values() ) {
            if ( mySev.getName().equals( sev.toLowerCase() ) ) {
                return mySev;
            }
        }
        return SeverityCode.N;
    }
}
