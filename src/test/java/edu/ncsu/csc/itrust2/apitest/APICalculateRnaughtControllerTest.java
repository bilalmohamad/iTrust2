/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.models.enums.SeverityCode;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test for the API functionality for calculate R_0 value.
 *
 * @author Tanvi Thummar
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APICalculateRnaughtControllerTest {

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
     * Tests calculating R_0 value without file upload, without any existing
     * data.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testBadRequest () throws Exception {

        // Clear out all passengers before running these tests.
        Passenger.deleteAll();

        mvc.perform( get( "/api/v1/calculateRnaught" ) ).andExpect( status().isBadRequest() );

    }

    /**
     * Tests calculating R_0 value without file upload, with existing data in
     * the database.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testValidRequestNoFile () throws Exception {

        // Ensure that there is data in the database.
        // Add a bunch of passengers in the database.

        final Passenger p1 = new Passenger();
        p1.setPassengerId( "3b9aca00" );
        p1.setName( "abc123 abc4567" );
        p1.setSeverityCode( SeverityCode.M );
        p1.setStartDateOfSymptoms( LocalDate.now() );
        p1.save();

        final Passenger p2 = new Passenger();
        p2.setPassengerId( "3b9aca76" );
        p2.setName( "abc123 abc4567890" );
        p2.setSeverityCode( SeverityCode.C );
        p2.setStartDateOfSymptoms( LocalDate.parse( "2020-04-01" ) );
        p2.save();

        final Passenger p3 = new Passenger();
        p3.setPassengerId( "3b9acaec" );
        p3.setName( "abc1 abc45" );
        p3.setSeverityCode( SeverityCode.C );
        p3.setStartDateOfSymptoms( LocalDate.parse( "2020-04-01" ) );
        p3.save();

        mvc.perform( get( "/api/v1/calculateRnaught" ) ).andExpect( status().isOk() );

    }

    // /**
    // * Tests calculating R_0 value with a file upload from user.
    // *
    // * @throws Exception
    // */
    // @Test
    // @WithMockUser ( username = "hcp", roles = { "HCP" } )
    // public void testValidFile () throws Exception {
    //
    // }
}
