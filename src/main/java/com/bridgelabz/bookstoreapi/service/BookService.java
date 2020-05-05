package com.bridgelabz.bookstoreapi.service;

import java.util.List;

import com.bridgelabz.bookstoreapi.dto.BookDTO;
import com.bridgelabz.bookstoreapi.entity.Book;

public interface BookService {

	public void addBook(BookDTO bookDTO, String token);
	public void updateBook(BookDTO bookDTO, String token, Long bookId);
	public void deleteBook(String token, Long bookId);
	public List<Book> getBooks(Integer pageNo);
	public List<Book> getBooksSortedByPriceLow(Integer pageNo);
	public List<Book> getBooksSortedByPriceHigh(Integer pageNo);
	public List<Book> getBooksSortedByArrival(Integer pageNo);
	public List<Book> getBookByNameAndAuthor(String text) ;
	
}
