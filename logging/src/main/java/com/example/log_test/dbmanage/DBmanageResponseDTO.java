package com.example.log_test.dbmanage;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBmanageResponseDTO {

	private String filePath;
	private String msg;
	private String details;

}
