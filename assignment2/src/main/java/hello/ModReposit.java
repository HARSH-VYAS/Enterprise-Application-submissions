package hello;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Adwait on 3/28/2015.
 */
public interface ModReposit extends MongoRepository<Moderator,String> {

    public Moderator findById(int id);

}
