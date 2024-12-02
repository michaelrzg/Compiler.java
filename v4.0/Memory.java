
// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 4
// File: Memory

// a simple memory system to store and fetch identifier values

import java.util.HashMap;
import java.util.Map;

class Memory {
    int size;
    String [] memory;
    Map<String, Integer> lookup;
    /**
     * Constructor for Memory.
     * 
     * @param size Size of memory (minimum of 128)
     */
    public Memory(int size){
        if(size<128){
            System.out.println("Memory set to minimum size of 128 locations");
            this.size = 128;
        }
        else {this.size=size;}
        memory = new String[this.size];
        lookup = new HashMap<>();
    }
    /**
     * Fetches passed ID and returns its value
     * 
     * @param id String
     */
    public String fetch(String id){
       Integer location = lookup.get(id);
       if(location!=null){
        return memory[location.intValue()];
       }
       else return null;
    }
    /**
     * Saves an ID, Value pair in memory
     * 
     * @param id String
     * @param value String
     */
    public boolean save(String id, String data){
        Integer x = lookup.get(id);
        if(x!=null){
            System.out.println(id+ " already exists in memory. Updating value from "+ memory[x]+" to " + data);
        }
        int location = generate_location(id);
        memory[location] = data;
        lookup.put(id,location);
        return true;
    }
    /**
     * Simple hash function to generate memory locations
     * 
     * @param id String 
     */
    private int generate_location(String id){
        int value = 0;
        int x = 12;
        for (int i=0;i<id.length();i++){
            value+= id.charAt(i)*x;
        }
        value%=size;
        while(memory[value]!=null){
            value+=1;
            value%=size;
        }
        return value;
    }
}