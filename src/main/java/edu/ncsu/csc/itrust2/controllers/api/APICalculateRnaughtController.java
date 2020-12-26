package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides REST API endpoints for the Calculate R_0 functionality.
 * There are two different features, as user can choose to provide new data, or
 * the user can calculate the value from the avaialable data.
 *
 * @author Tanvi Thummar (tdthumma)
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APICalculateRnaughtController extends APIController {

    /**
     * Returns the calculated R_0 value for the existing data.
     *
     * @return Calculated R_0 value.
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    @GetMapping ( BASE_PATH + "/calculateRnaught" )
    public ResponseEntity calcRnaught () {

        final List<Passenger> passengerList = Passenger.getPassengers();

        if ( passengerList.size() > 0 ) {

            final double rNaught = Passenger.averageRNaught();

            // Log the event.
            LoggerUtil.log( TransactionType.CALCULATE_R_NAUGHT, LoggerUtil.currentUser(),
                    "HCP calculated R-naught value." );

            // Return a success message based on the R_0 value calculated.
            String message = "Calculated R_0 value: " + rNaught + "\n";

            if ( rNaught > 1 ) {
                message = message + "Inference: COVID-19 is Spreading.";
            }
            else if ( rNaught < 1 ) {
                message = message + "Inference: COVID-19 Dies Out.";
            }
            else {
                message = message + "Inference: COVID-19 is Stable";
            }

            return new ResponseEntity( successResponse( message ), HttpStatus.OK );
        }

        else {
            // Return a failure response if there are no passengers in the
            // database.
            return new ResponseEntity( errorResponse( "There are no passengers in the database." ),
                    HttpStatus.BAD_REQUEST );
        }

    }

}
