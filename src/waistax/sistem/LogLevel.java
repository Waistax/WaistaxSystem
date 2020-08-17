package waistax.sistem;

import java.awt.*;

/**
 * Severity level of a log
 *
 * Author: Waistax
 * Created: version / 17 Aðu 2020 / 17:57:34
 *
 */
public class LogLevel
{
	/** The number that represent the severity
	 * A lower number means more severe log. */
	public final int number;
	
	/** The name of the log level */
	public final String name;
	
	/** The color to show the logs in this level */
	public final Color color;

	/** Initialize from a number, a name and a color */
	public LogLevel(int number, String name, Color color)
	{
		this.number = number;
		this.name = name;
		this.color = color;
	}
}
