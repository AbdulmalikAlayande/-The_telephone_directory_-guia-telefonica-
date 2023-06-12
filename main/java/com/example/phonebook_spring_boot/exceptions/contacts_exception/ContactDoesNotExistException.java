package com.example.phonebook_spring_boot.exceptions.contacts_exception;

public class ContactDoesNotExistException extends Throwable {
	public ContactDoesNotExistException(String message) {
		super(message);
	}
}
