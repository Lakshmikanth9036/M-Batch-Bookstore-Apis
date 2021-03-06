package com.bridgelabz.bookstoreapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelabz.bookstoreapi.entity.Book;
import com.bridgelabz.bookstoreapi.entity.CartDetails;
import com.bridgelabz.bookstoreapi.entity.User;
import com.bridgelabz.bookstoreapi.response.UserResponse;
import com.bridgelabz.bookstoreapi.service.WhishListService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/wishList")
@PropertySource("classpath:message.properties")
@CrossOrigin("*")
@Api(value="bookStore", description="Operations pertaining to Whishlist in Book Store")
public class WhishListController {

	@Autowired
	private WhishListService whishlistService;

	@Autowired
	private Environment env;
	
	@ApiOperation(value = "Adding the books to the Whishlist",response = Iterable.class)
	@PostMapping(value="/book/{bookId}")
	public ResponseEntity<UserResponse> addBooksToWhilist(@RequestHeader(name="token") String token,@PathVariable("bookId") Long bookId) throws Exception {
		    List<Book> whishlist = whishlistService.addBooksToWhishList(token,bookId);
   	    return ResponseEntity.status(HttpStatus.CREATED)
					.body(new UserResponse(env.getProperty("600"), whishlist,HttpStatus.OK));  	
	}
	
	@ApiOperation(value = "Getting the books from Whishlist",response = Iterable.class)
	@GetMapping(value="/book")
	public ResponseEntity<UserResponse> getBooksfromCart(@RequestHeader(name="token")  String token) throws Exception {
		    List<Book> whishlist = whishlistService.getBooksfromWhishList(token);
		    return ResponseEntity.status(HttpStatus.CREATED)
 				.body(new UserResponse(env.getProperty("603"),  whishlist,HttpStatus.OK));  
	}
	
	@ApiOperation(value = "Getting the books from Whishlist",response = Iterable.class)
	@GetMapping(value="/bookcount")
	public ResponseEntity<UserResponse> getBooksCountfromCart(@RequestHeader(name="token")  String token) throws Exception {
		    int whishlist = whishlistService.getcountBooksfromWhishList(token);
     	    return ResponseEntity.status(HttpStatus.CREATED)
					.body(new UserResponse(env.getProperty("603"),  whishlist,HttpStatus.OK));  
	}

	@ApiOperation(value = "Removing the books to the Whishlist",response = Iterable.class)
	@DeleteMapping(value="/book/{token}/{bookId}")
	public ResponseEntity<UserResponse> removeBooksToWhilist(@PathVariable("token") String token,@PathVariable("bookId") long bookId) throws Exception {
		    List<Book> whishlist = whishlistService.removeBooksToWhishList(token,bookId);
		    return ResponseEntity.status(HttpStatus.CREATED)
					.body(new UserResponse(env.getProperty("604"), whishlist,HttpStatus.OK));  	
	}
	
	@ApiOperation(value = "Verify the books in the wishlist",response = Iterable.class)
	@GetMapping(value="/verify_book/{token}")
	public ResponseEntity<UserResponse> verifyBookInWishlist(@PathVariable(name="token") String token,@RequestParam("bookId") Long bookId) throws Exception {
		    boolean cart = whishlistService.verifyBookInWishlist(token, bookId);
		    return ResponseEntity.status(200)
					.body(new UserResponse(env.getProperty("500"), cart,HttpStatus.OK));
		  	}
}
