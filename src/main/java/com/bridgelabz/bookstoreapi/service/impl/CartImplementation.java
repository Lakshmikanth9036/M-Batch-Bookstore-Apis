package com.bridgelabz.bookstoreapi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstoreapi.entity.Book;
import com.bridgelabz.bookstoreapi.entity.CartDetails;
import com.bridgelabz.bookstoreapi.entity.QuantityOfBooks;
import com.bridgelabz.bookstoreapi.entity.User;
import com.bridgelabz.bookstoreapi.exception.UserException;
import com.bridgelabz.bookstoreapi.repository.BookRepository;
import com.bridgelabz.bookstoreapi.repository.UserRepository;
import com.bridgelabz.bookstoreapi.service.CartService;
import com.bridgelabz.bookstoreapi.utility.JWTUtil;

@Service
@PropertySource("classpath:message.properties")
public class CartImplementation implements CartService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JWTUtil jwt;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private Environment env;
	
	@Override
	public User addBooksToCart(String token, long bookId) {
		long id = (Long) jwt.decodeToken(token);
		
		User user = userRepository.findUserById(id)
				.orElseThrow(() -> new UserException(401, env.getProperty("104")));
		
		CartDetails cart=new CartDetails();
		ArrayList<Book> booklist=new ArrayList<>();
		
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new UserException(201, env.getProperty("104")));
		
		booklist.add(book);
		cart.setPlaceTime(LocalDateTime.now());
		cart.setBooksList(booklist);
	    user.getCartBooks().add(cart);
	   
	
		return userRepository.save(user);
	       	
	}
	
	@Override
	public User addBooksQuantityToCart(String token, long bookId,long quantity) {
		
		long id = (Long) jwt.decodeToken(token);
		QuantityOfBooks cartquantity=new QuantityOfBooks();
		User user = userRepository.findUserById(id)
				.orElseThrow(() -> new UserException(401, env.getProperty("104")));
		
	        user.getCartBooks().forEach((cart)->{
	        	cart.getBooksList().forEach((books)->{
	        		if(books.getBookId()==bookId) {
		       			cartquantity.setQuantityOfBook(quantity);
		       			cart.getQuantityOfBooks().add(cartquantity);
		       		}
	        	});
	       		
	       	});
	        return user;
	        
	}

	
	@Override
	public List<CartDetails> getBooksfromCart(String token) {
		long id = (Long) jwt.decodeToken(token);
		
		User user = userRepository.findUserById(id)
				.orElseThrow(() -> new UserException(401, env.getProperty("104")));
	 List<CartDetails> cartBooks = user.getCartBooks();
	 return cartBooks;
	}

	@Override
	public User removeBooksToCart(String token, long bookId) {
		
		CartDetails cart=new CartDetails();
		long id = (Long) jwt.decodeToken(token);
		
		User user = userRepository.findUserById(id)
				.orElseThrow(() -> new UserException(201, env.getProperty("104")));
		
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new UserException(201, env.getProperty("104")));
		
		cart.setPlaceTime(LocalDateTime.now());
		user.getCartBooks().forEach((books)->{
			 books.getBooksList().remove(book);
		});
		
		user.getCartBooks().add(cart);
		return userRepository.save(user);
	}
}