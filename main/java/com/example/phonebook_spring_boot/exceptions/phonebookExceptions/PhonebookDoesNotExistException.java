package com.example.phonebook_spring_boot.exceptions.phonebookExceptions;

public class PhonebookDoesNotExistException extends Exception{
	public PhonebookDoesNotExistException(String message){
		super(message);
	}
}
