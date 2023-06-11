package com.example.phonebook_spring_boot.exceptions;

public class PhonebookDoesNotExistException extends Exception{
	public PhonebookDoesNotExistException(String message){
		super(message);
	}
}
