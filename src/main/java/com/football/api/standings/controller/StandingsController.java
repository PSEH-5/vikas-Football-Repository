package com.football.api.standings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.football.api.standings.pojo.StandingsResponse;
import com.football.api.standings.service.StandingsService;
import com.football.api.standings.service.exceptions.StandingsException;

@RestController
public class StandingsController {
	
	@Autowired
	private StandingsService standingservice;
	
	
	@RequestMapping(value = "/football/standings/{countryName}/{leagueName}/{teamName}", method = RequestMethod.GET)
	public ResponseEntity<List<StandingsResponse>> getTeamStandings(@PathVariable(value = "countryName",required = true) String countryName , @PathVariable(value = "leagueName",required = true) String leagueName, @PathVariable(value = "teamName",required = true) String teamName) throws StandingsException
	{
		
		List<StandingsResponse> responselist=standingservice.getStandingsData(countryName, leagueName, teamName);
		
		return new ResponseEntity<List<StandingsResponse>>(responselist,HttpStatus.OK);
		}	
	
	

}
