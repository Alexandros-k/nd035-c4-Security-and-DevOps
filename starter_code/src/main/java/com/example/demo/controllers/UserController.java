package com.example.demo.controllers;

import java.io.IOException;

import com.splunk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	public static final Logger log = LoggerFactory.getLogger(UserController.class);

	/*@Autowired
	 TcpInput tcpInput;*/

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
	/*	try {
			tcpInput.submit("INFO: New user create request received");
			tcpInput.submit(createUserRequest.getUsername());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}*/
		Cart cart = new Cart();
		cartRepository.save(cart);
		log.info("CREATE_CART = SUCCESS");
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.error("CREATE_USER_REQUEST = ERROR - Either length is less than 7 or pass and conf pass do not match. Unable to create, "
					+""+createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info("CREATE_USER_REQUEST =SUCCESS, name:" + createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}

	@Bean
	TcpInput splunkService() throws IOException {
		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
// Create a map of arguments and add login parameters that you get from splunk
		ServiceArgs loginArgs = new ServiceArgs();
		loginArgs.setUsername("axelllf13");
		loginArgs.setPassword("Alfxr1324$");
		loginArgs.setHost("127.0.0.1");
		loginArgs.setPort(8089);
// Create a Service instance and log in with the argument map
		com.splunk.Service service = com.splunk.Service.connect(loginArgs);
		IndexCollectionArgs indexcollArgs = new IndexCollectionArgs();
		indexcollArgs.setSortKey("totalEventCount");
		indexcollArgs.setSortDirection(IndexCollectionArgs.SortDirection.DESC);
		IndexCollection myIndexes = service.getIndexes(indexcollArgs);
		for (Index entity : myIndexes.values()) {
			System.out.println("  " + entity.getName() + " (events: "
					+ entity.getTotalEventCount() + ")");
		}
		for (Application app : service.getApplications().values()) {
			System.out.println(app.getName());
		}
// Get the collection of data inputs
		InputCollection myInputs = service.getInputs();
		// Iterate and list the collection of inputs
		System.out.println("There are " + myInputs.size() + " data inputs:\n");
		for (Input entity : myInputs.values()) {
			System.out.println("  " + entity.getName() + " (" + entity.getKind() + ")");
		}
// Retrieve the input
		return (TcpInput) service.getInputs().get("3000");
	}
}