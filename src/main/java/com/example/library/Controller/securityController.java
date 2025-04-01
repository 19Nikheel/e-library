package com.example.library.Controller;


import com.example.library.Entity.JwtRequest;
import com.example.library.Entity.JwtResponce;
import com.example.library.Entity.Userinfo;
import com.example.library.Requestuserinfo.Signpacket;
import com.example.library.Security.JwtHelper;
import com.example.library.Service.Userrepo;
import com.example.library.config.CustomUserDetailService;
import jakarta.servlet.ServletOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class securityController {

    @Autowired
    private Userrepo userrepo;



    @Autowired
    private CustomUserDetailService userDetailsService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final String SECRET_ADMIN_KEY = "123";
    @PostMapping("/signup")
    public ResponseEntity<?>  loadData(@RequestBody Signpacket user){

        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong sign up credentials");
        }
        if (userrepo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        if (userrepo.findByEmailid(user.getEmailid()).isPresent()) {
            return ResponseEntity.badRequest().body("Email id already exists!");
        }
        if ("ADMIN".equalsIgnoreCase(user.getRole()) && !SECRET_ADMIN_KEY.equals(user.getKey())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Admin Key");
        }

        Userinfo uload=new Userinfo();
        uload.setEmailid(user.getEmailid());
        uload.setFirstname(user.getFirstname());
        uload.setLastname(user.getLastname());
        uload.setUsername(user.getUsername());
        uload.setRole(user.getRole());
        uload.setPassword(passwordEncoder.encode(user.getPassword()));
       userrepo.save(uload);
        return ResponseEntity.ok().body("Sign up successfully");
    }


        private Logger logger= LoggerFactory.getLogger(securityController.class);

        @PostMapping("/login")
        private ResponseEntity<JwtResponce> login(@RequestBody JwtRequest jwtRequest){
            this.doAuthenticate(jwtRequest.getUsername(),jwtRequest.getPassword());
            UserDetails userDetails=userDetailsService.loadUserByUsername(jwtRequest.getUsername());
            String token=this.jwtHelper.generateToken(userDetails);
            JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(userDetails.getUsername()).build();
            return new ResponseEntity<>(responce,HttpStatus.OK);
        }

        private void doAuthenticate(String email,String password){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(email,password);
            try {
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            }catch(BadCredentialsException e){
                throw new BadCredentialsException("Invalid username and password");
            }

        }
        @ExceptionHandler(BadCredentialsException.class)
        public String exceptionHandler(){
            return "Credentials Invalid !!";
        }

}
