package com.example.phonebook_spring_boot.data.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Contact {
	private int id;
	private int phonebookId;
	private String name;
	private String phoneNumber;
}
