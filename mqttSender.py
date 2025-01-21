import paho.mqtt.client as mqtt 
import RPi.GPIO as GPIO 
import time 
import dht11 
import datetime 

# MQTT Setup 
broker = "URL" 
port = 8883 
topic = "sensor/measurement"   

GPIO.setmode(GPIO.BOARD) 

pin = 7 
instance = dht11.DHT11(pin=pin) 

client = mqtt.Client() 
client.tls_set() 
client.username_pw_set("username", "password") 
client.connect(broker, port, 60) 

try: 
    while True: 
        result = instance.read() 
        temperature = result.temperature 
        humidity = result.humidity 
        timestamp = datetime.datetime.now().isoformat() 
        sensorId = 3 

        measurement_data = { 
            "timestamp": timestamp, 
            "temperature": temperature, 
            "humidity": humidity, 
            "sensorId": sensorId 
            } 

        if result.is_valid(): 
            payload = str(measurement_data) 
            client.publish(topic, payload, qos=1)  
            print(f"succesfully recorded: {measurement_data}") 
        else: 
            print(f"invalid data") 

        time.sleep(5) 
except KeyboardInterrupt: 
    print("end, cleaning up") 
    GPIO.cleanup()