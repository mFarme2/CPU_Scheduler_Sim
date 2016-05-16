package original_scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Morgan Farmer
 */
public class mfarme2_cpu_scheduler {
    public static void main(String args[]) throws FileNotFoundException, IOException{
        Scanner in = new Scanner(System.in);
        String loc = ("arivalAndBurst.txt");
        boolean flag = false;
        int slice = 10;
        
        while (!flag){
            System.out.println("--------CPU Scheduling Algorithm Simulator--------");
            System.out.println("Type 1 to import from file.");
            System.out.println("Type 2 to select number of processes to randomly generate.");
            System.out.println("Type quit to quit.");
            String answer = in.next();
            
            //USER WANTS TO TAKE PROCESSES FROM A FILE
            if (answer.contains("1")){
                //import
                System.out.println("Importing...");
                
                //procSet = importFrom(file.toString());
                SimProcess[] procSet = importFrom(loc);
                
                RR(procSet, slice);
                
                SJF(procSet);

                flag = true;
            }
            
            //USER WANTS TO HAVE HIS PROCCESSES RANDOMLY GENERATED
            else if (answer.contains("2")){
                System.out.println("How many processes do you want to make? ");
                if (in.hasNextInt()){
                    int n = in.nextInt();
                    
                    //returns a SimProcess Array of n generated arrival times sorted by shortest.
                    SimProcess[] procSet = generate(n);
                    
                    RR(procSet, slice);
                    
                    SJF(procSet);
                    
                    flag = true;
                } else {
                    System.out.println("INVALID!");
                    flag = false;
                }
            } else if (answer.toLowerCase().contains("quit")){
                flag = true;
            } else {
                //something other than 1, 2, or quit...
                System.out.println("INVALID!");
                flag = false;
            }
        }
        in.close();
    }
    
    public static SimProcess[] importFrom(String location) throws FileNotFoundException, IOException{
        File f = new File(location);
        BufferedReader lineFinder = new BufferedReader(new FileReader(f));
        int lineCount = 0;
        while (lineFinder.readLine() != null) lineCount++;
        lineFinder.close();
        
        SimProcess[] simP = new SimProcess[lineCount];
        Scanner read = new Scanner(f);
        int counter = 0;
        while(read.hasNextLine()){
            simP[counter] = new SimProcess();
            simP[counter].set(("P" + counter), read.nextInt(), read.nextInt());
            counter++;
        }
        read.close();
        Arrays.sort(simP);

        return simP;
    }
    
    public static SimProcess[] generate(int num){
        Random randNum = new Random();
        SimProcess[] simP = new SimProcess[num];
        
        for (int i = 0; i < num; i++){
            simP[i] = new SimProcess();
            simP[i].set(("Process " + i), (randNum.nextInt(50)+1), randNum.nextInt(100));
        }
        Arrays.sort(simP);
        
        return simP;
    }
    
    public static void RR(SimProcess[] simArray, int slice){
        //ROUND ROBIN ALGORITHMN
        System.out.println("---------ROUND ROBIN---------");
        int totalTime = 0;
        int time = 0;
        
        //Finds time the total time the system will need to run until all processes are done.
        for (SimProcess procSet1 : simArray) {
            totalTime = totalTime + procSet1.getBurstTime();
        }
        totalTime = totalTime + simArray[0].getArrivalTime();
        
        while (time <= totalTime + 1){
            int ready = 0;
            
            //Go through and tallys up number of processes ready to go on 
            for (int i = 0; i < simArray.length ; i++){
                if ((simArray[i].getArrivalTime() <= time) && (simArray[i].getRT() != 0)){ready++;}
            }
            
            if (ready == 0) {
                //no processes are ready so increase time
                time++;
            } else {
                for (int i = 0; i < simArray.length ; i++) {
                    //iterates through the processes and only "takes" the process if it has arrived and is not finished
                    if ((simArray[i].getArrivalTime() <= time) && (simArray[i].getRT() != 0)) {
                        System.out.println("computing " + simArray[i].getName());
                        if (simArray[i].getRT() > slice){
                            simArray[i].decreaseRT(slice);
                            time = time + slice;
                        } else if (simArray[i].getRT() <= slice) {
                            time = time + simArray[i].getRT();
                            simArray[i].setRT(0);
                            simArray[i].setFinTime(time); //this process is done.
                        }
                    }
                }
            }
        }
        for (int i = 0; i < simArray.length; i++) {
            //PRINT RESULTS OF Round Robin turnaround times and waiting times.
            simArray[i].toText();
            simArray[i].reset();//reset
        }
    }
    
    public static void SJF(SimProcess[] simArray){
        int totalTime = 0;
        int time = 0;
        
        System.out.println("---------SJF---------");
        //Finds time the total time the system will need to run until all processes are done.
        for (SimProcess procSet1 : simArray) {
            totalTime = totalTime + procSet1.getBurstTime();
        }
        totalTime = totalTime + simArray[0].getArrivalTime();
        
        while (time <= totalTime){
            int ready = 0;
            
            //Go through and tallys up number of processes ready to go on 
            for (int i = 0; i < simArray.length ; i++){
                if ((simArray[i].getArrivalTime() <= time) && (simArray[i].getRT() != 0)){
                    ready++;
                    System.out.println("adding 1");
                }
            }
            
            if (ready == 0) {
                //no processes are ready so increase time
                time++;
            } else {
                for(int i = 0; i < simArray.length; i++){
                    if ((simArray[i].getArrivalTime() <= time) && (simArray[i].getRT() != 0)){
                        System.out.println("computing " + simArray[i].getName());
                        time = time + simArray[i].getRT();
                        simArray[i].setRT(0);
                        simArray[i].setFinTime(time);
                    }
                }
            }
        }
        System.out.println("RESULTS::::");
        for (int i = 0; i < simArray.length; i++) {
            //PRINT RESULTS OF SJF turn around times and waiting times.
            simArray[i].toText();
            simArray[i].reset();//reset
        }
    }
}
