package com.example.phonebook_spring_boot.data.repositories;

import com.example.phonebook_spring_boot.data.models.Contact;
import com.example.phonebook_spring_boot.exceptions.DatabaseConnectionFailedException;
import com.example.phonebook_spring_boot.exceptions.DatabaseInsertionFailedException;
import com.example.phonebook_spring_boot.exceptions.StatementPreparationFailedException;
import com.example.phonebook_spring_boot.exceptions.contacts_exception.ContactDoesNotExistException;
import com.example.phonebook_spring_boot.exceptions.phonebookExceptions.TableCreationFailedException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ContactsRepository {
	
	Optional<Contact> saveContact(Contact contact) throws DatabaseConnectionFailedException, TableCreationFailedException, DatabaseInsertionFailedException, ContactDoesNotExistException;
	Optional<Contact> findContactById(int contactId) throws DatabaseConnectionFailedException, StatementPreparationFailedException;
	void deleteContactById(int contactId) throws DatabaseConnectionFailedException, ContactDoesNotExistException;
	Set<Optional<Contact>> getAllContactRelatingToPhonebookWithThePhonebookId(int id);
	Map<String, Optional<Contact>> getAllContactsInTheDatabase() throws DatabaseConnectionFailedException, ContactDoesNotExistException;
	void clearDatabase();
	void dropDatabase();
}
