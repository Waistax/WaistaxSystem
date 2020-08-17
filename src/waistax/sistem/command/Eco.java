package waistax.sistem.command;

import waistax.sistem.*;

/**
 * Command that logs back the given arguments
 *
 * Author: Waistax
 * Created: 0.2 / 17 A�u 2020 / 21:53:42
 *
 */
public class Eco implements Command
{
	@Override
	public void run(String[] args)
	{
		StringBuilder builder = new StringBuilder();
		
		for (String arg : args)
			
			builder.append(arg).append(" ");
		
		Sistem.SISTEM.log(new Log(Sistem.REGISTRY.get("sistem:mesaj_kay�d�", LogLevel.class), builder.toString().trim()));
	}
}
