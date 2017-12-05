package edu.controllers;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class WriteToFile 
{
	private String path;
	private boolean appendToFile = false;
	
	public WriteToFile(String filePath)
	{
		path = filePath;
	}
	
	public WriteToFile( String filePath , boolean appendValue ) 
	{
		path = filePath;
		appendToFile = appendValue;
	}
	
	public void writeToFile(String textLine) throws IOException 
	{
		FileWriter write = new FileWriter(path, appendToFile);
		PrintWriter printLine = new PrintWriter(write);
		printLine.printf( "%s" + "%n" , textLine);
		printLine.close();
	}
}
