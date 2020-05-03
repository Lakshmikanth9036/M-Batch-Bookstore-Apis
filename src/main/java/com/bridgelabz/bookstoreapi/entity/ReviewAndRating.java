package com.bridgelabz.bookstoreapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="review_and_rating")
@Data
@NoArgsConstructor
@ToString
public class ReviewAndRating {

	@Id
	@GenericGenerator(name = "idGen", strategy = "increment")
	@GeneratedValue(generator = "idGen")
	private long ratingReviewId;
	
	@Column(name = "rating", nullable = false)
	private int rating;
	
	@Column(name = "review", nullable = false)
	private String review;
		
	
	
}