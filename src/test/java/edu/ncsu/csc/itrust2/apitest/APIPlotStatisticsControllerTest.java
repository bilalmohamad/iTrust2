/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
 * Test for the API functionality for plotting the statistics of the passengers
 * on Aquatranquilica. Tests three REST API end points.
 *
 * @author Tanvi Thummar
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIPlotStatisticsControllerTest {

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
     * Tests the first REST API end point which returns data for plotting the
     * number of infected patients per day.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testInfectionsPerDay () throws Exception {

        if ( Passenger.getPassengers().size() > 0 ) {

            mvc.perform( get( "/api/v1/plotStatistics/infectedPatientsPerDay" ) ).andExpect( status().isOk() )
                    .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        }

    }

    /**
     * Tests the first REST API end point which returns data for plotting the
     * number of new infections per day.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testNewInfectionsPerDay () throws Exception {

        if ( Passenger.getPassengers().size() > 0 ) {

            mvc.perform( get( "/api/v1/plotStatistics/newInfectionsPerDay" ) ).andExpect( status().isOk() )
                    .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        }

    }

    /**
     * Tests the first REST API end point which returns data for plotting the
     * number of patients by severity.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testPatientsBySeverity () throws Exception {

        if ( Passenger.getPassengers().size() > 0 ) {

            mvc.perform( get( "/api/v1/plotStatistics/patientsBySeverity" ) ).andExpect( status().isOk() )
                    .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        }

    }

}
