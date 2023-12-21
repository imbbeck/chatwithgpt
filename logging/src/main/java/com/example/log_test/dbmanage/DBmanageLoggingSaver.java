package com.example.log_test.dbmanage;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DBmanageLoggingSaver {
	private final DBmanageLoggingRepository dbmanageloggingRepository;

	public DBmanageLoggingSaver(DBmanageLoggingRepository dbmanageloggingRepository) {
		this.dbmanageloggingRepository = dbmanageloggingRepository;
	}

	public void dbmanageLogger(String processType, String fileName, double elapsedTime, String response, String responseDetail, String createdBy, Exception e) {
		DBmanageLoggingEntity logEntity = new DBmanageLoggingEntity();

		logEntity.setProcessType(processType);
		logEntity.setFileName(fileName);
		logEntity.setElapsedTime(elapsedTime); // Convert to seconds
		logEntity.setResponse(response);
		logEntity.setResponseDetail(responseDetail);
		logEntity.setCreatedBy(createdBy);
		logEntity.setCreatedAt(Instant.now());

		dbmanageloggingRepository.save(logEntity);
	}

	private String getCurrentUsername() {
		return SecurityContextHolder.getContext().getAuthentication() != null ?
				SecurityContextHolder.getContext().getAuthentication().getName() :
				"GUEST";
	}

	String extractBackupFilePath(String outputLog) {
		Pattern pattern = Pattern.compile("BACKUP_FILE=(.*?)\\s"); // 정규 표현식
		Matcher matcher = pattern.matcher(outputLog);
		if (matcher.find()) {
			return matcher.group(1); // 첫 번째 그룹이 파일 경로
		}
		return "Path not found";
	}
}
