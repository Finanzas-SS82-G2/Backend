package com.finanzas.backend.controller;


import com.finanzas.backend.entities.User;
import com.finanzas.backend.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "Users", value = "Web Service RESTFul of Users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all Users", notes = "Method to list all Users")
    @ApiResponses({
            @ApiResponse(code = 200, message = "All Users founds"),
            @ApiResponse(code = 404, message = "Users Not Found"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<List<User>> findAllUsers(){
        try {
            List<User> users = userService.getAll();
            if(users.size()>0)
                return new ResponseEntity<>(users, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search User by Id", notes = "Method for find a User by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User found by Id"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<User> findById (@PathVariable ("id") Long id){
        try {
            Optional<User> user = userService.getById(id);
            if(!user.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/searchByFirstName/{firstName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search User by First Name", notes = "Method for find a User by first name")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User found by First Name"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<List<User>>findByFirstName (@PathVariable ("firstName") String firstName){
        try {
            List<User> users = userService.findByFirstName(firstName);
            if(users.size()>0)
                return new ResponseEntity<>(users, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/searchByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search User by Email", notes = "Method for find a User by email")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User found by Email"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<User>findByEmail (@PathVariable ("email") String email){
        try {
            User user = userService.findByEmail(email);
            if(user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create User", notes = "Method to create a new User")
    @ApiResponses({
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 404, message = "User not created"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<User> insertUser(@Valid @RequestBody User user){
        try{
            User userNew = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userNew);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update User", notes = "Method to update a User")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User updated"),
            @ApiResponse(code = 404, message = "User not updated"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user){
        try{
            Optional<User> currentUser = userService.getById(id);
            if(!currentUser.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else {
                user.setId(id);
                userService.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete User", notes = "Method to delete a User")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User deleted"),
            @ApiResponse(code = 404, message = "User not deleted"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id){
        try{
            Optional<User> user = userService.getById(id);
            if(!user.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else {
                userService.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
