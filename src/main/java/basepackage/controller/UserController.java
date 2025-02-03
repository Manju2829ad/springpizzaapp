package basepackage.controller;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import basepackage.dto.UserDTO;
import basepackage.service.UserService;
import basepackage.util.JwtTokenUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil; // Inject the JwtTokenUtil to use for token generation

    /**
     * Endpoint for creating a new user.
     * Generates a JWT token upon successful creation.
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        String successMessage = "User created successfully";
        String failureMessage = "User creation failed";

        try {
            // Save the user
            String result = userService.saveUser(userDTO);

            // If save is successful
            if ("ok".equalsIgnoreCase(result)) {
                // Generate a token using the user's email
                String token = jwtTokenUtil.generateTokenUsingEmail(userDTO.getEmail());
                return ResponseEntity.status(HttpStatus.CREATED).body(token);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failureMessage);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failureMessage);
        }
    }

    /**
     * Endpoint for fetching a user by ID with token validation.
     * Token is validated against email or mobile number.
     */
    @GetMapping(value = "/getuser/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        System.out.println("Inside the get method");

        // Remove "Bearer" from the token if it's passed with the prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Validate the token
        boolean isValidToken = jwtTokenUtil.validateToken(token);

        // If token validation fails, return a 401 Unauthorized
        if (!isValidToken) {
            System.out.println("Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Token is valid, proceed to fetch the user by ID
        UserDTO user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            System.out.println("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Endpoint for updating user details.
     * Requires a valid JWT token in the Authorization header.
     */
    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token) {
        String success = "Details updated successfully";
        String failure = "Failed to update the details";

        try {
            // Check for "Bearer " prefix and remove it
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Validate the token
            boolean isValidToken = jwtTokenUtil.validateToken(token);

            if (!isValidToken) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }

            // Proceed to update user details
            String response = userService.updateUser(userDTO);
            if ("ok".equalsIgnoreCase(response)) {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failure);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating your details. Please try again later.");
        }
    }
}
