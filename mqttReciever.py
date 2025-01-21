import paho.mqtt.client as mqtt 
import time 
import json 
import board 
import busio 
import adafruit_character_lcd.character_lcd_i2c as character_lcd 

lcd_columns = 16 
lcd_rows = 2 
i2c = busio.I2C(board.SCL, board.SDA) 
lcd = character_lcd.Character_LCD_I2C(i2c, lcd_columns, lcd_rows, address=0x21) 

broker = "URL" 
port = 8883 
topic = "sensor/measurement" 

def on_connect(client, uesrdata, flags, rc): 
    print(f"connected {rc}") 
    client.subscribe(topic) 

def on_message(client, userdata, msg): 
    try: 
        payload = json.loads(msg.payload.decode().replace("'", '"')) 
        temperature = payload.get("temperature", "N/A") 
        humidity = payload.get("humidity", "N/A") 

        lcd.clear() 
        lcd.message = f"Temp {temperature}\nHum: {humidity}" 
        print(f"message on lcd: temp={temperature}, hum={humidity}") 
    except Exception as e: 
        print (f"error: {e}") 

lcd.backlight = True 
lcd.message = "hello" 
time.sleep(2) 
lcd.message = "waiting for data..." 

client = mqtt.Client() 
client.tls_set() 
client.username_pw_set("username", "password") 
client.on_connect = on_connect 
client.on_message = on_message 

try: 
    client.connect(broker, port, 60) 
    client.loop_forever()() 
except KeyboardInterrupt: 
    print("disconnected, cleaning up") 
    lcd.clear() 
    lcd.message = "bye-bye" 
    time.sleep(2) 
    lcd.backlight = False 
    client.disconnect()  