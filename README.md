![on-track logo](https://i.imgur.com/yW6pl11.png)

## About
This software is responsible simulating and managing various trains around a track circuit in real time.

## Includes modules
### Central Traffic Control
The main command center of the platform. Responsible for creating, dispatching, scheduling, and routing trains.
### Track Controller
The wayside modules to be installed on the track itself. Controlled by PLC logic.
### Train Controller
The controller aboard the physical train. Controls the power and engine.
### Track Model
The representation of the track circuit itself. The format is a .csv file that is loaded upon initialization.
### Train Model
The representation of the train itself. Calculates the physics of the moving train.

#### Developed for use by the PAAC
