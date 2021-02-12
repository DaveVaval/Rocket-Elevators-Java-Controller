// Battery Class
// public class Battery
// {
//     public int ID;
//     public string status;
//     public int amountOfFloors;
//     public int amountOfColumns;
//     public int amountOfBasements;
//     public int columnID;
//     public int floorRequestButtonID;
//     public List<Column> columnsList;
//     public List<FloorRequestButton> floorRequestButtonsList;
    
//     public Battery(int id, string status,int amountOfColumns, int amountOfFloors, int amountOfBasements, int amountOfElevatorPerColumn) // this is the battery constructor
//     {
//         this.ID = id;
//         this.amountOfColumns = amountOfColumns;
//         this.status = status;
//         this.amountOfFloors = amountOfFloors;
//         this.amountOfBasements = amountOfBasements;
//         this.columnsList = new List<Column>();
//         this.floorRequestButtonsList = new List<FloorRequestButton>();
//         this.columnID = 1;
//         this.floorRequestButtonID = 1;

//         if (this.amountOfBasements > 0){
//             createBasementFloorRequestButtons(amountOfBasements);
//             createBasementColumn(this.amountOfBasements, amountOfElevatorPerColumn);
//             amountOfColumns--;
//         }
//         createFloorRequestButtons(amountOfFloors);
//         createColumns(amountOfColumns, this.amountOfFloors, this.amountOfBasements, amountOfElevatorPerColumn);
//     }

//     public void createBasementColumn(int amountOfBasements, int amountOfElevatorPerColumn){
//         List<int> servedFloors = new List<int>();
//         int floor = -1;

//         for (int i = 0; i < amountOfBasements; i++){
//             servedFloors.Add(floor);
//             floor--;
//         }
//         columnsList.Add(new Column(columnID, "online", amountOfBasements, amountOfElevatorPerColumn, servedFloors, true));
//         columnID++;
//     }

//     public void createColumns(int amountOfColumns, int amountOfFloors, int amountOfBasements, int amountOfElevatorPerColumn){
//         int amountOfFloorsPerColumn = (int)Math.Ceiling((double)amountOfFloors / amountOfColumns);
//         int floor = 1;

//         for (int i = 1; i <= amountOfColumns; i++){ // i = 1 fixed the issue of the amount of columns
//             List<int> servedFloors = new List<int>(); 
//             for (int n = 0; n < amountOfFloorsPerColumn; n++){
//                 if(floor <= amountOfFloors){
//                     servedFloors.Add(floor);
//                     floor++;
//                 }
//             }
//             columnsList.Add(new Column(columnID, "online", amountOfFloors, amountOfElevatorPerColumn, servedFloors, false));
//             columnID++;
//         }
//     }

//     public void createFloorRequestButtons(int amountOfFloors){
//         int buttonFloor = 1;
//         for (int i = 1; i <= amountOfFloors; i++){
//             floorRequestButtonsList.Add(new FloorRequestButton(floorRequestButtonID, "off", buttonFloor));
//             floorRequestButtonID++;
//             buttonFloor++;
//         }
//     }

//     public void createBasementFloorRequestButtons(int amountOfBasements){
//         int buttonFloor = -1;
//         for (int i = 1; i <= amountOfBasements; i++){
//             floorRequestButtonsList.Add(new FloorRequestButton(floorRequestButtonID, "off", buttonFloor));
//             buttonFloor--;
//             floorRequestButtonID++;
//         }
//     }

//     public Column findBestColumn(int requestedFloor){
//         Column col = null;
//         foreach (Column column in columnsList){
//             if(column.servedFloors.Contains(requestedFloor)){
//                 col = column;
//             }
//         }
//         return col;
//     }

//     // This function will return the best elevator from the best column to the user
//     public void assignElevator(int requestedFloor, string direction){ 
//         Column column = findBestColumn(requestedFloor); // return?
//         Elevator elevator = column.findBestElevator(1, direction); // return?
//         elevator.floorRequestList.Add(requestedFloor);
//         elevator.sortFloorList();
//         elevator.move();
//         elevator.openDoors();
//         Console.WriteLine(".........");
//         Console.WriteLine("Elevator {0} from column {1} is sent to lobby", elevator.ID, column.ID);
//         Console.WriteLine("He enters the elevator");
//         Console.WriteLine(".........");
//         Console.WriteLine("Elevator reached floor: " + elevator.currentFloor);
//         Console.WriteLine("He gets out...");
//     }
// }


// // Column class
// public class Column{
//     public int ID;
//     public string status;
//     public int amountOfFloors;
//     public int amountOfElevators;
//     public bool isBasement;
//     public List<int> servedFloors;
//     public List<Elevator> elevatorsList;
//     public List<CallButton> callButtonsList;

//     public Column(int id, string status, int amountOfFloors, int amountOfElevators, List<int> servedFloors, bool isBasement){ // This is the column constructor
        
//         this.ID = id;
//         this.status = status;
//         this.amountOfFloors = amountOfFloors;
//         this.amountOfElevators = amountOfElevators;
//         this.isBasement = isBasement;
//         this.elevatorsList = new List<Elevator>(){};
//         this.callButtonsList = new List<CallButton>(){};
//         this.servedFloors = servedFloors;
//         createElevators(amountOfFloors, amountOfElevators);
//         createCallButtons(amountOfFloors, isBasement);

//     }


//     public void createElevators(int amountOfFloors, int amountOfElevators){
//         int elevatorID = 1;
//         for(int i = 0; i < amountOfElevators; i++){
//             elevatorsList.Add(new Elevator(elevatorID, "idle", amountOfFloors, 1));
//             elevatorID++;
//         }
//     }

//     public void createCallButtons(int amountOfFloors, bool isBasement){
//         int callButtonID = 1;
//         if (isBasement == true){
//             int buttonFloor = -1;
//             for(int i = 0; i < amountOfFloors; i++){
//                 callButtonsList.Add(new CallButton(callButtonID, "off", buttonFloor, "up"));
//                 buttonFloor--;
//                 callButtonID++;
//             }
//         } 
//         else{
//             int buttonFloor = 1;
//             for(int i = 0; i < amountOfFloors; i++){
//                 callButtonsList.Add(new CallButton(callButtonID, "off", buttonFloor, "down"));
//                 buttonFloor++;
//                 callButtonID++;
//             }
//         }
//     }

//     // This is the function that will be called when a user wants to go back to the lobby from any given floor
//     public void requestElevator(int userPosition, string direction){
//         Elevator elevator = findBestElevator(userPosition, direction); // return?
//         elevator.floorRequestList.Add(1);
//         elevator.sortFloorList();
//         elevator.move();
//         elevator.openDoors();
//         Console.WriteLine(".........");
//         Console.WriteLine("Elevator {0} from column {1} is sent to floor: {2}", elevator.ID, this.ID, userPosition);
//         Console.WriteLine("He enters the elevator");
//         Console.WriteLine(".........");
//         Console.WriteLine("Elevator reached floor: " + elevator.currentFloor);
//         Console.WriteLine("He gets out...");
//     }

//     // This function in conjuction wwith checkElevator will return the best elevator
//     public Elevator findBestElevator(int requestedFloor, string requestedDirection){ 
//         Hashtable bestElevatorInfo = new Hashtable(){
//             {"bestElevator", null},
//             {"bestScore", 6},
//             {"referenceGap", int.MaxValue}
//         };
//         if(requestedFloor == 1){
//             foreach(Elevator elevator in elevatorsList){
//                 if(1 == elevator.currentFloor && elevator.status == "stopped"){
//                     bestElevatorInfo = checkElevator(1, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else if(1 == elevator.currentFloor && elevator.status == "idle"){
//                     bestElevatorInfo = checkElevator(2, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else if(1 > elevator.currentFloor && elevator.direction == "up"){
//                     bestElevatorInfo = checkElevator(3, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else if(1 < elevator.currentFloor && elevator.direction == "down"){
//                     bestElevatorInfo = checkElevator(3, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else if(elevator.status == "idle"){
//                     bestElevatorInfo = checkElevator(4, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else{
//                     bestElevatorInfo = checkElevator(5, elevator, requestedFloor, bestElevatorInfo);
//                 }
//             }
//         }
//         else{
//             foreach(Elevator elevator in elevatorsList){
//                 if(requestedFloor == elevator.currentFloor && elevator.status == "idle" && requestedDirection == elevator.direction){
//                     bestElevatorInfo = checkElevator(1, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else if(requestedFloor > elevator.currentFloor  && elevator.direction == "up" && requestedDirection == elevator.direction){
//                     bestElevatorInfo = checkElevator(2, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else if(requestedFloor < elevator.currentFloor  && elevator.direction == "down" && requestedDirection == elevator.direction){
//                     bestElevatorInfo = checkElevator(2, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else if(elevator.status == "stopped"){
//                     bestElevatorInfo = checkElevator(4, elevator, requestedFloor, bestElevatorInfo);
//                 }
//                 else{
//                     bestElevatorInfo = checkElevator(5, elevator, requestedFloor, bestElevatorInfo);
//                 }
//             }
//         }
//         return (Elevator)bestElevatorInfo["bestElevator"];
//     }

//     public Hashtable checkElevator(int baseScore, Elevator elevator, int floor, Hashtable bestElevatorInfo){
//         if(baseScore < (int)bestElevatorInfo["bestScore"]){
//             bestElevatorInfo["bestScore"] = baseScore;
//             bestElevatorInfo["bestElevator"] = elevator;
//             bestElevatorInfo["referenceGap"] = (int)Math.Abs((double)elevator.currentFloor - floor);
//         }
//         else if((int)bestElevatorInfo["bestScore"] == baseScore){
//             int gap = (int)Math.Abs((double)elevator.currentFloor - floor);
//             if((int)bestElevatorInfo["referenceGap"] > gap){
//                 bestElevatorInfo["bestElevator"] = elevator;
//                 bestElevatorInfo["referenceGap"] = gap;
//             }
//         }
//         return bestElevatorInfo;
//     }
// }



// // Elevator class
// public class Elevator{
//     public int ID;
//     public string status;
//     public int amountOfFloors;
//     public string direction;
//     public int currentFloor;
//     public Door door;
//     public List<int> floorRequestList; 

//     public Elevator(int id, string status, int amountOfFloors, int currentFloor){ // Elevator Constructor
//         this.ID = id;
//         this.status = status;
//         this.amountOfFloors = amountOfFloors;
//         this.direction = null;
//         this.currentFloor = currentFloor;
//         this.door = new Door(id, "closed");
//         this.floorRequestList = new List<int>();
//     }

//     // This function will make the elevator to any given floor
//     public void move(){
//         while(floorRequestList.Count != 0){
//             int destination = floorRequestList[0];
//             status = "moving";
//             if(currentFloor < destination){
//                 direction = "up";
//                 while(currentFloor < destination){
//                     currentFloor++;
//                 }
//             }
//             else if(currentFloor > destination){
//                 direction = "down";
//                 while(currentFloor > destination){
//                     currentFloor--;
//                 }
//             }
//             status = "idle";
//             floorRequestList.RemoveAt(0);
//         }
//     }

//     public void sortFloorList(){
//        if(direction == "up"){
//            floorRequestList.Sort();
//        }
//        else{
//            floorRequestList.Reverse();
//        }
//     }

//     public void openDoors(){
//         door.status = "open";
//         door.status = "closed";
//     }
// }



// // Call Button class
// public class CallButton{
//     public int ID;
//     public string status;
//     public int floor;
//     public string direction;

//     public CallButton(int id, string status, int floor, string direction){
//         this.ID = id;
//         this.status = status;
//         this.floor = floor;
//         this.direction = direction;
//     }
// }


// // Floor request button class
// public class FloorRequestButton{
//     public int ID;
//     public string status;
//     public int floor;

//     public FloorRequestButton(int id, string status, int floor){
//         this.ID = id;
//         this.status = status;
//         this.floor = floor;
//     }
// }


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
    public static void main(String[] args){
        System.out.println("-------------------------// TESTING //-----------------------------");
        Door newDoor = new Door(1, "what?");

        System.out.println(newDoor.ID);
    }
}