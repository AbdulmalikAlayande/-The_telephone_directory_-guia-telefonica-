package com.example.phonebook_spring_boot.exceptions;

import java.sql.SQLException;

public class StatementPreparationFailedException extends Throwable {
	public StatementPreparationFailedException(String message) {
		super(message);
	}
}
