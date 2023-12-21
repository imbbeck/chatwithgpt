package com.example.log_test.dbmanage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class RestoreController {

	private static final String EXCEPTION_OCCURRED = "Exception 발생 백업 실패 : ";

	@Value("${spring.datasource.url}")
	private String databaseUrl;
	@Value("${spring.datasource.username}")
	private String databaseUsername;
	@Value("${spring.datasource.password}")
	private String databasePassword;
	@Value("${backup.default.path}")
	private String defaultBackupFolderPath;
	@Value("${mysql.install.path}")
	private String mysqlInstallPath;
	@Value("${bems.datasource.name}")
	private String bemsDbName;
	@Value("${jace.datasource.name}")
	private String jaceDbName;

	private final DBmanageLoggingSaver dbmanageLoggingSaver;
	public RestoreController(DBmanageLoggingSaver dbmanageLoggingSaver, DBmanageLoggingRepository dbmanageloggingRepository) {
		this.dbmanageLoggingSaver = dbmanageLoggingSaver;
	}

//	@Async("taskExecutor")
	@Async
	@GetMapping("/restore")
	public CompletableFuture<DBmanageResponseDTO> restoreDatabase(@RequestParam String fileName) {

		SecurityContext context = SecurityContextHolder.getContext();

		return CompletableFuture.supplyAsync(() -> {
			SecurityContextHolder.setContext(context);
			long startTime = System.currentTimeMillis();
			try {
				// IOException 테스트
//			    if (true) {throw new IOException("Simulated IOException for testing.");}

				File backupFile = new File(defaultBackupFolderPath, fileName);
				if (!backupFile.exists()) {
					throw new NoSuchFileException(backupFile.getAbsolutePath());
				}

				Process process = getProcess(fileName);

				// Capture and log the standard output
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

				String s;
				StringBuilder outputLog = new StringBuilder();
				while ((s = stdInput.readLine()) != null) {
					outputLog.append(s).append("\n");
				}
				while ((s = stdError.readLine()) != null) {
					outputLog.append(s).append("\n");
				}

				int exitCode = process.waitFor();

				double elapsedTime = (double) (System.currentTimeMillis() - startTime) / 1000;
				Authentication auth = context.getAuthentication();
				String userId = auth.getName();

				dbmanageLoggingSaver.dbmanageLogger(
						"복원",
						fileName,
						elapsedTime,
						"OK",
						"",
						userId,
						null);

				return new DBmanageResponseDTO(defaultBackupFolderPath + fileName, "복원 성공.", outputLog.toString());
			}
			catch (NoSuchFileException e) {
				double elapsedTime = (double) (System.currentTimeMillis() - startTime) / 1000;
				Authentication auth = context.getAuthentication();
				String userId = auth.getName();

				dbmanageLoggingSaver.dbmanageLogger(
						"복원",
						e.getClass().getSimpleName(),
						elapsedTime,
						"FAIL",
						e.getMessage() + " is not found.",
						userId,
						e);

				return new DBmanageResponseDTO(defaultBackupFolderPath + fileName, "백업 파일이 존재하지 않습니다.", e.getMessage());
			}
			catch (IOException | InterruptedException e) {
				double elapsedTime = (double) (System.currentTimeMillis() - startTime) / 1000;
				Authentication auth = context.getAuthentication();
				String userId = auth.getName();

				dbmanageLoggingSaver.dbmanageLogger(
						"복원",
						e.getClass().getSimpleName(),
						elapsedTime,
						"FAIL",
						e.getMessage(),
						userId,
						e);

				return new DBmanageResponseDTO(defaultBackupFolderPath + fileName, EXCEPTION_OCCURRED, e.getMessage());
			}
			finally {
				SecurityContextHolder.clearContext();
			}
		});
	}

	private Process getProcess(String fileName) throws IOException {
		String mysqlExcutionPath = mysqlInstallPath + File.separator + "mysql";
		// MySQL 복원 명령 실행
		List<String> command = new ArrayList<>();
		command.add(mysqlExcutionPath); // MySQL 실행 경로
		command.add("-u" + databaseUsername); // MySQL username
		command.add("-p" + "\"" + databasePassword + "\""); // MySQL Password
		command.add("-v"); // 설명모드
		command.add("-e");
		command.add("source " + "\"" + defaultBackupFolderPath + fileName + "\""); // SQL 파일

		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		return builder.start();
	}

	@GetMapping("/restore-default")
	public CompletableFuture<DBmanageResponseDTO> testRestoreDatabase() {
		String testFileName = "NBS_231218_130236.sql";
		return restoreDatabase(testFileName);
	}
}


