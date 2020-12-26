package edu.ncsu.csc.itrust2.controllers.api;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.utils.ConvertCSVUtil;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import edu.ncsu.csc.itrust2.utils.PassengerToPatientUtil;

/**
 * Class that provides REST API end-points for the Upload CSV File
 * functionality. Uses the file in a CSV format to find create new passengers in
 * the database.
 *
 * @author Tanvi Thummar (tdthumma)
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APIUploadCSVController extends APIController {

    /**
     * Returns the number of new passengers created in the database, basically
     * number of passengers whose data is uploaded successfully.
     *
     * @param fileString
     *            String form of the CSV file
     * @return Number of new passengers created in the database.
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    @PostMapping ( BASE_PATH + "/passengerCSV" )
    public ResponseEntity uploadCSV ( @RequestBody final String fileString ) {

        // Keeps a track of number of new passengers created.
        int passengerCount = 0;

        // Keeps a track of the number of duplicate entries, so the skipped
        // passengers.
        int skippedPassengers = 0;

        // Try to open a file at the given path,
        // and convert it to a list of passengers.
        ArrayList<Passenger> passengerList = null;
        try {
            passengerList = ConvertCSVUtil.convertCSV( fileString );
        }
        catch ( final Exception e ) {
            // If the format is wrong then just send a failure message to user.
            return new ResponseEntity( errorResponse( "Invalid format of the csv file." ), HttpStatus.BAD_REQUEST );
        }

        // Process the list of passengers to see how many passengers exist,
        // save new passengers in the database.
        final int listSize = passengerList.size();
        for ( int i = 0; i < listSize; i++ ) {

            // Take the passenger at this position and see if it exists.
            // if it does then move one, and if it does not then save it in the
            // database.
            final Passenger newPassenger = passengerList.get( i );

            final Passenger passengerExist = Passenger.getByPassengerId( newPassenger.getPassengerId() );

            if ( passengerExist == null ) {
                newPassenger.save();
                PassengerToPatientUtil.passengerToPatient( newPassenger );
                passengerCount++;
            }
            else {
                skippedPassengers++;
            }

        }

        if ( passengerCount > 0 ) {
            // Log the event.
            LoggerUtil.log( TransactionType.UPLOAD_PASSENGER_DATA, LoggerUtil.currentUser(),
                    "HCP uploaded a list" + passengerCount + "of passengers." );

            // Return a success response if one or more new passengers are
            // created,
            // return the number of passengers generated in the database.
            return new ResponseEntity(
                    successResponse( "Number of new passenger entries made: " + passengerCount + "\n"
                            + "Number of passenger entries skipped due to duplication: " + skippedPassengers ),
                    HttpStatus.OK );
        }

        else {
            // Return a failure response no new passengers were created,
            // as the data already existed.
            return new ResponseEntity( errorResponse( "All the passengers in the given list already exist." ),
                    HttpStatus.BAD_REQUEST );
        }

    }

}
