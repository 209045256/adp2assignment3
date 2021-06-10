/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.adp2assignment3;

import java.io.*;
import java.text.*;
import java.time.format.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;

/**
 *
            ____              _   _                  _____              _ _        __  __       _         ____   ___   ___   ___  _  _  ____ ____  ____   __   
  __/\__   / __ \  __ _ _   _| |_| |__   ___  _ __  |__  /___ _ __  ___(_) | ___  |  \/  | ___ | | ___   |___ \ / _ \ / _ \ / _ \| || || ___|___ \| ___| / /_  
  \    /  / / _` |/ _` | | | | __| '_ \ / _ \| '__|   / // _ \ '_ \|_  / | |/ _ \ | |\/| |/ _ \| |/ _ \    __) | | | | (_) | | | | || ||___ \ __) |___ \| '_ \ 
  /_  _\ | | (_| | (_| | |_| | |_| | | | (_) | |     / /|  __/ | | |/ /| | |  __/ | |  | | (_) | | (_) |  / __/| |_| |\__, | |_| |__   _|__) / __/ ___) | (_) |
    \/    \ \__,_|\__,_|\__,_|\__|_| |_|\___/|_|    /____\___|_| |_/___|_|_|\___| |_|  |_|\___/|_|\___/  |_____|\___/   /_/ \___/   |_||____/_____|____/ \___/ 
           \____/                                                                                                                                              
 * 
 */
public class TaskOutputs  
{
    private ObjectInputStream in;
    private ArrayList<Customer> customers = new ArrayList<Customer>();
    private ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
    private BufferedWriter readS, readC; 
    private ArrayList<Customer> CustomerFDOB = new ArrayList<>();
    private ArrayList<Integer> DetAge = new ArrayList<Integer>();
    
    
     public void open()
        {
        try
        {
            in = new ObjectInputStream( new FileInputStream( "stakeholder.ser" ) ); 
            System.out.println("*** CREATED AND READ ***");               
        }
        catch (IOException ioe){
            System.out.println("error opening ser file: " + ioe.getMessage());
        }
    }
     
    public void close()
    {
        try
        {
        in.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("error closing ser file: " + ioe.getMessage());
        }        
    }        
    
    // Array lists
    public void readTheFile()
    {
        try
        {
            Stakeholder ser;
            while(true)
            {
                ser = (Stakeholder)in.readObject();
                
                if(ser instanceof Customer)
                {
                    customers.add((Customer)ser);  
                }
                
                if(ser instanceof Supplier)
                {
                    suppliers.add((Supplier)ser); 
                }
                
            }
        }
        catch (EOFException e)
        {
            System.out.println("");
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("");            
        }
        catch (IOException i)
        {
            System.out.println("");
        }
        finally
        {
            close();
        }
        
    } 
    
  
    public void SortTheCustomer()
    {
        customers.sort((customer1, customer2) -> customer1.getStHolderId().compareTo(customer2.getStHolderId())); 
    }
    
    
    public void Age()
    {
        try
        {
            customers.forEach((Age) ->
            {
                LocalDate DoB = LocalDate.parse(Age.getDateOfBirth());
                LocalDate present = LocalDate.now(); 
                
                int Y = Period.between(DoB, present).getYears();  
                System.out.println(Y); 
                
                DetAge.add(Y); 
            });            
        }
        catch (DateTimeParseException dtpe)
        {
            System.out.println("Show Error: " + dtpe);
        }
       
    }

    public void formattingTheDate() //Date Formatting
    {
        customers.forEach((DateFormat) ->
        {
            String DOB = DateFormat.getDateOfBirth();  // retrieving the customers date of birth
            SimpleDateFormat FDOB = new SimpleDateFormat("yyyy-MM-dd");
            
            try
            {
                Date d = FDOB.parse(DOB);
                SimpleDateFormat AlterFormat = new SimpleDateFormat("dd MMM yyyy"); //format this way
                DOB = AlterFormat.format(d);
                DateFormat.setDateOfBirth(DOB);
            }
            catch (ParseException pe)
            {
               System.out.println("Show Error: " + pe);      
            }
        });
    }
  
   public void writeCustomer()  // Creating the text file and storing the customer data 
    {
  
    try
    {            
            FileWriter customerRead = new FileWriter("customerOutFile.txt");  
            readC = new BufferedWriter(customerRead);              
            readC.write(String.format("========================= CUSTOMERS ==========================\n"));
            readC.write(String.format("%-8s%-15s%-15s%-15s%-15s\n", "ID", "Name", "Surname", "Date Of Birth", "Age"));            
            readC.write(String.format("==============================================================\n"));
            formattingTheDate();
            
            for (int i = 0; i < customers.size(); i++)
            {
                    readC.write(String.format("%-8s%-15s%-15s%-15s%-15s\n",                
                    customers.get(i).getStHolderId(),
                    customers.get(i).getFirstName(),
                    customers.get(i).getSurName(),
                    customers.get(i).getDateOfBirth(),
                    DetAge.get(i).toString()
                                            ));                    
            }
            
            readC.write("\nNumbers of Customers who can rent:      " + Renting());            
            readC.write("\nNumbers of Customers who cannot rent:   " + CantRent());
            
    }
    catch (IOException ee)
     {
         System.out.println("Error: " + ee.getMessage());
         
     }
        try 
        {
            readC.close();
        }
        catch (IOException ex)
        {
            System.out.println("Error:" + ex);
        }
   } 
    
   
   // Rent 
   int canRent = 0;
   int cannotRent = 0;
   
    public int Renting()
    {
        customers.forEach((CCR)->
        {
            if(CCR.getCanRent())
            {
                //System.out.println(canRent++);
                canRent++;
            }
        });
        System.out.println("Numbers of Customers who can Rent: " + "     " + canRent);
        return canRent;
     }
  
    public int CantRent()
    {
       customers.forEach((CCR)->
       {
                if(CCR.getCanRent()== false)
                {
                    //System.out.println(cannotRent++);
                    cannotRent++;
                }
            });
            System.out.println("Numbers of Customers who cannot Rent: " + "  " + cannotRent);
            return cannotRent;
        }
   
    // Sort the suppliers in name ascending order
    public void SortTheSupplier()
    {
        suppliers.sort((supplier1, supplier2) -> supplier1.getName().compareTo(supplier2.getName())); 
    }
   
    
    public void writeSupplier() // Creating the supplier text file and storing
    {        
        try{
            FileWriter supplierRead = new FileWriter("supplierOutFile.txt");  
            BufferedWriter readS = new BufferedWriter(supplierRead);  
            readS.write(String.format("========================= SUPPLIERS ============================\n"));
            readS.write(String.format("%-7s%-24s%-12s%-15s\n", "ID", "Name", "Prod Type", "Description"));            
            readS.write(String.format("================================================================\n"));
            
            suppliers.forEach((sup) ->
            {
                try {
                    readS.write(String.format("%-7s%-24s%-12s%-15s\n",
                            sup.getStHolderId(),
                            sup.getName(),
                            sup.getProductType(),
                            sup.getProductDescription()
                            ));
                    } 
                catch (IOException ex)
                {
                    System.out.println("Show: " + ex);
                }
                                       
             });
        
        readS.close();
        
        
        }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
   
    }
    
    
    public static void main(String[] args) // Main method 
    {
        TaskOutputs  CS = new TaskOutputs ();
        
        CS.open();
        CS.readTheFile();
        CS.SortTheCustomer();
        CS.Age();
        CS.formattingTheDate();   
        CS.writeCustomer();
        CS.SortTheSupplier();
        CS.writeSupplier(); 
        CS.close();
       
        
    }
  
}
     


