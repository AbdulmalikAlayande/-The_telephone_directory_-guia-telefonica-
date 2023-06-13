package com.example.phonebook_spring_boot.data.repositories;

import com.example.phonebook_spring_boot.data.models.Contact;
import com.example.phonebook_spring_boot.exceptions.DatabaseConnectionFailedException;
import com.example.phonebook_spring_boot.exceptions.DatabaseInsertionFailedException;
import com.example.phonebook_spring_boot.exceptions.StatementPreparationFailedException;
import com.example.phonebook_spring_boot.exceptions.contacts_exception.ContactDoesNotExistException;
import com.example.phonebook_spring_boot.exceptions.phonebookExceptions.TableCreationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;


@Repository
@Slf4j
public class ContactsRepoImplementation implements ContactsRepository{
	String url = "jdbc:mysql://localhost/phonebook_db?createDatabaseIfNotExist=true";
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
		} catch (SQLException | StatementPreparationFailedException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PreparedStatement saveContactToDatabase(Connection connection, Contact contact) throws DatabaseInsertionFailedException {
		String sqlInsertQuery = "insert into `contacts` (`id`,`name`, phoneNumber, phonebookId) values (null, ?, ?, ?)";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sqlInsertQuery, Statement.RETURN_GENERATED_KEYS);
			System.out.println("hello");
			statement.setString(1, contact.getName());
			statement.setString(2, contact.getPhoneNumber());
			statement.setInt(3, contact.getPhonebookId());
			System.out.println("Hello world!");
			statement.executeUpdate();
			System.out.println("Hello there");
			log.info("Saving {}", "Saved successfully!");
		} catch (SQLException e) {
			throw new DatabaseInsertionFailedException(e.getMessage());
		}
		return statement;
	}
	
	private Connection createdDatabase() throws DatabaseConnectionFailedException, TableCreationFailedException {
		String sqlTableCreationQuery = "CREATE TABLE IF NOT EXISTS `phonebook_db`.`contacts` (\n" +
				                               "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
				                               "  `name` VARCHAR(45) NULL,\n" +
				                               "  `phoneNumber` VARCHAR(15) NULL,\n" +
				                               "  `phonebookId` INT NULL,\n" +
				                               "  PRIMARY KEY (`id`));";
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sqlTableCreationQuery);
			System.out.println("as we go on");
			statement.executeUpdate();
			System.out.println("we remember");
			log.info("Contact Database Created {}", "Table Contact created successfully");
		} catch (SQLException e) {
			throw new TableCreationFailedException(e.getMessage());
		}
		return connection;
	}
	
	@Override
	public Optional<Contact> findContactById(int contactId) throws DatabaseConnectionFailedException, StatementPreparationFailedException {
		Connection connection = getConnection();
		String sqlFindByIdQuery = "select * from contacts where id = "+contactId;
		try {
			PreparedStatement statement = connection.prepareStatement(sqlFindByIdQuery);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return foundContact(resultSet);
			return Optional.empty();
		} catch (SQLException e) {
			throw new StatementPreparationFailedException(e.getMessage());
		}
	}
	
	private Optional<Contact> foundContact(ResultSet resultSet) throws SQLException {
		return Optional.of(Contact.builder()
				                   .id(resultSet.getInt(1))
				                   .name(resultSet.getString(2))
				                   .phoneNumber(resultSet.getString(3))
				                   .phonebookId(resultSet.getInt(4))
				                   .build());
	}
	
	@Override
	public void deleteContactById(int contactId) throws DatabaseConnectionFailedException, ContactDoesNotExistException {
		Connection connection = getConnection();
		String sqlDeleteStatement = "delete from contacts where id = "+contactId;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ContactDoesNotExistException(e.getMessage());
		}
	}
	
	@Override
	public Set<Optional<Contact>> getAllContactRelatingToPhonebookWithThePhonebookId(int id) {
		return null;
	}
	
	@Override
	public Map<String, Optional<Contact>> getAllContactsInTheDatabase() throws DatabaseConnectionFailedException, ContactDoesNotExistException {
		Connection connection = getConnection();
		int counter = 0;
		Map<String, Optional<Contact>> allContacts = new TreeMap<>();
		String sqlSelectAllStatement = "select * from contacts";
		try {
			PreparedStatement statement = connection.prepareStatement(sqlSelectAllStatement);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()){
				allContacts.put(String.valueOf(counter), foundContact(resultSet));
				counter++;
			}
		} catch (SQLException e) {
			throw new ContactDoesNotExistException(e.getMessage());
		}
		return allContacts;
	}
	
	@Override
	public void clearDatabase() {
	
	}
	
	@Override
	public void dropDatabase() {
	
	}
}
