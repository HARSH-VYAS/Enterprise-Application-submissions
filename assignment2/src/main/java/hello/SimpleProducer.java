package hello;

/**
 * Created by harsh on 4/9/2015.
 */
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Properties;

//@Repository
public class SimpleProducer {
  //  @Autowired
    //ModReposit mr;

    private static Producer<Integer, String> producer;
    private final Properties properties = new Properties();

    public SimpleProducer() {
        properties.put("metadata.broker.list", "54.149.84.25:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        producer = new Producer<>(new ProducerConfig(properties));
    }

    public void  callProducer(String [] choice, int [] result,String email)
    {
          new SimpleProducer();
     //   for (Integer i : mod) {
            String topic = "cmpe273-topic";
            String msg = "harshvyas1214@gmail.com" + ":010095410:Poll Result [" + choice[0] + "=" + result[0] + "," + choice[1] + "=" + result[1] + "]";
            System.out.println(msg + topic);
            KeyedMessage<Integer, String> data = new KeyedMessage<>(topic, msg);
            producer.send(data);
            producer.close();
            // System.out.println(msg+topic);
       // }
    }
}