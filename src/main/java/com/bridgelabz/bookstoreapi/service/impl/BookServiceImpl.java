package com.bridgelabz.bookstoreapi.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstoreapi.dto.BookDTO;
import com.bridgelabz.bookstoreapi.entity.Book;
import com.bridgelabz.bookstoreapi.entity.Seller;
import com.bridgelabz.bookstoreapi.exception.BookException;
import com.bridgelabz.bookstoreapi.exception.SellerException;
import com.bridgelabz.bookstoreapi.repository.BookRepository;
import com.bridgelabz.bookstoreapi.repository.SellerRepository;
import com.bridgelabz.bookstoreapi.service.BookService;
import com.bridgelabz.bookstoreapi.service.IServiceElasticSearch;
import com.bridgelabz.bookstoreapi.utility.JWTUtil;

@Service
@PropertySource("classpath:message.properties")
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private JWTUtil jwt;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IServiceElasticSearch iServiceElasticSearch;
	
	public void addBook(BookDTO bookDTO, String token) {
		Long sId = jwt.decodeToken(token);
		Book book = new Book(bookDTO);
		Seller seller = sellerRepository.findById(sId).orElseThrow(() -> new SellerException(404, env.getProperty("104")));
		List<Book> books =  seller.getSellerBooks();
		boolean notExist = books.stream().noneMatch(bk -> bk.getBookName().equals(bookDTO.getBookName()));
		if(notExist) {
		seller.getSellerBooks().add(book);
		Book bookSave = bookRepository.save(book);
		sellerRepository.save(seller);
		try {
			iServiceElasticSearch.addBook(bookSave);
		} catch (Exception e) {
			throw new BookException(401, env.getProperty("111"));
		}
			
		}
		else {
			throw new BookException(500, env.getProperty("5001"));
		}
	}
	
	@Transactional
	public void updateBook(BookDTO bookDTO, String token, Long bookId) {
		Long sId = jwt.decodeToken(token);
		Seller seller = sellerRepository.findById(sId).orElseThrow(() -> new SellerException(404, env.getProperty("104")));
		List<Book> books = seller.getSellerBooks();
		Book filteredBook = books.stream().filter(book -> book.getBookId().equals(bookId)).findFirst()
				.orElseThrow(() -> new BookException(404, env.getProperty("4041")));
		filteredBook.setBookName(bookDTO.getBookName());
		filteredBook.setBookAuthor(bookDTO.getBookAuthor());
		filteredBook.setBookDescription(bookDTO.getBookDescription());
		filteredBook.setBookPrice(bookDTO.getBookPrice());
		filteredBook.setNoOfBooks(bookDTO.getNoOfBooks());
		filteredBook.setBookUpdatedTime(LocalDateTime.now());
		Book bookUpdate = bookRepository.save(filteredBook);
		sellerRepository.save(seller);
		try {
			iServiceElasticSearch.upDateBook(bookUpdate);
		} catch (Exception ae) {
			throw new BookException(500, env.getProperty("5001"));
		}
	}
	
	@Transactional
	public void deleteBook(String token, Long bookId) {
		Long sId = jwt.decodeToken(token);
		Seller seller = sellerRepository.findById(sId).orElseThrow(() -> new SellerException(404, env.getProperty("104")));
		List<Book> books = seller.getSellerBooks();
		Book filteredBook = books.stream().filter(book -> book.getBookId().equals(bookId)).findFirst()
				.orElseThrow(() -> new BookException(404, env.getProperty("4041")));
		books.remove(filteredBook);
		bookRepository.delete(filteredBook);
		sellerRepository.save(seller);
		try {
			iServiceElasticSearch.deleteBook(String.valueOf(bookId));
		} catch (Exception ae) {
			throw new BookException(500, env.getProperty("5001"));
		}
	}
	
	public List<Book> getBooks(Integer pageNo){
		Integer start = (pageNo-1)*10;
		return bookRepository.findBook(start);
	}
	
	public List<Book> getBooksSortedByPriceLow(Integer pageNo){
		Integer start = (pageNo-1)*10;
		return bookRepository.findBookSortedByPriceLow(start);
	}
	
	public List<Book> getBooksSortedByPriceHigh(Integer pageNo){
		Integer start = (pageNo-1)*10;
		return bookRepository.findBookSortedByPriceHigh(start);
	}
	
	public List<Book> getBooksSortedByArrival(Integer pageNo){
		Integer start = (pageNo-1)*10;
		return bookRepository.findBookSortedByArrival(start);
	}
}
