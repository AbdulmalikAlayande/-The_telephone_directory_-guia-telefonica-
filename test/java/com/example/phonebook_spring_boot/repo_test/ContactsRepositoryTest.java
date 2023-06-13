package com.example.phonebook_spring_boot.repo_test;

import com.example.phonebook_spring_boot.data.models.Contact;
import com.example.phonebook_spring_boot.data.repositories.ContactsRepository;
import com.example.phonebook_spring_boot.exceptions.DatabaseConnectionFailedException;
import com.example.phonebook_spring_boot.exceptions.StatementPreparationFailedException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
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
		contact.setName("Abdulmalik");
		contact.setPhonebookId(34);
		contact.setPhoneNumber("07036174617");
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
			Optional<Contact> foundContact;
			try {
				foundContact = contactsRepository.findContactById(savedContact.get().getId());
			} catch (DatabaseConnectionFailedException | StatementPreparationFailedException e) {
				throw new RuntimeException(e);
			}
			assertNotNull(foundContact);
		});
	}
	
	@SneakyThrows
	@Test void saveContact_DeleteSavedContactById_COntactNoLongerExistsInDatabaseAfterDeletionTest(){
		int id = savedContact.get().getId();
		savedContact.ifPresent(this::accept);
		assertThat(contactsRepository.findContactById(id)).isEmpty();
	}
	
	@SneakyThrows
	private void accept(Contact value) {
		contactsRepository.deleteContactById(value.getId());
	}
	
	@SneakyThrows
	@Test void getAllContactEntitiesPresentInTheDatabaseTest(){
		Contact contact1 = new Contact();
		contact1.setName("Abolade");
		contact1.setPhonebookId(34);
		contact1.setPhoneNumber("08181587649");
		contactsRepository.saveContact(contact1);
		Map<String, Optional<Contact>> allContacts = contactsRepository.getAllContactsInTheDatabase();
		for (int i = 0; i < allContacts.size(); i++) {
			assertNotNull(allContacts.get(String.valueOf(i+1)));
		}
	}
}