package com.capgemini.capstore.main.controller;

import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.capstore.main.beans.MerchantProduct;
import com.capgemini.capstore.main.beans.Product;
import com.capgemini.capstore.main.beans.Role;
import com.capgemini.capstore.main.beans.User;
import com.capgemini.capstore.main.service.IMerchantService;

@RestController
public class MyController extends HttpServlet{

	@Context
	HttpServletRequest request;
	
	@Autowired
	IMerchantService service;
	
	@RequestMapping(value="/")
	public String homePage() {
		return "indexPage";
	}
	
	@RequestMapping(value="/logIn")
	public String logIn(@Valid @RequestBody User user) {
		Optional<User> result = service.ValidateLogIn(user);
		if(result != null) {
			//apply session here
			HttpSession session = request.getSession(true);
			return "HomePage";
		}
		//set error msg
		return "logInPageWithErrorMessage";
	}
	
	@RequestMapping(value="/SignUp")
	public String signUp(@Valid @RequestBody User user) {
		if(ValidateUserDetails(user)) {			
			if(user.getRole() == Role.Customer) {
				return "RegisterCustomerPage";
			}
			else if(user.getRole() == Role.Merchant) {
				return "RegisterMerchantPage";
			}
		}
		else {
			//set error msg details are incorrect
			return "SignUpPage";
		}
		return "SignUpPage";
	}
	
	private boolean ValidateUserDetails(@Valid User user) {
		return true;
	}

	@RequestMapping(method=RequestMethod.GET, value="/addProduct")
	public String addProduct(@Valid @RequestBody Product product, @PathVariable double productPrice, @PathVariable int productQuantity) {
		
		System.out.println(product);
		System.out.println(productPrice);
		System.out.println(productQuantity);
			
		int merchantId = -5;//get this from session
		
		int ProductId = product.getProductId();
		MerchantProduct merchantProduct = new MerchantProduct();
		merchantProduct.setMerchant(service.getMerchant(merchantId));
		merchantProduct.setProduct(product);
		merchantProduct.setProductPrice(productPrice);
		merchantProduct.setProductQuantity(productQuantity);
		
		return "Successfull Add Product";
	}

}
