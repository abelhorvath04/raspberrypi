package demo.group9.mqtt; 
 
import org.eclipse.paho.client.mqttv3.*; 
import org.springframework.http.HttpEntity; 
import org.springframework.http.HttpHeaders; 
import org.springframework.http.ResponseEntity; 
import org.springframework.stereotype.Service; 
import org.springframework.web.client.RestTemplate; 
 
@Service 
public class MqttService { 
 
    private static final String BROKER_URL = "ssl://URL:8883"; 
    private static final String CLIENT_ID = "SpringBootClient"; 
    private static final String USERNAME = "username"; 
    private static final String PASSWORD = "password"; 
    private static final String TOPIC = "sensor/measurement"; 
    private static final String REST_ENDPOINT = "http://localhost:8080/measurements"; 
 
    private final RestTemplate restTemplate; 
 
    public MqttService(RestTemplate restTemplate) { 
        this.restTemplate = restTemplate; 
        connectToMqttBroker(); 
    } 

    private void connectToMqttBroker() { 
        try { 
            MqttClient mqttClient = new MqttClient(BROKER_URL, CLIENT_ID); 
 
            MqttConnectOptions options = new MqttConnectOptions(); 
            options.setUserName(USERNAME); 
            options.setPassword(PASSWORD.toCharArray()); 
            options.setCleanSession(true); 
 
            mqttClient.setCallback(new MqttCallback() { 
                @Override 
                public void connectionLost(Throwable cause) { 
                    System.err.println("Connection lost: " + cause.getMessage()); 
                } 
 
                @Override 
                public void messageArrived(String topic, MqttMessage message) { 
                    handleIncomingMessage(new String(message.getPayload())); 
                } 
 
                @Override 
                public void deliveryComplete(IMqttDeliveryToken token) { 
                    // Not used for subscribing clients 
                } 
            }); 
 
            mqttClient.connect(options); 
            mqttClient.subscribe(TOPIC); 
            System.out.println("Connected to MQTT broker and subscribed to topic: " + TOPIC); 
 
        } catch (Exception e) { 
            e.printStackTrace(); 
            throw new RuntimeException("Failed to connect to MQTT broker", e); 
        } 
    } 
 
    private void handleIncomingMessage(String payload) { 
        try { 
            String normalizedPayload = payload.replace("'", "\""); 
 
            HttpHeaders headers = new HttpHeaders(); 
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON); 
 
            HttpEntity<String> request = new HttpEntity<>(normalizedPayload, headers); 
 
            ResponseEntity<String> response = restTemplate.postForEntity(REST_ENDPOINT, request, String.class); 
            System.out.println("Data sent to REST endpoint. Response: " + response.getStatusCode()); 
        } catch (Exception e) { 
            e.printStackTrace(); 
            System.err.println("Failed to send MQTT message to REST endpoint: " + payload); 
        } 
    } 
}