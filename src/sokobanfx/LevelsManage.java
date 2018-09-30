package sokobanfx;

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class LevelsManage {
	//Método Que se usa solo en el otro método fromFileToStringArray, 
	//es private porque solo quiero usar aqui y sirve para contar las lineas de archivo
	private static int countLinesFile(File file){
		int lines = 0;
		try{
			
			Scanner fileScanned = new Scanner(file);
			
			while(fileScanned.hasNextLine()){
				fileScanned.nextLine();
				lines++;
			}
			}catch(Exception ex){
				System.out.println("Archivo no encontrado "+ex);
			 }
		return lines;
	}
	
	//Método Vacano para pasar de File A String[]
	private static String[] fromFileToStringArray(File file){
		String[] level = new String[countLinesFile(file)];
			try{
				Scanner fileScanned = new Scanner(file);

				for(int i = 0 ; i < level.length; i++)
					level[i] = fileScanned.nextLine();
     
			}catch(Exception ex){
				System.out.println("Archivo no encontrado "+ex);
			 }
		
		return level;
	}
	
	private static boolean fileExist(String url){
		try{
			Scanner fileScanned = new Scanner(new File(url));
			System.out.println("El archivo se ha cargado con exito");
		}catch(Exception ex){
			System.out.println("El archivo.txt no se encuentra");
			return false;
		}
		return true;
	}
	
	static ArrayList<String[]> getAllFiles(/*String url*/){
		ArrayList<String[]> level_All = new ArrayList<>();
		int i = 1;
		while(fileExist(/*url+"\\*/"level_"+ i + ".txt")){
			level_All.add(fromFileToStringArray(new File(/*url+"\\*/"level_"+ i + ".txt")));
			System.out.print(i);
		i++;
		}
		return  level_All;
	}
}