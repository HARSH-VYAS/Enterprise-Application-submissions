package edu.sjsu.cmpe273.lab2;

import io.grpc.ChannelImpl;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;

import java.lang.String;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PollClient {
  private static final Logger logger = Logger.getLogger(PollClient.class.getName());

  private final ChannelImpl channel;
  private final PollServiceGrpc.PollServiceBlockingStub blockingStub;

  public PollClient(String host, int port) {
    channel =
        NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT)
            .build();
    blockingStub = PollServiceGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTerminated(5, TimeUnit.SECONDS);
  }

  public void poll(String moderatorId,String question, String createdAt, String expireAt, String [] choice) {
    try {


        logger.info("Server Response: Poll Id : " + moderatorId);
        logger.info("Server Response: Question : " + question);
        logger.info("Server Response: CreatedAt : " + createdAt);
        logger.info("Server Response: ExpireAt : " + expireAt);
        logger.info("Server Response: Choice 0 : " + choice[0]);
        logger.info("Server Response: Choice 1 :"+ choice[1]);

      PollRequest request = PollRequest.newBuilder()
       .setModeratorId(moderatorId)
       .setQuestion(question)
       .setStartedAt(createdAt)
       .setExpiredAt(expireAt)
       .addChoice(choice[0])
       .addChoice(choice[1]).build();

      PollResponse response = blockingStub.createPoll(request);
      logger.info("Server Response: Poll Id : " + response.getId());
    } catch (RuntimeException e) {
      logger.log(Level.WARNING, "RPC failed", e);
      return;
    }
  }

  public static void main(String[] args) throws Exception {

      String moderatorId = "123456";
      String question = "What type of smartphone do you have?";
      String startedAt = "2015-02-23T13:00:00.000Z";
      String expireAt = "2015-02-24T13:00:00.000Z";
      String [] choice  = {"Android","iphone"};

      PollClient client = new PollClient("localhost", 50051);
    try {
      	client.poll(moderatorId,question,startedAt,expireAt,choice);
          } finally {
      client.shutdown();
    }
  }
}
