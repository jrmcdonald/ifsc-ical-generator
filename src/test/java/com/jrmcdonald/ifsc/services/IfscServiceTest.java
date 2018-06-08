package com.jrmcdonald.ifsc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import com.jrmcdonald.ifsc.templates.Competition;
import com.jrmcdonald.ifsc.templates.Competitions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

@RunWith(SpringRunner.class)
@RestClientTest(IfscService.class)
public class IfscServiceTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private IfscService service;

    @Value("classpath:find_all_single.json")
    private Resource FIND_ALL_SINGLE_JSON;

    @Value("classpath:find_multiple.json")
    private Resource FIND_MULTIPLE_JSON;

    private static final String IFSC_RESOURCE_URL =
            "http://egw.ifsc-climbing.org/egw/ranking/json.php";

    @Test
    public void testFindAll() throws Exception {
        String response = new String(Files.readAllBytes((FIND_ALL_SINGLE_JSON.getFile()).toPath()));

        server.expect(once(), requestTo(IFSC_RESOURCE_URL))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Competitions competitions = service.findAll();

        assertNotNull(competitions.getCompetitions());
        assertTrue(competitions.getCompetitions().size() == 1);

        Competition competition = competitions.getCompetitions().get(0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");

        assertEquals(competition.getName(), "Promotional Event Gravical (B) - Singapore (SGP) 2018");
        assertEquals(competition.getCat(), "70");
        assertEquals(competition.getHomepage(), null);
        assertEquals(competition.getStartDate(), sdf.parse("2018-01-11"));
        assertEquals(competition.getEndDate(), sdf.parse("2018-01-14"));
    }

    @Test
    public void testFindAll_Multiple() throws Exception {
        String response = new String(Files.readAllBytes((FIND_MULTIPLE_JSON.getFile()).toPath()));

        server.expect(once(), requestTo(IFSC_RESOURCE_URL))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Competitions competitions = service.findAll();

        assertNotNull(competitions.getCompetitions());
        assertTrue(competitions.getCompetitions().size() == 3);
    }

    @Test
    public void testFindByCategory() throws Exception {
        String response = new String(Files.readAllBytes((FIND_MULTIPLE_JSON.getFile()).toPath()));

        server.expect(once(), requestTo(IFSC_RESOURCE_URL))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Competitions competitions = service.findByCategory(Arrays.asList("70"));

        assertNotNull(competitions.getCompetitions());
        assertTrue(competitions.getCompetitions().size() == 2);
    }
}
