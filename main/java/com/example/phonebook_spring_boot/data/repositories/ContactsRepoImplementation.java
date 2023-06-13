package com.example.phonebook_spring_boot.data.repositories;

import com.example.phonebook_spring_boot.data.models.Contact;
import com.example.phonebook_spring_boot.exceptions.DatabaseConnectionFailedException;
import com.example.phonebook_spring_boot.exceptions.DatabaseInsertionFailedException;
import com.example.phonebook_spring_boot.exceptions.contacts_exception.ContactDoesNotExistException;
import com.example.phonebook_spring_boot.exceptions.phonebookExceptions.TableCreationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Repository
@Slf4j
public class ContactsRepoImplementation implements ContactsRepository{
	String url = "jdbc:mysql://localhost/contact_db?createDatabaseIfNotExist=true";
	String username = "root";
	String password = "seriki@64";
	
	private Connection getConnection() throws DatabaseConnectionFailedException {
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			log.info("Database Connection {}", "Connection Successful");
			return connection;
		} catch (SQLException e) {
			throw new DatabaseConnectionFailedException(e.getMessage());
		}
	}
	
	@Override
	public Optional<Contact> saveContact(Contact contact) throws DatabaseConnectionFailedException, TableCreationFailedException, DatabaseInsertionFailedException, ContactDoesNotExistException {
		Connection connection = createdDatabase();
		PreparedStatement statement = saveContactToDatabase(connection, contact);
		ResultSet keys;
		try {
			keys = statement.getGeneratedKeys();
			if (keys.next()) {
				return findContactById(keys.getInt(1));
			}
			else throw new ContactDoesNotExistException("Contact does not exist");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PreparedStatement saveContactToDatabase(Connection connection, Contact contact) throws DatabaseInsertionFailedException {
		String sqlInsertQuery = "insert into `Contact` (`name`, phoneNumber, phonebookId) values (?, ?, ?)";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sqlInsertQuery, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, contact.getName());
			statement.setString(2, contact.getPhoneNumber());
			statement.setInt(3, contact.getPhonebookId());
			statement.executeUpdate();
			log.info("Saving {}", "Saved successfully!");
		} catch (SQLException e) {
			throw new DatabaseInsertionFailedException(e.getMessage());
		}
		return statement;
	}
	
	private Connection createdDatabase() throws DatabaseConnectionFailedException, TableCreationFailedException {
		String sqlTableCreationQuery = """
				CREATE TABLE IF NOT EXISTS `phonebook_db` `Contact` (
				`id` INT NOT NULL AUTO_INCREMENT,
				`name` VARCHAR(45) NULL,
				`phoneNumber` VARCHAR(15) NULL,
				`phonebookId` INT NULL,
				CONSTRAINT contact_pk PRIMARY KEY (id),
				CONSTRAINT contact_fk FOREIGN KEY (phonebookId) REFERENCES `phonebook`(`id`));
				""";
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sqlTableCreationQuery);
			statement.executeUpdate();
			log.info("Contact Database Created {}", "Table Contact created successfully");
		} catch (SQLException e) {
			throw new TableCreationFailedException(e.getMessage());
		}
		return connection;
	}
	
	@Override
	public Optional<Contact> findContactById(int contactId) {
		return Optional.empty();
	}
	
	@Override
	public void deleteContactById(int contactId) {
	
	}
	
	@Override
	public Set<Optional<Contact>> getAllContactRelatingToPhonebookWithThePhonebookId(int id) {
		return null;
	}
	
	@Override
	public Map<String, Optional<Contact>> getAllContactsInTheDatabase() {
		return null;
	}
	
	@Override
	public void clearDatabase() {
	
	}
	
	@Override
	public void dropDatabase() {
	
	}
}
