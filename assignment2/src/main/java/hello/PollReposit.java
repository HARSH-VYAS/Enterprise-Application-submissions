package hello;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by harsh on 3/28/2015.
 */
public interface PollReposit extends MongoRepository<Polls,String> {
    public Polls findById(String id);
    public List<Polls> findAll();

}
