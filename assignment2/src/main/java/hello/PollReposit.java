package hello;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by harsh on 3/28/2015.
 */
public interface PollReposit extends MongoRepository<Polls,String> {
    public Polls findById(String id);

}
