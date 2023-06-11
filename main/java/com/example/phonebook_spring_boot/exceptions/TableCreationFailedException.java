package com.example.phonebook_spring_boot.exceptions;

public class TableCreationFailedException extends Exception{
	public TableCreationFailedException(String message){
		super(message);
	}
}
