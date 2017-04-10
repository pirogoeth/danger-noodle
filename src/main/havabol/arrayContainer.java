/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.havabol;

import java.util.ArrayList;

/**
 *
 * @author fish
 */
class arrayContainer {

     public boolean bounded;   //determines if array is bounded or unbounded
     String type;              //type of array ie. Int, String...
     int numElements;          //Keeps track of the number of items in the array
     int length;               //Keeps track of the index of the last item in the array
     int maxSize;              //Keeps track of the upper bound of the array
     ArrayList items;          //Will store the items added to the array
     ArrayList <Boolean> validBits;      //will keep track if an item is added into each index

     //if a list of items and a boolean are given it tells that the array should 
     //be unbounded
     arrayContainer(boolean bounded, int size, String type){
         if(bounded){
             this.bounded = true; 
         }    
         makeValidBits(size);
         this.maxSize = size;
         this.numElements = 0;
         this.length = 0;
         this.type = type;
         
         switch(type){
             
             case "Int":
                 items = new <Integer> ArrayList();
                 this.addBlankint(size);
                 break;
             case "String":
                 items = new <String> ArrayList();
                 this.addBlankint(size);
                 break;
             case "Float":
                 items = new <Float> ArrayList();
                 this.addBlankint(size);
                 break;
             case "Boolean":
                 items = new <Boolean> ArrayList();
                 this.addBlankint(size);
                 break;
         }
     }
     

     //Only use this for adding items during initalization
     // ie. Int I[] = {1, 2, 3};
     // otherwise use insert
     public void addInt(int thingToadd){
        if(this.type.equals("Int")){
                this.items.set(this.length, thingToadd);
                this.numElements++;
                this.length++;
                this.validBits.set(this.items.indexOf(thingToadd), true);
            
        }else{
            System.out.println("Error trying to add an int to a non nt array");
            System.exit(-1);
        }
     }
     
     public void addFloat(double thingToadd){
        if(this.type.equals("Float")){
                this.items.set(this.length, thingToadd);
                this.numElements++;
                this.length++;
                this.validBits.set(this.items.indexOf(thingToadd), true);
            
        }else{
            System.out.println("Error trying to add a non float item to a float array");
            System.exit(-1);
        }
     }
    
     public void addString(String thingToadd){
        if(this.type.equals("String")){
                this.items.set(this.length, thingToadd);      
                this.numElements++;
                this.length++;
                this.validBits.set(this.items.indexOf(thingToadd), true);
            
        }else{
            System.out.println("Error trying to add an int to a non nt array");
            System.exit(-1);
        }
     }
          
     public void addBool(boolean thingToadd){
        if(this.type.equals("Boolean")){
                this.items.set(this.length, thingToadd);
                this.numElements++;
                this.length++;
                this.validBits.set(this.items.indexOf(thingToadd), true);
            
        }else{
            System.out.println("Error trying to add an int to a non nt array");
            System.exit(-1);
        }
     }     
        
     public void insertInt(int add, int index){
         int temp = index;
         if(!this.type.equals("Int")){
             System.out.println("Error attempt to add wrong type of item");
             System.exit(-1);
         }
         
         if(temp < this.maxSize){
             if(temp < 0){
                 temp = this.maxSize + temp; 
             }
             this.items.set(temp, add);
             this.validBits.set(temp, true);
             if(index >this.length){
                 this.length = index;
             }
         }else{
             System.out.println("Error index is out fo bounds");
             System.exit(-1);
         }
     }
     
     public void insertString(String add, int index){
         int temp = index;
         if(!this.type.equals("String")){
             System.out.println("Error attempt to add wrong type of item");
             System.exit(-1);
         }
         if(temp < this.maxSize){
             if(temp < 0){
                 temp = this.maxSize + temp; 
             }
             
             this.items.set(temp, add);
             this.validBits.set(temp, true);
             if(index >this.length){
                 this.length = index;
             }
         }else{
             System.out.println("Error index is out fo bounds");
             System.exit(-1);
         }
     }
     
     public void insertFloat(double add, int index){
         int temp = index;
         if(!this.type.equals("Float")){
             System.out.println("Error attempt to add wrong type of item");
             System.exit(-1);
         }
         
         if(temp < this.maxSize){
             if(temp < 0){
                 temp = this.maxSize + temp; 
             }
             this.items.set(temp, add);
             this.validBits.set(temp, true);
             if(index >this.length){
                 this.length = index;
             }
         }else{
             System.out.println("Error index is out fo bounds");
             System.exit(-1);
         }
     }
     
     public void insertBoolean(boolean add, int index){
         int temp = index;
         if(!this.type.equals("Boolean")){
             System.out.println("Error attempt to add wrong type of item");
             System.exit(-1);
         }
         
         if(temp < this.maxSize){
             if(temp < 0){
                 temp = this.maxSize + temp; 
             }
             this.items.set(temp, add);
             this.validBits.set(temp, true);
             if(index >this.length){
                 this.length = index;
             }
         }else{
             System.out.println("Error index is out fo bounds");
             System.exit(-1);
         }
     }
     
     //this sets the valid bits for a bounded array
     private void makeValidBits(int max){
         this.validBits = new ArrayList();
         for(int i = 0; i < max; i++){
             this.validBits.add(false);
         }
     }
     
     private void addValidBit(){
         this.validBits.add(false);
         this.maxSize++;
     }
     
    private void addBlankint(int num){
        for(int i = 0; i < num; i++){
            this.items.add(0);
        }
    }
    
    private void addBlankfloat(int num){
        for(int i = 0; i < num; i++){
            this.items.add(0);
        }
    }
    
    private void addBlankstring(int num){
        for(int i = 0; i < num; i++){
            this.items.add(0);
        }
    }
    
    private void addBlankbool(int num){
        for(int i = 0; i < num; i++){
            this.items.add(false);
        }
    }
    
    public int getIntValue(int index){
        int ret = 0;
             if(index > this.maxSize){
                 System.out.println("Index is out of current bounds of array");
                 System.exit(-1);
             }
            
             if(validBits.get(index)){
                ret = (int) this.items.get(index);
             }else{
                 System.out.println("Value has not be initialied");
                 System.exit(-1);
             }      
        return ret;
    }
    
    public String getStringValue(int index){
        String ret = "0";
             if(index > this.maxSize){
                 System.out.println("Index is out of current bounds of array");
                 System.exit(-1);
             }
            
             if(validBits.get(index)){
                ret = (String) this.items.get(index);
                
             }else{
                 System.out.println("Value has not be initialied");
                 System.exit(-1);
             }      
        return ret;
    }
    
    public double getFloatValue(int index){
        double ret = 0;
        
             if(validBits.get(index)){
                ret = (double) this.items.get(index);
             }else{
                 System.out.println("Value has not be initialied");
                 System.exit(-1);
             }      
        return ret;
    }
    
    public boolean getBooleanValue(int index){
        boolean ret = false;
        
             if(validBits.get(index)){
                ret = (boolean) this.items.get(index);
             }else{
                 System.out.println("Value has not be initialied");
                 System.exit(-1);
             }      
        return ret;
    }

    public void addScalarInt(int scalar){
        if(this.type.equals("Int") || this.type.equals("Float")){
            for(int i = 0; i < this.validBits.size(); i ++){
                if(this.validBits.get(i)){
                    if(this.type.equals("Int")){
                       this.items.set(i, (int) this.items.get(i) + scalar);
                    }
                    if(this.type.equals("Float")){
                        this.items.set(i, (double) this.items.get(i) + scalar);
                    }
                }
            }
        }
    }
    
    public void addScalarDouble(double scalar){
        if(this.type.equals("Int") || this.type.equals("Float")){
            for(int i = 0; i < this.validBits.size(); i ++){
                if(this.validBits.get(i)){
                    if(this.type.equals("Int")){
                        
                       this.items.set(i, (int) (this.getIntValue(i) + scalar));
                       //System.out.println("Here");
                    }else{
                    if(this.type.equals("Float")){
                        System.out.println("Here2");
                        this.items.set(i, ((double) this.items.get(i) + scalar));
                    }
                    }
                }
            }
        }
    }
    
}
 

    

