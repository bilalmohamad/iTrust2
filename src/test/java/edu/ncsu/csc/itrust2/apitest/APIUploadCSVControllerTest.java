/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test for the API functionality for uploading the CSV file.
 *
 * @author Tanvi Thummar
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIUploadCSVControllerTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up tests
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests invalid format file scenario.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testInvalidFormatFile () throws Exception {

        // Clear out all passengers before running these tests.
        Passenger.deleteAll();

        final String invalidFileString = "3b9aca00, \"Reuchlin, Jonkheer, J.G.\", M, 2020/02/16 21:26:34, 12345\r\n"
                + "3b9aca76, \"Drachstedt, Baron von\", M, 2020/02/04 13:18:03, adbsjf\r\n"
                + "3b9acaec, \"Foreman, B. L.\", S, 2020/02/04 20:13:22\r\n"
                + "3b9acb62, \"Mirko, Dika\", M, 2020/02/05 14:01:16\r\n"
                + "3b9acbd8, \"Kassem, Fared\", M, 2020/02/11 18:05:56";

        mvc.perform(
                post( "/api/v1/passengerCSV" ).contentType( MediaType.APPLICATION_JSON ).content( invalidFileString ) )
                .andExpect( status().isBadRequest() );

    }

    /**
     * Tests valid file scenario. Also tests all existing passengers scenario.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testValidFile () throws Exception {

        // Clear out all passengers before running these tests.
        Passenger.deleteAll();

        final String validFileString = convertFileToString( "passenger-data-short.csv" );

        mvc.perform(
                post( "/api/v1/passengerCSV" ).contentType( MediaType.APPLICATION_JSON ).content( validFileString ) )
                .andExpect( status().isOk() );

        // Now let the passengers be in the database purposefully.
        // This is a bad request as all passengers already exist in the
        // database.
        mvc.perform(
                post( "/api/v1/passengerCSV" ).contentType( MediaType.APPLICATION_JSON ).content( validFileString ) )
                .andExpect( status().isBadRequest() );

    }

    /**
     * Helper method to convert a file to string.
     *
     * @param filename
     *            File name
     * @return String of the file
     */
    private String convertFileToString ( final String filename ) {

        Scanner s = null;
        try {
            s = new Scanner( new File( filename ) );
        }
        catch ( final FileNotFoundException e ) {
            e.printStackTrace();
        }

        String validFileString = "";

        while ( s.hasNextLine() ) {
            validFileString = validFileString + s.nextLine() + "\n";
        }

        return validFileString;
    }

}
