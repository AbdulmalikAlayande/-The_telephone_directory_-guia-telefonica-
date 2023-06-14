package com.example.phonebook_spring_boot.data.repositories;

import com.example.phonebook_spring_boot.data.models.Phonebook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhonebookJpaRepository extends JpaRepository<Phonebook, String> {
	
}
