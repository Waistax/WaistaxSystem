package waistax.sistem.command;

/**
 * A command in the console
 *
 * Author: Waistax
 * Created: 0.2 / 17 Aðu 2020 / 21:50:45
 *
 */
public interface Command
{
	/** Run the command with the given arguments */
	public void run(String[] args);
}
