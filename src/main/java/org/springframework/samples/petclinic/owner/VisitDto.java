package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;

public class VisitDto {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date = LocalDate.now();

	@NotBlank
	private String description;

	public boolean isNew() {
		return true;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}