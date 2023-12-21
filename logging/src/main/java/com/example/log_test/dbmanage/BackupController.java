// 배치 파일을 이용한 백업 프로세스


package com.example.log_test.dbmanage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("database")
public class BackupController {

	private static final String EXCEPTION_OCCURRED = "Exception 발생 백업 실패 : ";

	@Value("${backup.script.path}")
	private String backupScriptPath;

	private final DBmanageLoggingSaver dbmanageLoggingSaver;
	public BackupController(DBmanageLoggingSaver dbmanageLoggingSaver, DBmanageLoggingRepository dbmanageloggingRepository) {
		this.dbmanageLoggingSaver = dbmanageLoggingSaver;
	}

	@PostMapping("/backup")
	public DBmanageResponseDTO runBackupBatchFile() {

		long startTime = System.currentTimeMillis();

		try {
//			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", backupScriptPath);

			// 배치 파일이 잘못되었을 경우 테스트
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "invalid_command_here");

			builder.redirectErrorStream(true);
			Process process = builder.start();

			StringBuilder outputLog = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				outputLog.append(line).append("\n");
			}

			int exitVal = process.waitFor();

			String backupFilePath = dbmanageLoggingSaver.extractBackupFilePath(outputLog.toString());
			Path filePath = Paths.get(backupFilePath);
			BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
			double elapsedTime = Duration.between(attrs.creationTime().toInstant(), attrs.lastModifiedTime().toInstant()).toMillis() / 1000.0;

			Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
//			System.out.println("authentication: " + authentication);
			String userId = authentication.getName();
//			System.out.println("userId: " + userId);
			dbmanageLoggingSaver.dbmanageLogger(
					"백업",
					new File(backupFilePath).getName(),
					elapsedTime,
					"OK",
					"",
					userId,
					null);

			return new DBmanageResponseDTO(backupFilePath, "백업 성공.", outputLog.toString());
		} catch (Exception e) {
			double elapsedTime = (double) (System.currentTimeMillis() - startTime) / 1000;

			Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
			String userId = authentication.getName();

			dbmanageLoggingSaver.dbmanageLogger(
					"백업",
					e.getClass().getSimpleName(),
					elapsedTime,
					"FAIL",
					e.getMessage(),
					userId,
					e);

			return new DBmanageResponseDTO("Exception is occured while executing batch", EXCEPTION_OCCURRED, e.getMessage());
		}
	}

	/*
//	@Async("taskExecutor")
	@Async
	@PostMapping("/backup")
	public CompletableFuture<DBmanageResponseDTO> runBatchFile() {
		SecurityContext context = SecurityContextHolder.getContext();

		return CompletableFuture.supplyAsync(() -> {
			long startTime = System.currentTimeMillis();
			SecurityContextHolder.setContext(context);
			try {

				// IOException 테스트
//				if (true) {throw new IOException("Simulated IOException for testing.");}

				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", backupScriptPath);

				// 배치 파일이 잘못되었을 경우 테스트
//				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "invalid_command_here");

				builder.redirectErrorStream(true);
				Process process = builder.start();

				StringBuilder outputLog = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;
				while ((line = reader.readLine()) != null) {
					outputLog.append(line).append("\n");
				}

				int exitVal = process.waitFor();

				String backupFilePath = dbmanageLoggingSaver.extractBackupFilePath(outputLog.toString());
				// 만들어진 파일의 생성 시간과 수정 시간을 비교하여 경과 시간을 구한다.
				Path filePath = Paths.get(backupFilePath);
				BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
				double elapsedTime = Duration.between(attrs.creationTime().toInstant(), attrs.lastModifiedTime().toInstant()).toMillis() / 1000.0;
				Authentication auth = context.getAuthentication();
				String userId = auth.getName();

				dbmanageLoggingSaver.dbmanageLogger(
						"백업",
						new File(backupFilePath).getName(),
						elapsedTime,
						"OK",
						"",
						userId,
						null);

				return new DBmanageResponseDTO(backupFilePath, "백업 성공.", outputLog.toString());
			}
			catch (IOException | InterruptedException e) {
				double elapsedTime = (double) (System.currentTimeMillis() - startTime) / 1000;
				Authentication auth = context.getAuthentication();
				String userId = auth.getName();

				dbmanageLoggingSaver.dbmanageLogger(
						"백업",
						e.getClass().getSimpleName(),
						elapsedTime,
						"FAIL",
						e.getMessage(),
						userId,
						e);

				return new DBmanageResponseDTO("Exception is occured while executing batch", EXCEPTION_OCCURRED, e.getMessage());
			}
			finally {
				SecurityContextHolder.clearContext();
			}
		});
	}
*/
}
