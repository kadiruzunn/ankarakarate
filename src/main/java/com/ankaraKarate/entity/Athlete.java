package com.ankaraKarate.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="sporcular")
public class Athlete {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Category category;
	@NotNull
	@NotEmpty(message="{name.notempty}")
	private String name;
	private String image;
	private String belt;
	private String club;
	private String gender;
	private String trainer;
	@Min(0)
	private Double weight;
	private WeightType weightType;
	@Past(message= "{productiondate.past}")
	private LocalDate birthDate;
	private Double criticalWeight;
	
	
	
}
