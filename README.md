# Rocket Elevators Java Controller 

![alt text](https://github.com/DaveVaval/Rocket-Elevators-Ruby-Controller/blob/Main/img/R3.png)

Welcome to the Java controller for Rocket Elevators! 

- Java is a simple language to work with. It is so close to C# there is barely any difference!


An instance of Battery is created and on initialization, that battery will create it's own columns and elevators. When a en elevator is called, the column will send the best elevator to the user based on the different attributes of the elevators and the origin of the request.

In order to see it in action, uncomment the Battery and one of the scenarios in the main function:

```
// Battery battery = new Battery(1, "online", 4, 60, 6, 5);

// //-------------------------// Scenario 4 //------------------------------
        // // A1
        // battery.columnsList.get(0).elevatorsList.get(0).currentFloor = -4;

        // // A2
        // battery.columnsList.get(0).elevatorsList.get(1).currentFloor = 1;

        // //A3
        // battery.columnsList.get(0).elevatorsList.get(2).currentFloor = -3;
        // battery.columnsList.get(0).elevatorsList.get(2).direction = "down";
        // battery.columnsList.get(0).elevatorsList.get(2).floorRequestList.add(-5);

        // // A4
        // battery.columnsList.get(0).elevatorsList.get(3).currentFloor = -6;
        // battery.columnsList.get(0).elevatorsList.get(3).direction = "up";
        // battery.columnsList.get(0).elevatorsList.get(3).floorRequestList.add(1);

        // // A5
        // battery.columnsList.get(0).elevatorsList.get(4).currentFloor = -1;
        // battery.columnsList.get(0).elevatorsList.get(4).direction = "down";
        // battery.columnsList.get(0).elevatorsList.get(4).floorRequestList.add(-6);

        // // User at Basement 3 want's to go to floor 1, Elevator 4 should be sent
        // System.out.println("User is at SS3 and wants to go to the lobby");
        // System.out.println("He presses on the pannel");
        // battery.columnsList.get(0).requestElevator(-3, "up");
```