package com.example.phonebook_spring_boot;

import com.example.phonebook_spring_boot.data.models.Phonebook;
import com.example.phonebook_spring_boot.data.repositories.PhonebookJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PhonebookSpringBootApplicationTests {
	Phonebook phonebook;
	
	@Autowired
	PhonebookJpaRepository jpaRepository;
	Phonebook savedPhonebook;
	
	@Test
	void contextLoads() {
	}
	
	@BeforeEach
	void setUp() {
		phonebook = new Phonebook();
		phonebook.setName("Abdulmalik");
		savedPhonebook = jpaRepository.save(phonebook);
	}
	
	@AfterEach
	void tearDown() {
	}
	
	@Test void savePhonebookTest(){
		assertEquals(BigInteger.ONE.intValue(), jpaRepository.count());
		assertNotNull(savedPhonebook);
		assertNotNull(savedPhonebook.getName());
		assertNotNull(savedPhonebook.getId());
	}
	
	@Test void savePhonebook_FindSavedPhonebookByIdTest(){
		Optional<Phonebook> foundPhonebook = jpaRepository.findById(phonebook.getId());
		assertThat(foundPhonebook).isPresent();
		assertEquals(foundPhonebook.get(), savedPhonebook);
	}
	
}
