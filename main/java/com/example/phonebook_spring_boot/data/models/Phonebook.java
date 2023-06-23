package com.example.phonebook_spring_boot.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


import java.util.Objects;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Phonebook {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
	@NonNull
	private String name;
	
	@Override
	public boolean equals(Object object){
		if (object == null ||object.getClass() != getClass() || object.getClass() == null) return false;
		Phonebook phonebook = (Phonebook) object;
		return Objects.equals(phonebook.id, this.id) && phonebook.name.equals(this.name);
	}
}
