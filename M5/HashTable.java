/**HashPrac
*Practice using Hashtable to add, remove and search for elements
*within the data structure 
*/

public class HashTable {
//initialize size of data structure to 5
   static final int SIZE = 5;
//initialize array for primary data structure 
   int arr[] = new int [SIZE];
//constructor 
   public HashTable() {
   //initialize counter 
      int i;
   //initialize all spots to -1
      for (i = 0; i < SIZE; i++)
         arr[i] = -1;
   }

//search method 
   public int search(int value) {
   //find key using modulus division
      int key = value % SIZE;
   //assignment statement for above
      int index = key;
   //check for collisions 
      while(arr[index] != -1) {
      //try next spot if collision occurs 
         index = (index + 1) % SIZE;
      //if we reach the end of table, array is full
         if(index == key) {
            System.out.println("it be full asf");
         //return zero upon failure 
            return 0;
         }
      }
   //else
   //return upon find 
      return index;
   
   }

}