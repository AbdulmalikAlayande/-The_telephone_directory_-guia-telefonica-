package com.example.phonebook_spring_boot.exceptions;

public class DatabaseConnectionFailedException extends Exception{
	public DatabaseConnectionFailedException(String message){
		super(message);
	}
}
