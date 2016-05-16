package original_scheduler;

/**
 * @author Morgan Farmer
 */

class SimProcess implements Comparable {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int rTime;
    private int finishTime;
    private boolean done;
    
    public void resetRT(){
        rTime = burstTime;
    }
    
    public int getRT(){
        return rTime;
    }
    
    public String getName(){
        return name;
    }
    
    public int setFinTime(int t){
        return finishTime = t;
    }
    
    public int getArrivalTime(){
        return arrivalTime;
    }
    
    public int getBurstTime(){
        return burstTime;
    }
    
    public void setBurst(int b){
        burstTime = b;
    }
    
    public boolean isDone(){
        return done;
    }
    
    public void setDone(boolean tf){
        done = tf;
    }
    
    public void setArrival(int a){
        arrivalTime = a;
    }
    
    public void setRT(int now){
        rTime = now;
    }
    
    public void decreaseRT(int up){
        rTime = rTime - up;
    }
    
    public void set(String newName, int newArrivalTime, int newBurstTime){
        name = newName;
        arrivalTime = newArrivalTime;
        burstTime = newBurstTime;
        rTime = burstTime;
        finishTime = 0;
        done = false;
    }
    
    public void reset(){
        rTime = burstTime;
        finishTime = 0;
        done = false;
    }
    
    public void toText(){
        System.out.println(name + ": waiting time: " + ((finishTime - arrivalTime)- burstTime) + " Turnaround time: " + (finishTime - arrivalTime));
        System.out.println("    Arrival Time: " + arrivalTime + " Burst Time: " + burstTime);
        System.out.println();
    }
    
    public int compareTo(Object anotherProcess) throws ClassCastException{
        if (!(anotherProcess instanceof SimProcess)){
            throw new ClassCastException("A simProcess expected.");
        }
        int anotherArrivalTime = ((SimProcess) anotherProcess).getArrivalTime();
        return this.arrivalTime - anotherArrivalTime;
    }
}