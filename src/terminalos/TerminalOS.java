/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terminalos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import static java.lang.System.exit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


//Mohamed Ahmed Mohamed Shafei 
//Aliaa ehab

/**
 *
 * @author mshaf
 */

 class Parser {
    String commandName; 
    String[] args;
    public boolean parse(String input)
    {
        String[] cmd;
        cmd = input.split(" ",0);
        commandName=cmd[0];
        if(cmd.length==1)
        {
            args = new String [1];
            args [0]="";
        }
        else if(cmd.length==2 && cmd[1].equals("-r")){
            commandName+=" -r";
            for(int i = 0 ; i < args.length ; i++)
            {
                args[i]="";
            }
        }
        else if(cmd[1].equals("-r"))
        {
            cmd[1]="";
            commandName+=" -r";
            args=new String [cmd.length-2];
            for(int i = 0 ; i < args.length ; i++)
            {
                args[i]="";
                args[i]=cmd[i+2];
                //System.out.println(args[i]);
            }
        }
        else
        {
            args=new String [cmd.length-1];
            for(int i = 0 ; i < args.length ; i++)
            {
                args[i]="";
                args[i]=cmd[i+1];
                //System.out.println(args[i]);
            }
        }
        //System.out.println(commandName);
        if(commandName.equals(""))
            return false;
        else
            return true;
    }
    public String getCommandName()
    {
      return this.commandName;
    } 
    public String[] getArgs()
    {
        return args;
    }
    public void rmargs()
    {
        if(args!=null)
        for(int i = 0 ; i < args.length ; i++)
            {
                args[i]="";
            }
    }
}

public class TerminalOS {
    static Parser parser = new Parser();
    static TerminalOS terminal = new TerminalOS();
    private static File current_file , default_directory ;
    int flag=0;
    String nowpath;
   TerminalOS()
   {
       current_file = new File(System.getProperty("user.dir"));
       nowpath=System.getProperty("user.dir");
       default_directory = new File(nowpath);
   }
 public void echo(String [] x)
 {
     for(int i = 0 ; i < x.length ; i++ )
         System.out.print(x[i]+" ");
     System.out.println();
 }
 public void pwd()
 {
    System.out.println("This is pwd "+this.current_file);
 }
  public void ls()
 {
       String arr[] = current_file.list();
           if (arr != null)
           {
               Arrays.sort(arr);
               for (int i=0; i<arr.length;i++) 
               {
                   System.out.println(arr[i]);
               }
           }
      
 }
  public void ls_r()
 {
     String arr[] = current_file.list();
     if (arr != null)
           {
               Arrays.sort(arr, Collections.reverseOrder());
               for (int i=0; i<arr.length;i++) 
               {
                 System.out.println(arr[i]);
               }
           }
 }
  void cd(String path) 
   {
       //System.out.println(path);
       if(path.equals(""))
       {
           current_file=default_directory;
           nowpath=current_file.getAbsolutePath().toString();
       }
       else if(path.equals(".."))
       {
               try{
               nowpath = current_file.getParentFile().getAbsolutePath();
               current_file= new File (current_file.getParentFile().getAbsolutePath());
               }
               catch (Exception IO)
                       {
                           System.out.println("Can not use this command now");
                       }
       }
       else
       {    
            System.out.println(checkPath(path));
            if(Paths.get(checkPath(path)).toFile().isDirectory())
            {
                nowpath = Paths.get(checkPath(path)).toString();
                current_file = new File (nowpath);
            }
            else
            {
                System.out.println("Please enter valid Path");
            }
       }
       terminal.pwd();
       
   }
  String checkPath (String path) {
            Path pth = Paths.get(path);
            if(!pth.isAbsolute()) {
                pth = Paths.get(nowpath + File.separatorChar + path);
            }
            return pth.normalize().toAbsolutePath().toString();// return absolute parh
        }
  public void rm(String file_name) {
          File src = new File(current_file+"\\"+file_name);
          //System.out.println(src);
        if (!src.exists()) {
            System.out.println("Couldn't find file");
        } else if (src.isDirectory()) {
            System.out.println("couldn't delete it because This is a file a directory");
        } 
         else {
            src.delete();
            System.out.println("File Deleted");
        }
    }
   public void cp(String source , String destination) throws IOException
 {
     File f1 =  new File(current_file+"\\"+source);
     File f2 =  new File(current_file+"\\"+destination);
     if(! f1.exists())
     {
         if (f2.isDirectory()) 
        {
           System.out.println("First file does not exsist");
        }
     }
     if(! f2.exists())
     {  if (f2.isDirectory()) 
        {
           System.out.println("Second File does not exsist");
        }
     }
     else
     {
        Files.copy(f1.toPath(),f2.toPath(),StandardCopyOption.REPLACE_EXISTING);
         System.out.println("File copied");
     }
     
 }
   public void mkdir(String[] path){
       for(int i = 0 ; i < path.length; i++)
       {
       Path pth= Paths.get(checkPath(path[i]));
       try{
       Path newDir = Files.createDirectory(pth);
       }
       catch(FileAlreadyExistsException e){
           System.out.println("File exists");
        } catch (IOException e) {
            e.printStackTrace();
        }
       }
   }
   public void touch(String path)
   {
       Path pth= Paths.get(checkPath(path));
       try{
       Path newfile = Files.createFile(pth);
       }
       catch(FileAlreadyExistsException e){
           System.out.println("File exists");
        } catch (IOException e) {
            System.out.println("no such file can be created");
        }
   }
   public void cat (String[] path){
       String statment="";
       String text="";
       for(int i = 0 ; i < path.length; i++)
       {
       File f1 = new File(checkPath(path[i]));
       if(f1.exists())
       try{
           FileReader read = new FileReader(f1);
           BufferedReader bufferReader= new BufferedReader(read);
             while( (statment = bufferReader.readLine()) != null)  {
                  text = text + statment+'\n';
                }
                bufferReader.close();
                System.out.println(text);
       }
       catch (Exception IO) { 
                System.out.println("An Error has occurred ");
            }
       else {
            System.out.println("Couldn't find file");
       }
       }
   }
   public void rmdir(String path) throws NoSuchFileException {
       if("*".equals(path)){
           String arr[] = current_file.list();
           if (arr != null)
           {
               for (int i=0; i<arr.length;i++) 
               {
                   File f1 = new File(checkPath(arr[i]));
                   if (f1.isDirectory()) {
                        if (f1.list().length == 0) {
                            f1.delete();
                            System.out.println(arr[i]+" Directory deleted");
                        } else {
                            System.out.println(arr[i]+" Couldn't delete the directory because it's not empty");
                        }
                    } 
                    else{
                            System.out.println(arr[i]+" couldn't delete it because This is not a directory");
                        }
               }
           }
       }
       else
       {
       File f1 = new File(checkPath(path));
        if (!f1.exists()) {
            System.out.println("File does not exist");
        } else {
            if (f1.isDirectory()) {
                if (f1.list().length == 0) {
                    f1.delete();
                    System.out.println("Directory deleted");
                } else {
                    System.out.println("Couldn't delete the directory because it's not empty");
                }
            } else {
                System.out.println("couldn't delete it because This is not a directory");
            }
        }
        }
    }
   public void cp_r (String source , String destination)
   {
       source=checkPath(source);
       destination=checkPath(destination);
       File f1=new File (checkPath(source));
       File f2=new File (checkPath(destination));
       File f3=new File (checkPath(source));
       try{
       if(f1.isDirectory())
           if(f2.isDirectory())
           {
               int index = source.lastIndexOf("\\");
               String fileName = source.substring(index + 1);
               Path pth= Paths.get(checkPath(destination+"\\"+fileName));
               Path newDir = Files.createDirectory(pth);
               if (f1.list().length == 0) {
                   
               }
               else
               {
                    destination=destination+"\\"+fileName;
                    String [] newarr = f1.list();
                    if (newarr != null)
                    {
                        for (int i=0; i<newarr.length;i++) 
                        {
                            f3=new File (checkPath(source+"\\"+newarr[i]));
                            if(f3.isDirectory())
                            {
                                pth = Paths.get(checkPath(destination+"\\"+newarr[i]));
                                newDir = Files.createDirectory(pth);
                            }
                            else if(f3.isFile())
                            {
                                pth = Paths.get(checkPath(destination+"\\"+newarr[i]));
                                Path newfile = Files.createFile(pth);
                                Files.copy(f3.toPath(),pth,StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                    }
               }
           }
       }
       catch (Exception IO)
       {
           System.out.println("please check the code");
       }
   }
  public void chooseCommandAction() {
            if(parser.getCommandName().equals("pwd"))
            {
                terminal.pwd();
            }
            else if(parser.getCommandName().equals("echo"))
            {
                terminal.echo(parser.getArgs());
            }
            else if(parser.getCommandName().equals("ls"))
            {
                terminal.ls();
            }
            else if(parser.getCommandName().equals("ls -r"))
            {
                terminal.ls_r();
            }
            else if(parser.getCommandName().equals("EXIT") || parser.getCommandName().equals("exit"))
                exit(0);
            else if(parser.getCommandName().equals("cd"))
            {
                String s=Arrays.toString(parser.getArgs());
                StringBuilder sb = new StringBuilder(s);
                sb.deleteCharAt(0); 
                sb.deleteCharAt(s.length()-2); 
                s=sb.toString();
                terminal.cd(s);
                if(flag!=0)
                    parser.rmargs();
                flag=1;
            }
            else if(parser.getCommandName().equals("rm"))
            {
                String s=Arrays.toString(parser.getArgs());
                StringBuilder sb = new StringBuilder(s);
                sb.deleteCharAt(0); 
                sb.deleteCharAt(s.length()-2); 
                s=sb.toString();
                terminal.rm(s);
            }
            else if(parser.getCommandName().equals("cp"))
            {
                try{
                    String[] s;
                    s=parser.getArgs();
                    terminal.cp(s[0],s[1]);
                }
                catch (Exception IO)
                {
                    System.out.println("please enter vaild command");
                }
            }
            else if(parser.getCommandName().equals("cp -r"))
            {
                try{
                    String[] s;
                    s=parser.getArgs();
                    terminal.cp_r(s[0],s[1]);
                }
                catch (Exception IO)
                {
                    System.out.println("enter vaild command");
                }
            }
            else if(parser.getCommandName().equals("mkdir"))
            {
                try{
                terminal.mkdir(parser.getArgs());
                }
                catch (Exception IO)
                {
                    System.out.println("please enter vaild command");
                }
            }
            else if(parser.getCommandName().equals("touch"))
            {
                try{
                    String s=Arrays.toString(parser.getArgs());
                    StringBuilder sb = new StringBuilder(s);
                    sb.deleteCharAt(0); 
                    sb.deleteCharAt(s.length()-2); 
                    s=sb.toString();
                    terminal.touch(s);
                }
                catch (Exception IO)
                {
                    System.out.println("please enter vaild command");
                }
            }
            else if(parser.getCommandName().equals("rmdir"))
            {
                try{
                    String s=Arrays.toString(parser.getArgs());
                    StringBuilder sb = new StringBuilder(s);
                    sb.deleteCharAt(0); 
                    sb.deleteCharAt(s.length()-2); 
                    s=sb.toString();
                    terminal.rmdir(s);
                }
                catch (Exception IO)
                {
                    System.out.println("please enter vaild command");
                }
            }
            else if(parser.getCommandName().equals("cat"))
            {
                try{
                    terminal.cat(parser.getArgs());
                }
                catch (Exception IO)
                {
                    System.out.println("please enter vaild command");
                }
            }
            else
                System.out.println("Enter Right Command");
        }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String command;
        Scanner in = new Scanner(System.in);
        while(true)
        {
            System.out.println("enter your command");
            command = in.nextLine();
            parser.rmargs();
            if(parser.parse(command))
            {
                terminal.chooseCommandAction();
            }
            else
                System.out.println("Enter Command please");
        }
    }
}
