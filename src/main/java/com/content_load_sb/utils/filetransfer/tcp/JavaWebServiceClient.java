package com.content_load_sb.utils.filetransfer.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JavaWebServiceClient {
    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    private static Logger logger = LoggerFactory.getLogger(JavaWebServiceClient.class);

    public static void main(String[] args) throws Exception {
     JavaWebServiceClient helloClient = new JavaWebServiceClient();
        List<Transport> transports = new ArrayList<>();
        ListenableFuture<StompSession> f = helloClient.connect();
        StompSession stompSession = f.get();

        logger.info("Subscribing to greeting topic using session " + stompSession);
        helloClient.subscribeGreetings(stompSession);

        logger.info("Sending hello message" + stompSession);
        helloClient.sendHello(stompSession);
        Thread.sleep(60000);
    }

    public ListenableFuture<StompSession> connect() {
        List<Transport> transports = new ArrayList<>();

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024);
        container.setDefaultMaxTextMessageBufferSize(1024 * 1024);
        transports.add(new WebSocketTransport(new StandardWebSocketClient(container)));

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);

        String url = "ws://127.0.0.1:8092/guestbook";
        return stompClient.connect(url, headers, new Handler(), "localhost", 8092);
    }

    public void subscribeGreetings(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/topic/entries", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                logger.info("donus saglandı");
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonValue = new String(((byte[]) o));


                logger.info("Received greeting " + new String((byte[]) o));
            }
        });
    }

    public void sendHello(StompSession stompSession) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(stompSession.getSessionId());
            stompSession.send("/app/guestbook", json.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private class Handler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Now connected");
        }
    }

}
