import java.util.*;


// Battery Class
class Battery
{
    public int ID;
    public String status;
    public int amountOfFloors;
    public int amountOfColumns;
    public int amountOfBasements;
    public int columnID;
    public int floorRequestButtonID;
    public ArrayList<Column> columnsList;
    public ArrayList<FloorRequestButton> floorRequestButtonsList;
    
    public Battery(int id, String status,int amountOfColumns, int amountOfFloors, int amountOfBasements, int amountOfElevatorPerColumn) // this is the battery constructor
    {
        this.ID = id;
        this.amountOfColumns = amountOfColumns;
        this.status = status;
        this.amountOfFloors = amountOfFloors;
        this.amountOfBasements = amountOfBasements;
        this.columnsList = new ArrayList<Column>();
        this.floorRequestButtonsList = new ArrayList<FloorRequestButton>();
        this.columnID = 1;
        this.floorRequestButtonID = 1;

        if (this.amountOfBasements > 0){
            createBasementFloorRequestButtons(amountOfBasements);
            createBasementColumn(this.amountOfBasements, amountOfElevatorPerColumn);
            amountOfColumns--;
        }
        createFloorRequestButtons(amountOfFloors);
        createColumns(amountOfColumns, this.amountOfFloors, this.amountOfBasements, amountOfElevatorPerColumn);
    }

    public void createBasementColumn(int amountOfBasements, int amountOfElevatorPerColumn){
        ArrayList<Integer> servedFloors = new ArrayList<Integer>();
        int floor = -1;

        for (int i = 0; i < amountOfBasements; i++){
            servedFloors.add(floor);
            floor--;
        }
        columnsList.add(new Column(columnID, "online", amountOfBasements, amountOfElevatorPerColumn, servedFloors, true));
        columnID++;
    }

    public void createColumns(int amountOfColumns, int amountOfFloors, int amountOfBasements, int amountOfElevatorPerColumn){
        int amountOfFloorsPerColumn = (int)Math.ceil(Double.valueOf(amountOfFloors / amountOfColumns));
        int floor = 1;

        for (int i = 1; i <= amountOfColumns; i++){ // i = 1 fixed the issue of the amount of columns
            ArrayList<Integer> servedFloors = new ArrayList<Integer>(); 
            for (int n = 0; n < amountOfFloorsPerColumn; n++){
                if(floor <= amountOfFloors){
                    servedFloors.add(floor);
                    floor++;
                }
            }
            columnsList.add(new Column(columnID, "online", amountOfFloors, amountOfElevatorPerColumn, servedFloors, false));
            columnID++;
        }
    }

    public void createFloorRequestButtons(int amountOfFloors){
        int buttonFloor = 1;
        for (int i = 1; i <= amountOfFloors; i++){
            floorRequestButtonsList.add(new FloorRequestButton(floorRequestButtonID, "off", buttonFloor));
            floorRequestButtonID++;
            buttonFloor++;
        }
    }

    public void createBasementFloorRequestButtons(int amountOfBasements){
        int buttonFloor = -1;
        for (int i = 1; i <= amountOfBasements; i++){
            floorRequestButtonsList.add(new FloorRequestButton(floorRequestButtonID, "off", buttonFloor));
            buttonFloor--;
            floorRequestButtonID++;
        }
    }

    public Column findBestColumn(int requestedFloor){
        Column col = null;
        for (Column column : columnsList){
            if(column.servedFloors.contains(requestedFloor)){
                col = column;
            }
        }
        return col;
    }

    // This function will return the best elevator from the best column to the user
    public void assignElevator(int requestedFloor, String direction){ 
        Column column = findBestColumn(requestedFloor); // return?
        Elevator elevator = column.findBestElevator(1, direction); // return?
        elevator.floorRequestList.add(requestedFloor);
        elevator.sortFloorList();
        elevator.move();
        elevator.openDoors();
        System.out.println(".........");
        System.out.println("Elevator: " + elevator.ID + " from column: " + column.ID + " is sent to lobby");
        System.out.println("He enters the elevator");
        System.out.println(".........");
        System.out.println("Elevator reached floor: " + elevator.currentFloor);
        System.out.println("He gets out...");
    }
}


// Column class
class Column{
    public int ID;
    public String status;
    public int amountOfFloors;
    public int amountOfElevators;
    public Boolean isBasement;
    public ArrayList<Integer> servedFloors;
    public ArrayList<Elevator> elevatorsList;
    public ArrayList<CallButton> callButtonsList;

    public Column(int id, String status, int amountOfFloors, int amountOfElevators, ArrayList<Integer> servedFloors, Boolean isBasement){ // This is the column constructor
        
        this.ID = id;
        this.status = status;
        this.amountOfFloors = amountOfFloors;
        this.amountOfElevators = amountOfElevators;
        this.isBasement = isBasement;
        this.elevatorsList = new ArrayList<Elevator>();
        this.callButtonsList = new ArrayList<CallButton>();
        this.servedFloors = servedFloors;
        createElevators(amountOfFloors, amountOfElevators);
        createCallButtons(amountOfFloors, isBasement);

    }


    public void createElevators(int amountOfFloors, int amountOfElevators){
        int elevatorID = 1;
        for(int i = 0; i < amountOfElevators; i++){
            elevatorsList.add(new Elevator(elevatorID, "idle", amountOfFloors, 1));
            elevatorID++;
        }
    }

    public void createCallButtons(int amountOfFloors, Boolean isBasement){
        int callButtonID = 1;
        if (isBasement == true){
            int buttonFloor = -1;
            for(int i = 0; i < amountOfFloors; i++){
                callButtonsList.add(new CallButton(callButtonID, "off", buttonFloor, "up"));
                buttonFloor--;
                callButtonID++;
            }
        } 
        else{
            int buttonFloor = 1;
            for(int i = 0; i < amountOfFloors; i++){
                callButtonsList.add(new CallButton(callButtonID, "off", buttonFloor, "down"));
                buttonFloor++;
                callButtonID++;
            }
        }
    }

    // This is the function that will be called when a user wants to go back to the lobby from any given floor
    public void requestElevator(int userPosition, String direction){
        Elevator elevator = findBestElevator(userPosition, direction); // return?
        elevator.floorRequestList.add(1);
        elevator.sortFloorList();
        elevator.move();
        elevator.openDoors();
        System.out.println(".........");
        System.out.println("Elevator: " + elevator.ID + " from column: " + this.ID + " is sent to floor: " + userPosition);
        System.out.println("He enters the elevator");
        System.out.println(".........");
        System.out.println("Elevator reached floor: " + elevator.currentFloor);
        System.out.println("He gets out...");
    }

    // This function in conjuction wwith checkElevator will return the best elevator
    public Elevator findBestElevator(int requestedFloor, String requestedDirection){ 
        HashMap<String, Object> bestElevatorInfo = new HashMap<String, Object>();
        bestElevatorInfo.put("bestElevator", null);
        bestElevatorInfo.put("bestScore", 6);
        bestElevatorInfo.put("referenceGap", Integer.MAX_VALUE);
            
        if(requestedFloor == 1){
            for(Elevator elevator : elevatorsList){
                if(1 == elevator.currentFloor && elevator.status == "stopped"){
                    bestElevatorInfo = checkElevator(1, elevator, requestedFloor, bestElevatorInfo);
                }
                else if(1 == elevator.currentFloor && elevator.status == "idle"){
                    bestElevatorInfo = checkElevator(2, elevator, requestedFloor, bestElevatorInfo);
                }
                else if(1 > elevator.currentFloor && elevator.direction == "up"){
                    bestElevatorInfo = checkElevator(3, elevator, requestedFloor, bestElevatorInfo);
                }
                else if(1 < elevator.currentFloor && elevator.direction == "down"){
                    bestElevatorInfo = checkElevator(3, elevator, requestedFloor, bestElevatorInfo);
                }
                else if(elevator.status == "idle"){
                    bestElevatorInfo = checkElevator(4, elevator, requestedFloor, bestElevatorInfo);
                }
                else{
                    bestElevatorInfo = checkElevator(5, elevator, requestedFloor, bestElevatorInfo);
                }
            }
        }
        else{
            for(Elevator elevator : elevatorsList){
                if(requestedFloor == elevator.currentFloor && elevator.status == "idle" && requestedDirection == elevator.direction){
                    bestElevatorInfo = checkElevator(1, elevator, requestedFloor, bestElevatorInfo);
                }
                else if(requestedFloor > elevator.currentFloor  && elevator.direction == "up" && requestedDirection == elevator.direction){
                    bestElevatorInfo = checkElevator(2, elevator, requestedFloor, bestElevatorInfo);
                }
                else if(requestedFloor < elevator.currentFloor  && elevator.direction == "down" && requestedDirection == elevator.direction){
                    bestElevatorInfo = checkElevator(2, elevator, requestedFloor, bestElevatorInfo);
                }
                else if(elevator.status == "stopped"){
                    bestElevatorInfo = checkElevator(4, elevator, requestedFloor, bestElevatorInfo);
                }
                else{
                    bestElevatorInfo = checkElevator(5, elevator, requestedFloor, bestElevatorInfo);
                }
            }
        }
        return (Elevator)bestElevatorInfo.get("bestElevator");
    }

    public HashMap<String, Object> checkElevator(int baseScore, Elevator elevator, int floor, HashMap<String, Object> bestElevatorInfo){
        if(baseScore < (Integer)bestElevatorInfo.get("bestScore")){
            bestElevatorInfo.put("bestScore", baseScore);
            bestElevatorInfo.put("bestElevator", elevator);
            bestElevatorInfo.put("referenceGap", Math.abs(elevator.currentFloor - floor));
            
        }
        return bestElevatorInfo;
    }
}



// Elevator class
class Elevator{
    public int ID;
    public String status;
    public int amountOfFloors;
    public String direction;
    public int currentFloor;
    public Door door;
    public ArrayList<Integer> floorRequestList; 

    public Elevator(int id, String status, int amountOfFloors, int currentFloor){ // Elevator Constructor
        this.ID = id;
        this.status = status;
        this.amountOfFloors = amountOfFloors;
        this.direction = null;
        this.currentFloor = currentFloor;
        this.door = new Door(id, "closed");
        this.floorRequestList = new ArrayList<Integer>();
    }

    // This function will make the elevator move to any given floor
    public void move(){
        while(floorRequestList.size() != 0){
            int destination = floorRequestList.get(0);
            status = "moving";
            if(currentFloor < destination){
                direction = "up";
                while(currentFloor < destination){
                    currentFloor++;
                }
            }
            else if(currentFloor > destination){
                direction = "down";
                while(currentFloor > destination){
                    currentFloor--;
                }
            }
            status = "idle";
            floorRequestList.remove(0);
        }
    }

    public void sortFloorList(){
       if(direction == "up"){
           Collections.sort(floorRequestList);
       }
       else{
           Collections.reverse(floorRequestList);
       }
    }

    public void openDoors(){
        door.status = "open";
        door.status = "closed";
    }
}



// Call Button class
class CallButton{
    public int ID;
    public String status;
    public int floor;
    public String direction;

    public CallButton(int id, String status, int floor, String direction){
        this.ID = id;
        this.status = status;
        this.floor = floor;
        this.direction = direction;
    }
}


// Floor request button class
class FloorRequestButton{
    public int ID;
    public String status;
    public int floor;

    public FloorRequestButton(int id, String status, int floor){
        this.ID = id;
        this.status = status;
        this.floor = floor;
    }
}


// Door class
class Door {
    int ID;
    String status;

    public Door(int id, String status){
        this.ID = id;
        this.status = status;
    }
}



// Main Program
public class commercial_controller {
    
    public static void main(String[] args) {
        System.out.println("-------------------------// TESTING //-----------------------------");
        // Battery battery = new Battery(1, "online", 4, 60, 6, 5);
        
        // //--------------// SCENARIO 1 //-----------------------
        // // B1
        // battery.columnsList.get(1).elevatorsList.get(0).currentFloor = 20;
        // battery.columnsList.get(1).elevatorsList.get(0).direction = "down";
        // battery.columnsList.get(1).elevatorsList.get(0).floorRequestList.add(5);
    
        // // B2
        // battery.columnsList.get(1).elevatorsList.get(1).currentFloor = 3;
        // battery.columnsList.get(1).elevatorsList.get(1).direction = "up";
        // battery.columnsList.get(1).elevatorsList.get(1).floorRequestList.add(15);
    
        // // B3
        // battery.columnsList.get(1).elevatorsList.get(2).currentFloor = 13;
        // battery.columnsList.get(1).elevatorsList.get(2).direction = "down";
        // battery.columnsList.get(1).elevatorsList.get(2).floorRequestList.add(1);
    
        // // B4
        // battery.columnsList.get(1).elevatorsList.get(3).currentFloor = 15;
        // battery.columnsList.get(1).elevatorsList.get(3).direction = "down";
        // battery.columnsList.get(1).elevatorsList.get(3).floorRequestList.add(2);
    
        // // B5
        // battery.columnsList.get(1).elevatorsList.get(4).currentFloor = 6;
        // battery.columnsList.get(1).elevatorsList.get(4).direction = "down";
        // battery.columnsList.get(1).elevatorsList.get(4).floorRequestList.add(1);
        // battery.columnsList.get(1).elevatorsList.get(4).move();
    
        //  // User at lobby want's to go to floor 20, Elevator 5 should be sent
        // System.out.println("User is at the lobby and wants to go to floor 20");
        // System.out.println("He enters 20 on the pannel");
        // battery.assignElevator(20, "up");

        
        // //--------------------------------------------// Scenario 2 //-----------------------------------------------------
        // // C1
        // battery.columnsList.get(2).elevatorsList.get(0).currentFloor = 1;
        // battery.columnsList.get(2).elevatorsList.get(0).direction = "up";
        // battery.columnsList.get(2).elevatorsList.get(0).floorRequestList.add(21);

        // // C2
        // battery.columnsList.get(2).elevatorsList.get(1).currentFloor = 23;
        // battery.columnsList.get(2).elevatorsList.get(1).direction = "up";
        // battery.columnsList.get(2).elevatorsList.get(1).floorRequestList.add(28);

        // // C3
        // battery.columnsList.get(2).elevatorsList.get(2).currentFloor = 33;
        // battery.columnsList.get(2).elevatorsList.get(2).direction = "down";
        // battery.columnsList.get(2).elevatorsList.get(2).floorRequestList.add(1);

        // // C4
        // battery.columnsList.get(2).elevatorsList.get(3).currentFloor = 40;
        // battery.columnsList.get(2).elevatorsList.get(3).direction = "down";
        // battery.columnsList.get(2).elevatorsList.get(3).floorRequestList.add(24);

        // // C5
        // battery.columnsList.get(2).elevatorsList.get(4).currentFloor = 39;
        // battery.columnsList.get(2).elevatorsList.get(4).direction = "down";
        // battery.columnsList.get(2).elevatorsList.get(4).floorRequestList.add(1);
                
        // // User at lobby want's to go to floor 36, Elevator 1 should be sent
        // System.out.println("User is at the lobby and wants to go to floor 36");
        // System.out.println("He enters 36 on the pannel");
        // battery.assignElevator(36, "up");


        // //--------------------------------------------// Scenario 3 //-----------------------------------------------------
        // // D1
        // battery.columnsList.get(3).elevatorsList.get(0).currentFloor = 58;
        // battery.columnsList.get(3).elevatorsList.get(0).direction = "down";
        // battery.columnsList.get(3).elevatorsList.get(0).floorRequestList.add(1);

        // // D2
        // battery.columnsList.get(3).elevatorsList.get(1).currentFloor = 50;
        // battery.columnsList.get(3).elevatorsList.get(1).direction = "up";
        // battery.columnsList.get(3).elevatorsList.get(1).floorRequestList.add(60);

        // // D3
        // battery.columnsList.get(3).elevatorsList.get(2).currentFloor = 46;
        // battery.columnsList.get(3).elevatorsList.get(2).direction = "up";
        // battery.columnsList.get(3).elevatorsList.get(2).floorRequestList.add(58);

        // // D4
        // battery.columnsList.get(3).elevatorsList.get(3).currentFloor = 1;
        // battery.columnsList.get(3).elevatorsList.get(3).direction = "up";
        // battery.columnsList.get(3).elevatorsList.get(3).floorRequestList.add(54);

        // // D5
        // battery.columnsList.get(3).elevatorsList.get(4).currentFloor = 60;
        // battery.columnsList.get(3).elevatorsList.get(4).direction = "down";
        // battery.columnsList.get(3).elevatorsList.get(4).floorRequestList.add(1);
                
        // // User at floor 54 want's to go to floor 1, Elevator 1 should be sent
        // System.out.println("User is at floor 54 and wants to go to the lobby");
        // System.out.println("He presses on the pannel");
        // battery.columnsList.get(3).requestElevator(54, "down");

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

    }
}