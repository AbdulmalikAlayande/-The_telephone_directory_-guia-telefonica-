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
		int id = 0;
		if (savedPhonebook.isPresent())
			id = savedPhonebook.get().getId();
		Optional<Phonebook> foundPhonebook = phonebookRepo.findPhonebookById((id));
		assertEquals(foundPhonebook, savedPhonebook);
		assertNotNull(foundPhonebook);
	}
	
	@SneakyThrows
	@Test void findByNameTest(){
		Phonebook phonebook = new Phonebook();
		phonebook.setName("Malik");
		Optional<Phonebook> savedPhonebook = phonebookRepo.save(phonebook);
		String name = null;
		if (savedPhonebook.isPresent()) name = savedPhonebook.get().getName();
		Optional<Phonebook> foundPhonebook = phonebookRepo.findPhonebookByName(name);
		assertEquals(foundPhonebook, savedPhonebook);
		assertNotNull(foundPhonebook);
	}
	
	@SneakyThrows
	@Test void savePhonebookDeleteSavedPhonebookByIdTest(){
		Phonebook phonebook = new Phonebook();
		phonebook.setName("Malik");
		Optional<Phonebook> savedPhonebook = phonebookRepo.save(phonebook);
		int id = 0;
		if (savedPhonebook.isPresent())
			id = savedPhonebook.get().getId();
		phonebookRepo.deletePhonebookById(phonebook.getId());
		assertNull(phonebookRepo.findPhonebookById(id));
	}
}
