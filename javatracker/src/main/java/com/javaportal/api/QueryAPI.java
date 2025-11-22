package com.javaportal.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.dto.QueryDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.QueryService;

@CrossOrigin
@RestController
@RequestMapping(value="api/query")
public class QueryAPI {
	
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private Environment env;
	
	@GetMapping(value="getQueries")
	public ResponseEntity<List<QueryDTO>> getPastTwoDaysQueries() throws JavaPortalException {
		List<QueryDTO> listOfQueries = queryService.getPastTwoDaysQueries();
		
		return new ResponseEntity<>(listOfQueries, HttpStatus.OK);
	}
	
	@PostMapping(value="sendQuery")
	public ResponseEntity<String> sendQuery(@RequestBody QueryDTO dto) throws JavaPortalException {
		String message = "";
		try {
			queryService.sendQuery(dto);
			message = env.getProperty("Api.QUERY_REGISTERED");
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value="readAllQueries")
	public ResponseEntity<String> readAllQueries(@RequestBody Integer[] ids) throws JavaPortalException {
		String message = "";
		try {
			queryService.readAllQueries(ids);
			message = env.getProperty("Api.READ_QUERIES_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value="readQuery/{queryId}")
	public ResponseEntity<String> readQuery(@PathVariable Integer queryId) throws JavaPortalException {
		String message = "";
		try {
			queryService.markQueryAsRead(queryId);
			message = env.getProperty("Api.MARK_QUERY_AS_READ");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
