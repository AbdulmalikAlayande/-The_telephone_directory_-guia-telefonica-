package com.example.phonebook_spring_boot.repo_test;

import com.example.phonebook_spring_boot.data.models.Contact;
 import com.example.phonebook_spring_boot.data.repositories.ContactsRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ContactsRepositoryTest {
	Contact contact;
	Optional<Contact> savedContact;
	@Autowired
	ContactsRepository contactsRepository;

	@SneakyThrows
	@BeforeEach void startAllTestWith(){
		contact = new Contact();
		savedContact = contactsRepository.saveContact(contact);
	}
	
	@AfterEach void endAllTestWith(){
		contactsRepository.clearDatabase();
	}
	
	@Test void saveNewContactContactIsSavedAndReturnedContactDoesNotHaveEmptyFieldTest(){
		assertThat(savedContact).isPresent();
		assertNotNull(savedContact.get().getName());
		assertNotNull(savedContact.get().getPhoneNumber());
	}
	
	@Test void saveContactFindContactByContactIdAndFoundContactIsNotNullTest(){
		savedContact.ifPresent(Contact -> {
			Optional<Contact> foundContact = contactsRepository.findContactById(savedContact.get().getId());
			assertNotNull(foundContact);
		});
	}
	
}