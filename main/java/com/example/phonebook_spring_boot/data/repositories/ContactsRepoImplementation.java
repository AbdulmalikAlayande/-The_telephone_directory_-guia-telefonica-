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
	
	@Override
	public Optional<Contact> saveContact(Contact contact) throws DatabaseConnectionFailedException, TableCreationFailedException, DatabaseInsertionFailedException, ContactDoesNotExistException {
		PreparedStatement statement = null;
		Connection connection;
		try{
			connection = createdDatabase();
			statement = saveContactToDatabase(connection);
			statement.setString(1, contact.getName());
			statement.setString(2, contact.getPhoneNumber());
			statement.setInt(3, contact.getPhonebookId());
			statement.executeUpdate();
			log.info("Saving {}", "Saved successfully!");
//			connection.close();
		}catch (SQLException exception){
			log.info("Error ===::> {}", exception.getMessage());
		}
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
	
	private PreparedStatement saveContactToDatabase(Connection connection) throws DatabaseInsertionFailedException {
		String sqlInsertQuery = """
				insert into Contact (name) values (?)
				insert into Contact (phoneNumber) values (?)
				insert into Contact (phonebookId) values (?)
				""";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sqlInsertQuery, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			throw new DatabaseInsertionFailedException(e.getMessage());
		}
		return statement;
	}
	
	private Connection createdDatabase() throws DatabaseConnectionFailedException, TableCreationFailedException {
		Connection connection = getConnection();
		String sqlTableCreationQuery = """
										use phonebook_db;
										create table if not exist `Contact`(
										`name` varchar(45) null,
										phoneNumber varchar(15) null,
										phonebookId int null,
										id not null auto_increment,
										constraint contact_pk primary key(id),
										constraint contact_fk foreign key(phonebookId)
																references phonebook(phonebookId)
										);
										""";
		try {
			PreparedStatement statement = connection.prepareStatement(sqlTableCreationQuery);
			statement.executeQuery();
			return connection;
		} catch (SQLException e) {
			throw new TableCreationFailedException(e.getMessage());
		}
	}
	
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
