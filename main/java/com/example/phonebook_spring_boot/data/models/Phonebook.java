package com.example.phonebook_spring_boot.data.models;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Phonebook {
	private int id;
	@NonNull
	private String name;
	
	@Override public boolean equals(Object object){
		if (object == null ||object.getClass() != getClass() || object.getClass() == null) return false;
		Phonebook phonebook = (Phonebook) object;
		return phonebook.id == this.id && phonebook.name.equals(this.name);
	}
}
