package com.football.api.standings;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.football.api.standings.pojo.StandingsResponse;
import com.football.api.standings.service.StandingsService;
import com.football.api.standings.service.exceptions.StandingsException;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"management.port=0"})
public class FootballStandingsApplicationTests {
	
	@Autowired
    private MockMvc mvc;

	@LocalServerPort
	private int port;

	@Value("${local.management.port}")
	private int mgt;
	
	@Autowired
	private StandingsService standingService;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void shouldReturn200WhenSendingRequestToManagementEndpoint() throws Exception {
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
				"http://localhost:" + this.mgt + "/actuator/info", Map.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void getTeamStandings()
	{
//		{"country_id":"124","country_name":"Scotland","legaue_id":"423","league_name":"Premiership","team_id":"6580","team_name":"Celtic","overall_league_position":"1"}
		try {
			List<StandingsResponse>   response=standingService.getStandingsData("Scotland", "Premiership", "Celtic");
			assert(!response.isEmpty());
		}
		catch (Exception e) {
			assert(false);
		}
	}
	
	@Test
	public void getTeamStandings_InvalidCountryName() throws StandingsException
	{
		try {
			List<StandingsResponse>   response=standingService.getStandingsData("Scotland123", "Premiership", "Celtic");
			assert(response.isEmpty());
		}
		catch (Exception e) {
			assert(true);
		}

	}

}
