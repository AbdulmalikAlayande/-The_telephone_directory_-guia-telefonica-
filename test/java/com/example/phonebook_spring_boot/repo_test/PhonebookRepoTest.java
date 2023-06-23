package com.example.phonebook_spring_boot.repo_test;

import com.example.phonebook_spring_boot.data.models.Phonebook;
import com.example.phonebook_spring_boot.data.repositories.PhonebookRepo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PhonebookRepoTest {
	@Autowired
	PhonebookRepo phonebookRepo;
	Optional<Phonebook> savedPhonebook;
	Phonebook phonebook;
	@SneakyThrows
	@BeforeEach void startAllTestWith(){
		phonebook = new Phonebook();
		phonebook.setName("Malik");
		savedPhonebook = phonebookRepo.save(phonebook);
	}
	
	@SneakyThrows
	@AfterEach void endAllTestWith(){
		phonebookRepo.deleteAll();
	}
	@Test void saveTest() {
		assertNotNull(savedPhonebook);
	}
	
	@SneakyThrows
	@Test void findByIdTest(){
		Long id = null;
		if (savedPhonebook.isPresent()) id = savedPhonebook.get().getId();
		Optional<Phonebook> foundPhonebook = phonebookRepo.findPhonebookById((id));
		assertEquals(foundPhonebook, savedPhonebook);
		assertNotNull(foundPhonebook);
	}
	
	@SneakyThrows
	@Test void findByNameTest(){
		String name = null;
		if (savedPhonebook.isPresent()) name = savedPhonebook.get().getName();
		Optional<Phonebook> foundPhonebook = phonebookRepo.findPhonebookByName(name);
		assertNotNull(foundPhonebook);
	}
	
	@SneakyThrows
	@Test void savePhonebookDeleteSavedPhonebookByIdTest(){
		Long id = 0L;
		if (savedPhonebook.isPresent())
			id = savedPhonebook.get().getId();
		phonebookRepo.deletePhonebookById(id);
		assertThat(phonebookRepo.findPhonebookById(id)).isEmpty();
		assertFalse(phonebookRepo.findPhonebookById(id).isPresent());
	}
	
	@SneakyThrows
	@Test void getAllPhonebooksInTheDatabaseTest(){
		Map<String, Optional<Phonebook>> allPhonebooksInTheDatabase = phonebookRepo.getAllPhonebooksInTheDatabase();
		Map<String, Optional<Phonebook>> phonebooks = new TreeMap<>(allPhonebooksInTheDatabase);
		System.out.println(allPhonebooksInTheDatabase);
		System.out.println(phonebooks);
		for (int i = 0; i < phonebooks.size(); i++) {
			assertNotNull(phonebooks.get(String.valueOf(i)));
		}
		assertEquals(phonebooks, allPhonebooksInTheDatabase);
	}
}
