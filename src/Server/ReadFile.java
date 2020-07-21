package Server;

import java.io.File;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ReadFile {
	public  String readFile(File file) {

		String result=null;
		try {
			 
			Scanner sc=new Scanner(new FileReader(file));
			String line=null;
			while((sc.hasNextLine()&&(line=sc.nextLine())!=null)){
			    if(!sc.hasNextLine())
			    result=line;
			}
				sc.close();

	 }catch (FileNotFoundException e) {

	e.printStackTrace();

	} catch (IOException e) {

		e.printStackTrace();

		}
	
	return result;
	}


}
