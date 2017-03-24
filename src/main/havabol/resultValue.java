/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package havabol;

/**
 *
 * @author fish
 */
public class resultValue {
String type;	                // usually data type of the result
String value;		// value of the result
String structure;	// primitive, fixed array, unbounded array
String terminatingStr;  // used for end of lists of things (e.g., a list of statements might be
                               // terminated by "endwhile")

   resultValue(){
      type = "";
      value = "";
      structure = "";
      terminatingStr = "";
   }
}
