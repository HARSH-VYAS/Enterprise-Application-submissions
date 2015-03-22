package edu.sjsu.cmpe273.lab2;

import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

import java.util.*;

import java.util.logging.Logger;

public class PollServer {
  private static final Logger logger = Logger.getLogger(PollServer.class.getName());

  private int port = 50051;
  private ServerImpl server;
   
 Random randomGenerator = new Random();
 int i =  randomGenerator.nextInt(100);
String rn = Integer.toString(i);
    private void start() throws Exception {
    server = NettyServerBuilder.forPort(port)
        .addService(PollServiceGrpc.bindService(new PollServiceImpl()))
        .build().start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        PollServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

   public static void main(String[] args) throws Exception {
    final PollServer server = new PollServer();
    server.start();
  }

  private class PollServiceImpl implements PollServiceGrpc.PollService {

      private String moderatorId;
    @Override
    public void createPoll(PollRequest req, StreamObserver<PollResponse> responseObserver) {

 	

	PollResponse reply;


	if(req.getModeratorId().equals(""))
	{
		
		reply=PollResponse.newBuilder().setId("id should not be equal to null").build();
	}

	else if(req.getQuestion().equals(null))
	{
		
		reply=PollResponse.newBuilder().setId("Question should not be equal to null").build();
	}

	else if(req.getStartedAt().equals(null))
	{
		
		reply=PollResponse.newBuilder().setId("StartedAt should not be equal to null").build();
	}

	else if(req.getExpiredAt().equals(null))
	{
		
		reply=PollResponse.newBuilder().setId("ExpiredAt should not be equal to null").build();
	}

	else
	{
		reply=PollResponse.newBuilder().setId(rn).build();
	}

	responseObserver.onValue(reply);
        responseObserver.onCompleted();
    }
  }
}
