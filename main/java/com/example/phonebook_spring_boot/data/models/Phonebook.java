package com.example.phonebook_spring_boot.data.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Phonebook {
	
	@Id
	private String id;
	@NonNull
	private String name;
	
	@Override
	public boolean equals(Object object){
		if (object == null ||object.getClass() != getClass() || object.getClass() == null) return false;
		Phonebook phonebook = (Phonebook) object;
		return Objects.equals(phonebook.id, this.id) && phonebook.name.equals(this.name);
	}
}
