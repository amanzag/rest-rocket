package es.amanzag.restrocket.service;

import es.amanzag.restrocket.dto.Target;
import es.amanzag.restrocket.dto.TargetList;
import es.amanzag.restrocket.service.exception.RocketException;

public interface RocketPersistenceService {
	Target getTarget(String targetId) throws RocketException;
	
	void persistTarget(Target target) throws RocketException;
	
	TargetList getTargets() throws RocketException;
	
	void deleteTarget(String targetId) throws RocketException;
}
