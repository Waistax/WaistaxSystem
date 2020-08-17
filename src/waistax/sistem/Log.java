package waistax.sistem;

import java.awt.*;
import java.text.*;
import java.util.*;

/**
 * A line in the console
 *
 * Author: Waistax
 * Created: 0.1 / 17 Aðu 2020 / 17:56:18
 *
 */
public class Log
{
	/** The date format to show date in the console */
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yy.MM.dd ");
	
	/** The date format to show time in the console */
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh.mm.ss ");

	/** The color of the date in the console */
	public static Color dateColor;
	
	/** Insert the date and the log level to a list of texts */
	public static void insertHeader(LogLevel level, ArrayList<Text> texts)
	{
		Date date = new Date();
		texts.add(0, new Text(DATE_FORMAT.format(date), dateColor));
		texts.add(1, new Text(TIME_FORMAT.format(date), dateColor));
		texts.add(2, new Text(new StringBuilder().append('[').append(level.name).append("] > ").toString(), level.color));
	}
	
	/** The severity level of this log */
	public final LogLevel level;
	
	/** The texts of this log */
	public final ArrayList<Text> texts;

	/** Initialize from a level and a list of texts */
	public Log(LogLevel level, ArrayList<Text> texts)
	{
		this.level = level;
		this.texts = texts;
		insertHeader(level, this.texts);
	}
	
	/** Initialize from a level and an array of texts */
	public Log(LogLevel level, Text...texts)
	{
		this.level = level;
		this.texts = new ArrayList<>();
		insertHeader(level, this.texts);
		
		for (Text text : texts)
			
			this.texts.add(text);
	}
	
	/** Initialize from a level and a string
	 * This text will have the default color of the level. */
	public Log(LogLevel level, String text)
	{
		this(level, new Text(text, level.color));
	}
}
