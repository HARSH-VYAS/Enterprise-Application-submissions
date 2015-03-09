package hello;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableWebMvcSecurity
@RequestMapping(value="/api/v1")
@RestController
public class ModeratorController extends WebSecurityConfigurerAdapter {
	
	
	Moderator mod = new Moderator();
	Polls poll = new Polls();
	
	ArrayList <Moderator> stringlist = new ArrayList<Moderator>();
	ArrayList <Polls> stringlist1 = new ArrayList<Polls>();

	private static final AtomicLong counter = new AtomicLong(123455);
	
     int [] tempresult = new int[2];
     int [] result = new int[2];
     int [] finalresult= new int[2];
	 String [] choice = new String[2];
    
	 protected void configure(HttpSecurity http) throws Exception {
                http
                .httpBasic().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/v1/").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/moderators").permitAll()
                .antMatchers("/api/v1/polls/**").permitAll()
                .antMatchers("/api/v1/moderators/**").fullyAuthenticated().anyRequest().hasRole("USER");
            }

         @Autowired
            public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth
                    .inMemoryAuthentication()
                        .withUser("foo").password("bar").roles("USER");
            }
    
    
    
    
	@RequestMapping(value = "/moderators", method = RequestMethod.POST)
	
	public ResponseEntity <Moderator> moderator(@Valid @RequestBody Moderator mod) {
		
		String date = new Date().toString();	
		mod.setCreated_at(date);
		mod.setId((int)counter.incrementAndGet());
		stringlist.add(mod);
		
		return new ResponseEntity<Moderator>(mod,HttpStatus.CREATED);
		
	   }

	@RequestMapping(value = "/moderators/{moderator_id}", method = RequestMethod.GET)
		public ResponseEntity <Moderator> Viewmoderator(@PathVariable int moderator_id) {
	
		int identifier = 0;
		
		System.out.println("size is"+stringlist.size());
		
		for(int i=0;i<stringlist.size();i++)
		{
			if(moderator_id == stringlist.get(i).getId())
			{
				identifier=i;
			}
		}
		
		return new ResponseEntity<Moderator>(stringlist.get(identifier),HttpStatus.OK);
	   
	}
	
	@RequestMapping(value = "/moderators/{id}", method = RequestMethod.PUT)
	 public ResponseEntity <Moderator> updatemoderator(@Valid @RequestBody Moderator mod,@PathVariable int id) {
		
         int identifier = 0;
		
         String email = mod.getEmail();
		 String password= mod.getPassword();
		
		System.out.println("size is"+stringlist.size());
		for(int i=0;i<stringlist.size();i++)
		{
			if(id == stringlist.get(i).getId())
			{
			
				identifier=i;
				stringlist.get(i).setEmail(email);
				stringlist.get(i).setPassword(password);
				
			}
			
		}
		return new ResponseEntity<Moderator>(stringlist.get(identifier),HttpStatus.OK);
	   }
	
    @RequestMapping(value = "/moderators/{moderator_id}/polls", method = RequestMethod.POST)

	public ResponseEntity <Polls> createPoll(@Valid @RequestBody Polls poll,@PathVariable int moderator_id) {
		
    	poll.setId(Integer.toString((int) counter.incrementAndGet(),36));
    	
    	stringlist1.add(poll);
    
    	
		for(int i=0;i<stringlist.size();i++)
		{
			if(moderator_id == stringlist.get(i).getId())
			{
				stringlist.get(i).getPollslist().add(poll);
		
			}
			
		}
	
		return new ResponseEntity<Polls>(poll,HttpStatus.CREATED);
		
	   }
    /*
     * 
     * 
       
        "http://ec2-54-153-100-62.us-west-1.compute.amazonaws.com:8080/api/v1/moderators [Update] Call failed",
       
        
        "http://ec2-54-153-100-62.us-west-1.compute.amazonaws.com:8080/api/v1/polls/{poll_id} [DELETE] Poll Call failed",
        "http://ec2-54-153-100-62.us-west-1.compute.amazonaws.com:8080/api/v1/moderators/{moderator_id}/polls [Create] Preparation for List Call failed",
        "http://ec2-54-153-100-62.us-west-1.compute.amazonaws.com:8080/api/v1/moderators/{moderator_id}/polls [Create] Preparation for List Call failed",
        "http://ec2-54-153-100-62.us-west-1.compute.amazonaws.com:8080/api/v1/polls/{poll_id}?choice=x [PUT] Vote Call failed",
        "http://ec2-54-153-100-62.us-west-1.compute.amazonaws.com:8080/api/v1/moderators [POST] Validation Check Call failed"
    ]
     */
    
    
    
    

	@RequestMapping(value = "/polls/{poll_id}", method = RequestMethod.GET)
		
	    public ResponseEntity <Polls> viewPollsWithoughResult(@PathVariable String poll_id) {
	
		int identifier = 0;
		
		System.out.println("size is"+stringlist1.size());
		
		for(int i=0; i<stringlist1.size(); i++)
		{
			if(poll_id.equals(stringlist1.get(i).getId()))
			{
				identifier=i;
			}
		}
		
		return new ResponseEntity<Polls>(stringlist1.get(identifier),HttpStatus.OK);
	}

	@RequestMapping(value = "/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.GET)
	public ResponseEntity viewPollWithResult(@PathVariable int moderator_id,@PathVariable String poll_id) {

    int identifier = 0;
	System.out.println("size is"+stringlist.size());
	
	for(int i=0;i<stringlist.size();i++)
	{
		if(moderator_id == stringlist.get(i).getId())
		{
		
			for(int j=0;j<stringlist1.size();j++)
			{
				if(poll_id.equals(stringlist1.get(j).getId()))	
				{
					return new ResponseEntity(stringlist.get(i).getPollslist().get(j),HttpStatus.OK);
				}
			}
		}	
	
	}
	
     return new ResponseEntity("View Polls is not sucessfull",HttpStatus.OK);
}

	@RequestMapping(value = "/moderators/{moderator_id}/polls", method = RequestMethod.GET)
	public ResponseEntity listAllPolls(@PathVariable int moderator_id) {

    int identifier = 0;
	System.out.println("size is"+stringlist.size());
	
	for(int i=0;i<stringlist.size();i++)
	{
		if(moderator_id == stringlist.get(i).getId())
		{
		
					return new ResponseEntity(stringlist.get(i).getPollslist(),HttpStatus.OK);
		}	
	}
     return new ResponseEntity("View Polls is not sucessfull",HttpStatus.OK);
}

	
	
	@RequestMapping(value = "/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.DELETE)
	public ResponseEntity deletePoll(@PathVariable int moderator_id,@PathVariable String poll_id) {

    int identifier = 0;
	System.out.println("size is"+stringlist.size());
	
	for(int i=0;i<stringlist.size();i++)
	{
		if(moderator_id == stringlist.get(i).getId())
		{
		
			for(int j=0;j<stringlist1.size();j++)
			{
				if(poll_id.equals(stringlist1.get(j).getId()))	
				{
					stringlist1.remove(j);
					return new ResponseEntity(stringlist.get(i).getPollslist(),HttpStatus.NO_CONTENT);
				}
			}
		}	
	
	}
	
     return new ResponseEntity("Delete Polls is not sucessfull",HttpStatus.OK);
}

	
	
	 @RequestMapping(value = "/polls/{poll_id}", method = RequestMethod.PUT)
	 public ResponseEntity voteAPoll(@PathVariable String poll_id,@RequestParam(value="choice")int choice_index) 
	 {
		 for(int i=0;i<stringlist1.size();i++)
		    {
			  if(poll_id.equals(stringlist1.get(i).getId()))
			  {
			   
				  	if(choice_index == 0)
				  		{
				  		
				  		tempresult=stringlist1.get(i).getResult();
				  		tempresult[choice_index]=tempresult[choice_index]+1;	
				  		stringlist1.get(i).setResult(tempresult);
				  	 	return new ResponseEntity(HttpStatus.NO_CONTENT);
				  		
				  		}
				  	else if(choice_index==1)
		            {
				  		 tempresult=stringlist1.get(i).getResult();
				  		 tempresult[choice_index]=tempresult[choice_index]+1;
						 stringlist1.get(i).setResult(tempresult);
						 return new ResponseEntity(HttpStatus.NO_CONTENT);
		            }
				  	
    		    }
            }
		    	 
           	return new ResponseEntity("Not able to vote",HttpStatus.NO_CONTENT);
		
	   }

     @ExceptionHandler(MethodArgumentNotValidException.class)
     @ResponseBody
     public ResponseEntity handleBadInput(MethodArgumentNotValidException e)
     {
         String errors="";
         for(FieldError obj: e.getBindingResult().getFieldErrors())
             {
                 errors+=obj.getDefaultMessage();
             }    
         return new ResponseEntity(errors,HttpStatus.BAD_REQUEST);
     }
}
