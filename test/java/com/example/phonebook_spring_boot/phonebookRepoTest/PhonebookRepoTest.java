package com.example.phonebook_spring_boot.phonebookRepoTest;

import com.example.phonebook_spring_boot.data.models.Phonebook;
import com.example.phonebook_spring_boot.data.repositories.PhonebookRepo;
import com.example.phonebook_spring_boot.exceptions.DatabaseConnectionFailedException;
import com.example.phonebook_spring_boot.exceptions.PhonebookDoesNotExistException;
import com.example.phonebook_spring_boot.exceptions.TableCreationFailedException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PhonebookRepoTest {
	@Autowired
	PhonebookRepo phonebookRepo;
	
	@Test void saveTest() throws DatabaseConnectionFailedException, TableCreationFailedException, PhonebookDoesNotExistException {
		Phonebook phonebook = new Phonebook();
		phonebook.setName("Malik");
		Optional<Phonebook> savedPhonebook = phonebookRepo.save(phonebook);
		assertNotNull(savedPhonebook);
	}
	@SneakyThrows
	@AfterEach void endAllTestWith(){
//		phonebookRepo.deleteAll();
//		phonebookRepo.deleteDatabase();
	}
	
	@SneakyThrows
	@Test void findByIdTest(){
		Phonebook phonebook = new Phonebook();
		phonebook.setName("Malik");
		Optional<Phonebook> savedPhonebook = phonebookRepo.save(phonebook);
		System.out.println("The phonebook id is: "+phonebook.getId());
		Optional<Phonebook> foundPhonebook = phonebookRepo.findPhonebookById(3);
		assertEquals(foundPhonebook, savedPhonebook);
		assertNotNull(foundPhonebook);
	}
	
	@SneakyThrows
	@Test void findByNameTest(){
		Phonebook phonebook = new Phonebook();
		phonebook.setName("Malik");
		Optional<Phonebook> savedPhonebook = phonebookRepo.save(phonebook);
		Optional<Phonebook> foundPhonebook = phonebookRepo.findPhonebookByName(phonebook.getName());
		assertEquals(foundPhonebook, savedPhonebook);
		assertNotNull(foundPhonebook);
	}
	
	@SneakyThrows
	@Test void savePhonebookDeleteSavedPhonebookByIdTest(){
		Phonebook phonebook = new Phonebook();
		phonebook.setName("Malik");
		Optional<Phonebook> savedPhonebook = phonebookRepo.save(phonebook);
		phonebookRepo.deletePhonebookById(3);
		assertNull(phonebookRepo.findPhonebookById(phonebook.getId()));
	}
}
