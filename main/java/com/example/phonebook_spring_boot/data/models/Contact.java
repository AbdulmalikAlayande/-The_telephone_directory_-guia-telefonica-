package com.example.phonebook_spring_boot.data.models;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Contact {
	private int id;
	@NonNull
	private int phonebookId;
	@NonNull
	private String name;
	@NonNull
	private String phoneNumber;
	
	@Override
	public boolean equals(Object object){
		if (object == null || object.getClass() != getClass() || object.getClass() != null) return false;
		Contact contact = (Contact) object;
		return contact.phoneNumber.equals(this.phoneNumber) ||contact.name.equals(this.name) || contact.id == this.id;
	}
}
