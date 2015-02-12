package hello

import org.springframework.context.annotation.{Configuration, ComponentScan}
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.ResponseBody; 
import org.springframework.stereotype.Controller; 

/**
 * This config class will trigger Spring @annotation scanning and auto configure Spring context.
 *
 * @author harsh
 * @since 1.0
 */
 
@Controller
@Configuration
@EnableAutoConfiguration
@ComponentScan
class HelloConfig {

@RequestMapping(Array("/"))
@ResponseBody
def home(): String = "Hello World! Hey how are you......???"

}