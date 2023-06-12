package com.example.phonebook_spring_boot.data.repositories;
//git push -u origin main
import com.example.phonebook_spring_boot.data.models.Phonebook;
import com.example.phonebook_spring_boot.exceptions.DatabaseConnectionFailedException;
import com.example.phonebook_spring_boot.exceptions.phonebookExceptions.PhonebookDoesNotExistException;
import com.example.phonebook_spring_boot.exceptions.phonebookExceptions.TableCreationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Formatter;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Repository
@Slf4j
public class PhonebookRepoImpl implements PhonebookRepo{
	private Connection getConnection() throws DatabaseConnectionFailedException {
		Connection connection;
		try{
			connection = DriverManager.getConnection(url, username, password);
			log.info("Database connection {}", "Connected successfully");
		}
		catch (SQLException exception){
			throw new DatabaseConnectionFailedException("Failed to connect to database");
		}
		return connection;
	}
	String url = "jdbc:mysql://localhost/phonebook_db?createDatabaseIfNotExist=true";
	String username = "root";
	String password = "seriki@64";
	
	@Override
	public Optional<Phonebook> save(Phonebook phonebook) throws DatabaseConnectionFailedException, TableCreationFailedException, PhonebookDoesNotExistException {
		Connection connection = prepareSqlTableCreationQuery();
		String sql = "INSERT INTO phonebook (name) VALUES (?)";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, phonebook.getName());
			statement.executeUpdate();
			log.info("Saving {}", "Saved successfully!");
		}catch (SQLException exception){
			throw new TableCreationFailedException("ERROR: Failed to save phonebook!");
		}
		try (ResultSet keys = statement.getGeneratedKeys()){
			if (keys.next()) {
				return findPhonebookById(keys.getInt(1));
			}
			connection.close();
			throw new  PhonebookDoesNotExistException("phonebook does not exist");
		} catch (SQLException e) {
			throw new PhonebookDoesNotExistException(e.getMessage());
		}
	}
	
	private Connection prepareSqlTableCreationQuery() throws DatabaseConnectionFailedException, TableCreationFailedException {
		String sqlQuery = """
				CREATE TABLE if not exists `phonebook_db`.`phonebook` (
				`id` INT NOT NULL AUTO_INCREMENT,
				`name` VARCHAR(45) NULL,
				PRIMARY KEY (`id`));
				""";
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();
			log.info("Db table creation {}", "Table phonebook created successfully");
		} catch (SQLException e) {
			throw new TableCreationFailedException(e.getMessage());
		}
		return connection;
	}
	
	@Override
	public Optional<Phonebook> findPhonebookById(int phonebookId) throws DatabaseConnectionFailedException {
		String sqlFindByIdQuery = "select * from phonebook where id = "+phonebookId;
		PreparedStatement statement;
		Connection connection = getConnection();
		try {
			statement =connection.prepareStatement(sqlFindByIdQuery);
			ResultSet sqlReturn = statement.executeQuery();
			if (sqlReturn.next()){
				return phonebook(sqlReturn);
			}
			else return Optional.empty();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, Optional<Phonebook>> getAllPhonebooksInTheDatabase() throws DatabaseConnectionFailedException {
		Map<String, Optional<Phonebook>> allPhonebooks = new TreeMap<>();
		int counter = 0;
		String sqlGetAllPhonebookQuery = "select * from phonebook";
		Connection connection = getConnection();
		try(PreparedStatement statement = connection.prepareStatement(sqlGetAllPhonebookQuery)) {
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()){
				allPhonebooks.put(String.valueOf(counter), phonebook(resultSet));
				counter++;
			}
			return allPhonebooks;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Optional<Phonebook> findPhonebookByName(String phonebookName) throws DatabaseConnectionFailedException, PhonebookDoesNotExistException {
		String findByNameSqlQuery = new Formatter().format("select * from phonebook where name = '%s'", phonebookName).toString();
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(findByNameSqlQuery);
			System.out.println("DAY");
			ResultSet result = statement.executeQuery();
			if (result.next()){
				return phonebook(result);
			}
			return Optional.empty();
		} catch (SQLException e) {
			throw new PhonebookDoesNotExistException(e.getMessage());
		}
	}
	
	@Override
	public void deletePhonebookById(int id) throws DatabaseConnectionFailedException {
		String sqlDeleteQuery = "delete from phonebook where id ="+id;
		Connection connection;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlDeleteQuery);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseConnectionFailedException(e.getMessage());
		}
	}
	
	@Override
	public void deleteAll() throws DatabaseConnectionFailedException {
		Connection connection = getConnection();
		String sqlDeleteAllQuery = "DELETE FROM phonebook";
		try {
			connection.prepareStatement(sqlDeleteAllQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public void deleteDatabase() throws DatabaseConnectionFailedException {
		Connection connection = getConnection();
		String sqlDropSchemaQuery = "drop database phonebook_db";
		try {
			connection.prepareStatement(sqlDropSchemaQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	private static Optional<Phonebook> phonebook(ResultSet result) throws SQLException {
		return Optional.of(Phonebook.builder()
				                   .name(result.getString(2))
				                   .id(result.getInt(1))
				                   .build());
	}
}
