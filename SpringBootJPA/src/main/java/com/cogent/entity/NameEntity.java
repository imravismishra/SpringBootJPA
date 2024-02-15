package com.cogent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class NameEntity {

	@Column(nullable = false)
	private String fname;
	private String mname;
	@Column(nullable = false)
	private String lname;

}
