/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      <https://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PetDtoValidatorTests {

	private PetDtoValidator validator;

	@BeforeEach
	void setup() {
		validator = new PetDtoValidator();
	}

	@Test
	void supportsOnlyPetDto() {
		assertThat(validator.supports(PetDto.class)).isTrue();
		assertThat(validator.supports(Pet.class)).isFalse();
		assertThat(validator.supports(String.class)).isFalse();
	}

	@Test
	void shouldPassWithValidNewPet() {
		PetDto pet = new PetDto();
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertThat(errors.hasErrors()).isFalse();
	}

	@Test
	void shouldRejectBlankName() {
		PetDto pet = new PetDto();
		pet.setName("  ");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertThat(errors.hasFieldErrors("name")).isTrue();
		assertThat(errors.getFieldError("name").getCode()).isEqualTo("required");
	}

	@Test
	void shouldRejectMissingTypeForNewPet() {
		PetDto pet = new PetDto(); // isNew() == true because id is null
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertThat(errors.hasFieldErrors("type")).isTrue();
		assertThat(errors.getFieldError("type").getCode()).isEqualTo("required");
	}

	@Test
	void shouldNotRequireTypeForExistingPet() {
		PetDto pet = new PetDto();
		pet.setId(1); // isNew() == false
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertThat(errors.hasFieldErrors("type")).isFalse();
	}

	@Test
	void shouldRejectMissingBirthDate() {
		PetDto pet = new PetDto();
		pet.setName("Buddy");
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertThat(errors.hasFieldErrors("birthDate")).isTrue();
		assertThat(errors.getFieldError("birthDate").getCode()).isEqualTo("required");
	}

}