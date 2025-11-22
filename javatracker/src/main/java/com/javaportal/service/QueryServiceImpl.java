package com.javaportal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaportal.dto.QueryDTO;
import com.javaportal.entity.Query;
import com.javaportal.entity.QueryStatus;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.QueryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class QueryServiceImpl implements QueryService {
	
	@Autowired
	private QueryRepository queryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<QueryDTO> getPastTwoDaysQueries() throws JavaPortalException {
		LocalDateTime sentOn = LocalDateTime.now().minusDays(2); 
		List<Query> listOfQueries = queryRepo.findBySentOnGreaterThanEqual(sentOn);
		
		List<QueryDTO> listOfQueryDTOs = new ArrayList<>();
		
		for (Query q : listOfQueries) {
			listOfQueryDTOs.add(modelMapper.map(q, QueryDTO.class));
		}
		
		listOfQueryDTOs.sort((a, b) -> b.getSentOn().compareTo(a.getSentOn()));
		
		return listOfQueryDTOs;
	}

	@Override
	public boolean readAllQueries(Integer[] queryIds) throws JavaPortalException {
	    if (queryIds == null || queryIds.length == 0) {
	        throw new JavaPortalException("Service.NO_QUERY_IDS_PASSED");
	    }

	    List<Query> listOfQueries = queryRepo.findAllById(Arrays.asList(queryIds));

	    for (Query q : listOfQueries) {
	        q.setStatus(QueryStatus.READ);
	    }

	    queryRepo.saveAll(listOfQueries);

	    return true;
	}

	
	@Override
	public boolean sendQuery(QueryDTO dto) throws JavaPortalException {
		Query q = modelMapper.map(dto, Query.class);
		q.setSentOn(LocalDateTime.now());
		q.setStatus(QueryStatus.UNREAD);
		
		queryRepo.save(q);
		
		return true;
	}
	
	@Override
	public boolean markQueryAsRead(Integer queryId) throws JavaPortalException {
		Optional<Query> optQuery = queryRepo.findById(queryId);
		Query q = optQuery.orElseThrow(() -> new JavaPortalException("Service.QUERY_NOT_FOUND"));
		
		q.setStatus(QueryStatus.READ);
		
		queryRepo.save(q);
		
		return true;
	}
}
