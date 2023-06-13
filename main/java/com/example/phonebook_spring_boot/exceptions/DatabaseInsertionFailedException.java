package com.example.phonebook_spring_boot.exceptions;

public class DatabaseInsertionFailedException extends Throwable {
	public DatabaseInsertionFailedException(String message) {
		super(message);
	}
}
