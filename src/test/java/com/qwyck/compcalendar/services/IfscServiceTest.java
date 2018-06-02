package com.qwyck.compcalendar.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import java.nio.file.Files;
import com.qwyck.compcalendar.templates.Competitions;
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
    }
}
