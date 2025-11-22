package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.QueryDTO;
import com.javaportal.exception.JavaPortalException;

public interface QueryService {
	public List<QueryDTO> getPastTwoDaysQueries() throws JavaPortalException;
	public boolean readAllQueries(Integer[] queryIds) throws JavaPortalException;
	public boolean sendQuery(QueryDTO dto) throws JavaPortalException;
	public boolean markQueryAsRead(Integer queryId) throws JavaPortalException;
}
