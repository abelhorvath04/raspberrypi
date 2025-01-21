# Joy-Pi MQTT Sensor Project

This project demonstrates a complete IoT workflow using a **Joy-Pi** and a **DHT11 sensor**. It measures temperature and humidity, sends the data to a cloud-based MQTT broker, and processes the information using Python and Java programs.

## Files

1. **`mqttSender.py`**  
   - Reads temperature and humidity data from the DHT11 sensor.  
   - Publishes the sensor measurements to an MQTT broker on the topic `sensor/measurement`.  

2. **`mqttReceiver.py`**  
   - Subscribes to the MQTT broker on the `sensor/measurement` topic.  
   - Retrieves the published sensor data and displays it on the Joy-Pi's LCD screen.  

3. **`mqttService.java`**  
   - A Spring Boot service that connects to the MQTT broker and subscribes to the same topic.  
   - Processes the incoming sensor data and forwards it to a backend microservice using a REST API for storage.

## Data Flow

1. The **`mqttSender.py`** script captures data from the DHT11 sensor and sends it to the MQTT broker.  
2. The **`mqttReceiver.py`** script retrieves this data from the broker and displays it on the LCD.  
3. The **`mqttService.java`** processes the data and sends it to a backend service for database storage.

## Example Data Format

The sensor data is sent in the following JSON format:
```json
{
  "timestamp": "2025-01-21T12:00:00Z",
  "temperature": 25.0,
  "humidity": 60.0,
  "sensorId": 3
}
```

## Prerequisites

- **Hardware**: Joy-Pi, DHT11 sensor, I2C-enabled LCD screen  
- **Software**: Python 3, Java 17+, MQTT broker (e.g., Mosquitto)  
- Install required libraries as listed in the respective program files.

## Usage

1. Run `mqttSender.py` to start publishing sensor data.  
2. Run `mqttReceiver.py` to display the data on the LCD.  
3. Deploy and start `mqttService.java` to process and store the data in the backend.

This setup provides a scalable IoT solution with data visualization and backend integration.
