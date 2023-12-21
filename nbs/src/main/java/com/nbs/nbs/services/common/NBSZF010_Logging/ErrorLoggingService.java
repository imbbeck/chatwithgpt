package com.nbs.nbs.services.common.NBSZF010_Logging;

import java.time.Instant;

import com.nbs.nbs.entity.logging.ErrorLoggingEntity;
import com.nbs.nbs.entity.logging.ErrorLoggingRepository;

public class ErrorLoggingService {
	static void errorlogToDB(String menuURL, String userId, String statusCode, String errorCode, String errorName, String errorMsg, String createdBy, Instant createdAt, ErrorLoggingRepository errorLoggingRepository) {
		ErrorLoggingEntity errorLoggingEntity = new ErrorLoggingEntity();
		errorLoggingEntity.setMenuId(menuURL);
		errorLoggingEntity.setUserId(userId);
		errorLoggingEntity.setStatusCode(statusCode);
		errorLoggingEntity.setErrorCode(errorCode);
		errorLoggingEntity.setErrorName(errorName);
		errorLoggingEntity.setErrorMsg(errorMsg);
		errorLoggingEntity.setCreatedBy(createdBy);
		errorLoggingEntity.setCreatedAt(createdAt);

		errorLoggingRepository.save(errorLoggingEntity);
	}
}