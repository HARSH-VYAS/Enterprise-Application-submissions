package hello;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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

@Repository
@EnableWebMvcSecurity
@RequestMapping(value="/api/v1")
@RestController
public class ModeratorController extends WebSecurityConfigurerAdapter {
	
    @Autowired
    ModReposit modr;

	@Autowired
	PollReposit pollr;

	Moderator mod = new Moderator();
	Polls poll = new Polls();
	Moderator m;
	Polls p;
	ArrayList <Moderator> stringlist = new ArrayList<Moderator>();

	ArrayList<String> strlst = new ArrayList<String>();
	ArrayList<Integer> strlst1 = new ArrayList<Integer>();
	private static final AtomicLong counter = new AtomicLong(123455);
	private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
     int [] tempresult = new int[2];
     int [] result = new int[2];
     int [] finalresult= new int[2];
	 String [] choice = new String[2];
    @Override
	 protected void configure(HttpSecurity http) throws Exception {
                http
                .httpBasic().and()
                .csrf().disable()
                .authorizeRequests()
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
		System.out.println(mod.getName());
		String date = new Date().toString();	
		mod.setCreated_at(formater.format(new Date()));
		mod.setId((int)counter.incrementAndGet());stringlist.add(mod);
		modr.save(mod);
		return new ResponseEntity<Moderator>(mod,HttpStatus.CREATED);
		
	   }

	@RequestMapping(value = "/moderators/{id}", method = RequestMethod.GET)
		public ResponseEntity Viewmoderator(@PathVariable int id) {
	
		//int identifier = 0;
		m = modr.findById(id);
		if(m==null)
		{
			return new ResponseEntity("There is no Moderator of this id",HttpStatus.OK);//stringlist.get(identifier),HttpStatus.OK);

		}
		return new ResponseEntity<Moderator>(m,HttpStatus.OK);//stringlist.get(identifier),HttpStatus.OK);
	   
	}
	
	@RequestMapping(value = "/moderators/{id}", method = RequestMethod.PUT)
	 public ResponseEntity <Moderator> updatemoderator(@Valid @RequestBody Moderator mod,@PathVariable int id) {
		
         int identifier = 0;
         String email = mod.getEmail();
		 String password= mod.getPassword();
		 m = modr.findById(id);
		 m.setEmail(email);
		 m.setPassword(password);
		 modr.save(m);
		return new ResponseEntity(m,HttpStatus.OK);
	   }
	
    @RequestMapping(value = "/moderators/{moderator_id}/polls", method = RequestMethod.POST)
	public ResponseEntity createPoll(@Valid @RequestBody Polls poll,@PathVariable int moderator_id) {
		
    	poll.setId(Integer.toString((int) counter.incrementAndGet(), 36));
		poll.setStarted_at(formater.format(new Date()));
		poll.setExpired_at(formater.format(new Date(new Date().getTime() + (long) 24 * 3600)));
		poll.setFlag(poll.getFlag());
		//strlst1.add(moderator_id);
		//poll.setModeratorList(strlst1);
		m = modr.findById(moderator_id);
		strlst = m.getPollslist();
		pollr.save(poll);
		strlst.add(poll.getId());
		m.setPollslist(strlst);
		modr.save(m);
		return new ResponseEntity(poll,HttpStatus.CREATED);
	}

	@RequestMapping(value = "/polls/{poll_id}", method = RequestMethod.GET)
	public ResponseEntity viewPollsWithoughtResult(@PathVariable String poll_id) {

		p=pollr.findById(poll_id);

		if(p==null)
		{
        	return new ResponseEntity("There is no Poll of this ID",HttpStatus.OK);//stringlist1.get(identifier),HttpStatus.OK);

		}
        	return new ResponseEntity(p,HttpStatus.OK);//stringlist1.get(identifier),HttpStatus.OK);
	}

	@RequestMapping(value = "/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.GET)
	public ResponseEntity viewPollWithResult(@PathVariable int moderator_id,@PathVariable String poll_id) {

		m=modr.findById(moderator_id);
		strlst=m.getPollslist();
		for(int i=0;i<strlst.size();i++) {
			if(strlst.get(i).equals(poll_id)) {
				p = pollr.findById(strlst.get(i));
				return new ResponseEntity(p,HttpStatus.OK);
			}
		}

     return new ResponseEntity("View Polls is not sucessfull",HttpStatus.OK);
}

	@RequestMapping(value = "/moderators/{moderator_id}/polls", method = RequestMethod.GET)
	public ResponseEntity listAllPolls(@PathVariable int moderator_id) {
		ArrayList <Polls> stringlist1 = new ArrayList<Polls>();
		m=modr.findById(moderator_id);
		strlst=m.getPollslist();

	for(int i=0;i<strlst.size();i++)
	{
                    p=pollr.findById(strlst.get(i));
					stringlist1.add(p);

	}
     return new ResponseEntity(stringlist1,HttpStatus.OK);
  }

	@RequestMapping(value = "/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.DELETE)
	public ResponseEntity deletePoll(@PathVariable int moderator_id,@PathVariable String poll_id) {
 		p=pollr.findById(poll_id);
		pollr.delete(p);

		m=modr.findById(moderator_id);
		strlst=m.getPollslist();

 	   for(int i=0;i<strlst.size();i++) {
		   if (strlst.get(i).equals(poll_id)) {
			   strlst.remove(i);

			   m.setPollslist(strlst);
			   modr.save(m);
			   return new ResponseEntity("Deleted a Poll", HttpStatus.NO_CONTENT);
		   }

	   }
		return new ResponseEntity("Delete Polls is not sucessfull",HttpStatus.OK);
}

	 @RequestMapping(value = "/polls/{poll_id}", method = RequestMethod.PUT)
	 public ResponseEntity voteAPoll(@PathVariable String poll_id,@RequestParam(value="choice")int choice_index) 
	 {
		 p=pollr.findById(poll_id);
		 if(choice_index == 0)
		 {

			 tempresult=p.getResult();
			 tempresult[choice_index]=tempresult[choice_index]+1;
			 p.setResult(tempresult);
			 pollr.save(p);
			 return new ResponseEntity(p,HttpStatus.NO_CONTENT);

		 }
		 else if(choice_index==1)
		 {
			 tempresult=p.getResult();
			 tempresult[choice_index]=tempresult[choice_index]+1;
			 p.setResult(tempresult);
			 pollr.save(p);
			 return new ResponseEntity(HttpStatus.NO_CONTENT);
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
