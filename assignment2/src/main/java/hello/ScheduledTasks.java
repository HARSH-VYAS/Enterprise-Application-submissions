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
    List<Moderator> mod;
    List<Polls> po;
    Date sysdate,expdate;
    static int flag =0;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime(){
         for(Polls p : pr.findAll()) {
               try {
                 expdate= formater.parse(p.getExpired_at());
                 sysdate= formater.parse(formater.format(new Date()));
                   mod=mr.findAll();
                 for(int i =0 ; i<mr.findAll().size();i++)
                 {
                     for (int j=0;j<mod.get(i).getPollslist().size();j++) {
                         if (p.getId().equals(mod.get(i).getPollslist().get(j))){

                             Moderator moderator = mod.get(i);
                             if(sysdate.after(expdate) && p.getFlag()==0) {
                                 choice=p.getChoice();
                                 result=p.getResult();
                                 p.setFlag(1);
                                 pr.save(p);
                                 System.out.println("Flag is :" + flag +"\n\n");
                                 sp.callProducer(choice,result,moderator.getEmail());


                             }
                         }
                     }
                 }

               } catch (ParseException e) {
                 e.printStackTrace();
             }


         }

    }
}
