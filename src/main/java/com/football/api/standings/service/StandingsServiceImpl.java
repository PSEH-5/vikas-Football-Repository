package com.football.api.standings.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.football.api.standings.config.StandingsConfiguration;
import com.football.api.standings.pojo.Country;
import com.football.api.standings.pojo.League;
import com.football.api.standings.pojo.StandingsResponse;
import com.football.api.standings.service.exceptions.StandingsException;

@Component
public class StandingsServiceImpl implements StandingsService {

	@Autowired
	StandingsConfiguration config;

	@Override
	public String getCountryId(String countryName) throws StandingsException {
		// TODO Auto-generated method stub
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<Country[]> responsecountryList = resttemplate.getForEntity(config.getGetCountriesUrl(),
				Country[].class);
		String country_Id = "";

		for (Country country : responsecountryList.getBody()) {
			if (countryName.equalsIgnoreCase(country.getCountry_name())) {
				country_Id = country.getCountry_id();
			}

		}

		if (StringUtils.isEmpty(country_Id)) {
			throw new StandingsException("Country does not exist in league.");
		}
		return country_Id;

	}

	@Override
	public String getLeagueId(String country_Id) throws StandingsException {

		RestTemplate resttemplate = new RestTemplate();
		String league_Id = "";

		String leagueUrl = UriComponentsBuilder.fromUriString(config.getGetleaguesurl())
				.replaceQueryParam("country_id", country_Id).toUriString();

		ResponseEntity<League[]> responseLeagueList = resttemplate.getForEntity(leagueUrl, League[].class);
		for (League league : responseLeagueList.getBody()) {
			league_Id = league.getLeague_id();
		}
		
		if (StringUtils.isEmpty(league_Id)) {
			throw new StandingsException("League does not exist.");
		}
		
		return league_Id;
	}


	@Override
	public List<StandingsResponse> getStandingsData(String countryName, String leagueName, String teamName)
			throws StandingsException {
		// TODO Auto-generated method stub
		RestTemplate resttemplate = new RestTemplate();
		
		String country_Id=getCountryId(countryName);
		String league_Id=getLeagueId(country_Id);

		
		// get standings
		String standingUrl = UriComponentsBuilder.fromUriString(config.getGetstandingsurl())
				.replaceQueryParam("league_id", league_Id).toUriString();

		ResponseEntity<StandingsResponse[]> standingresponse = resttemplate.getForEntity(standingUrl,
				StandingsResponse[].class);

		List<StandingsResponse> list = new ArrayList<>();
		StandingsResponse leagueResponse = null;

		for (StandingsResponse standings : standingresponse.getBody()) {
			if (standings.getLeague_name().equalsIgnoreCase(leagueName)
					&& standings.getCountry_name().equalsIgnoreCase(countryName)
					&& standings.getTeam_name().equalsIgnoreCase(teamName)) {
				leagueResponse = new StandingsResponse();
				leagueResponse.setCountry_id(country_Id);
				leagueResponse.setCountry_name(standings.getCountry_name());
				leagueResponse.setLeague_name(standings.getLeague_name());
				leagueResponse.setLegaue_id(league_Id);
				leagueResponse.setTeam_id(standings.getTeam_id());
				leagueResponse.setTeam_name(standings.getTeam_name());
				leagueResponse.setOverall_league_position(standings.getOverall_league_position());
				list.add(leagueResponse);
			}

		}
		return list;
	}

}
