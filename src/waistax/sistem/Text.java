package waistax.sistem;

import java.awt.*;

/**
 * A colored text to draw in the console
 *
 * Author: Waistax
 * Created: version / 17 Aðu 2020 / 18:01:53
 *
 */
public class Text
{
	/** The string of this text */
	public final String string;
	
	/** The color of this text */
	public final Color color;

	/** Initialize from a string and a color */
	public Text(String string, Color color)
	{
		this.string = string;
		this.color = color;
	}
}
