package com.example.log_test.dbmanage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class DumpedDBFileInfoGetter {
	@Getter @Setter
	private String fileName;

	@Setter
	private long fileSize;

	@Getter @Setter
	private String creationTime;

	@Getter
	private String creationDuration;

	public void setCreationTimeFromFile(File file) {
		Path filePath = Paths.get(file.getAbsolutePath());
		this.creationTime = getFormattedCreationTime(filePath);
	}

	public String getFileSize() {
		return formatFileSize(fileSize);
	}

	public void setCreationDuration(String backupFolderPath, String fileName) {
		Path path = Paths.get(backupFolderPath, fileName);
		this.creationDuration = calculateCreationDuration(path);
	}

	private String getFormattedCreationTime(Path filePath) {
		try {
			BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
			return formatInstant(attrs.creationTime().toInstant());
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}

	private String calculateCreationDuration(Path path) {
		try {
			BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
			Duration duration = Duration.between(attr.creationTime().toInstant(), attr.lastModifiedTime().toInstant());
			return formatDuration(duration);
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}

	private String formatInstant(Instant instant) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss.SS");
		return sdf.format(Date.from(instant));
	}

	private String formatFileSize(long size) {
		if (size < 1024) {
			return size + " B";
		} else if (size < Math.pow(1024, 2)) {
			return String.format("%.1f KB", size / 1024.0);
		} else if (size < Math.pow(1024, 3)) {
			return String.format("%.1f MB", size / Math.pow(1024, 2));
		} else {
			return String.format("%.1f GB", size / Math.pow(1024, 3));
		}
	}

	private String formatDuration(Duration duration) {
		double millis = duration.toMillis();
		return String.format("%.2f sec", millis / 1000.0);
	}
}