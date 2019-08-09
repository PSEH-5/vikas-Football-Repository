package com.football.api.standings.service;

import java.util.List;

import com.football.api.standings.pojo.StandingsResponse;
import com.football.api.standings.service.exceptions.StandingsException;

public interface StandingsService {
	String getCountryId(String countryName) throws StandingsException;

	String getLeagueId(String leagueName) throws StandingsException;

	List<StandingsResponse> getStandingsData(String countryName, String leagueName, String teamName)
			throws StandingsException;
}
