package com.example.log_test.dbmanage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAsync
@RestController
@RequestMapping("database")
public class DumpedDBListController {

	@Value("${backup.default.path}")
	private String defaultBackupFolderPath;

	@Async
	@GetMapping("/list")
	public CompletableFuture<List<DumpedDBFileInfoGetter>> listDatabaseBackups(
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String backupFolderPath) throws IOException {

		LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusWeeks(1);
		LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

		List<DumpedDBFileInfoGetter> dumpedDBList = new ArrayList<>();
		if (backupFolderPath == null || backupFolderPath.isEmpty()) {
			backupFolderPath = defaultBackupFolderPath;
		}

		File folder = new File(backupFolderPath);
		FilenameFilter sqlFilter = (dir, name) -> name.toLowerCase().endsWith(".sql");
		File[] listOfFiles = folder.listFiles(sqlFilter);

		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					DumpedDBFileInfoGetter fileInfo = new DumpedDBFileInfoGetter();
					fileInfo.setFileName(file.getName());
					fileInfo.setFileSize(file.length());
					fileInfo.setCreationTimeFromFile(file);

					if (isFileInDateRange(file, start, end)) {
						fileInfo.setCreationDuration(backupFolderPath, file.getName());
						dumpedDBList.add(fileInfo);
					}
				}
			}
		}

		return CompletableFuture.completedFuture(dumpedDBList);
	}

	private boolean isFileInDateRange(File file, LocalDate start, LocalDate end) throws IOException {
		Path filePath = Paths.get(file.getAbsolutePath());
		BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
		LocalDate fileDate = attrs.creationTime()
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
		return (fileDate.isEqual(start) || fileDate.isAfter(start)) &&
				(fileDate.isEqual(end) || fileDate.isBefore(end));
	}
}
