package com.bridgelabz.bookstoreapi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstoreapi.entity.Book;
import com.bridgelabz.bookstoreapi.entity.CartDetails;
import com.bridgelabz.bookstoreapi.entity.OrderDetails;
import com.bridgelabz.bookstoreapi.entity.QuantityOfBooks;
import com.bridgelabz.bookstoreapi.entity.User;
import com.bridgelabz.bookstoreapi.exception.UserException;
import com.bridgelabz.bookstoreapi.repository.CartRepository;
import com.bridgelabz.bookstoreapi.repository.UserRepository;
import com.bridgelabz.bookstoreapi.service.OrderService;
import com.bridgelabz.bookstoreapi.utility.JWTUtil;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JWTUtil jwt;
	
	@Autowired
	private Environment env;
	
	@Transactional
	@Override
	public List<OrderDetails> getOrderList(String token) {
		Long id = jwt.decodeToken(token);
		User userdetails = userRepository.findById(id)
				.orElseThrow(()->new UserException(400, env.getProperty("104")));
		
		return userdetails.getOrderBookDetails();
		
	}

	@Override
	public User getOrderConfrim(String token) {
		Long id = jwt.decodeToken(token);
		User userdetails = userRepository.findById(id)
				.orElseThrow(()->new UserException(400, env.getProperty("104")));
		
		OrderDetails orderDetails=new OrderDetails();
		QuantityOfBooks order=new QuantityOfBooks();
		Random random=new Random();
        //userdetails.getOrderBookDetails().clear();
		ArrayList<Book> list=new ArrayList<>();
		/**
		 * adding the books to orderlist by fetching it from cartlist
		 */
		userdetails.getCartBooks().forEach((cart)->{
			cart.getBooksList().forEach(book->{
				list.add(book);
				long orderId=random.nextInt(1000000);
				if(orderId<0) {
					orderId=orderId*-1;
				}
				orderDetails.setOrderId(orderId);
				orderDetails.setOrderPlaceTime(LocalDateTime.now());
				orderDetails.setBooksList(list);
				userdetails.getOrderBookDetails().add(orderDetails);
				long quantity = cart.getQuantityOfBooks().getQuantityOfBook();
				System.out.println("*****************"+cart.getQuantityOfBooks().getQuantityOfBook());
				order.setQuantityOfBook(quantity);
				userdetails.getOrderBookDetails().forEach(orders->{
					orders.setQuantityOfBooks(order);
					});
				
			});
				
		});
		
		/**
		 * clearing the cart after added to the orderlist
		 */
		userdetails.getCartBooks().remove(userdetails);
		return userRepository.save(userdetails);
		
	}

}
