package hello;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public class ScheduledTasks {

    private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    @Autowired
    PollReposit pr;

    @Autowired
    ModReposit mr;

    SimpleProducer sp = new SimpleProducer();
    String [] choice = new String[10];
    int [] result = new int [10];
    ArrayList<Polls> poll = new ArrayList<Polls>();
    ArrayList<Integer> mod = new ArrayList<Integer>();
    List<Polls> po;
    Date sysdate,expdate;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime(){
         for(Polls p : pr.findAll()) {
               try {
                 expdate= formater.parse(p.getExpired_at());
                 sysdate= formater.parse(formater.format(new Date()));
               } catch (ParseException e) {
                 e.printStackTrace();
             }

             if(sysdate.after(expdate)) {

                 choice=p.getChoice();
                 result=p.getResult();

               //  mod=p.getModeratorList();

                 sp.callProducer(choice,result);

             }
         }

    }
}
