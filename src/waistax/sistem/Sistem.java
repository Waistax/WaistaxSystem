package waistax.sistem;

import waistax.engine.*;
import waistax.engine.renderer.*;
import waistax.math.*;
import waistax.registry.*;
import waistax.sistem.command.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Description
 *
 * Author: Waistax
 * Created: 0.1 / 17 Ağu 2020 / 17:50:53
 *
 */
public class Sistem implements App
{
	public static final String VERSION = "0.2";
	
	public static final Registry REGISTRY = new Registry();
	
	public static final AWTRenderer RENDERER = new AWTRenderer();
	
	public static final Sistem SISTEM = new Sistem();
	
	public static void main(String[] args)
	{
		Engine.app = SISTEM;

		RENDERER.antiAliasing = true;
		RENDERER.title = "Waistax Sistem " + VERSION;
		RENDERER.dimension = new Vec2i(16 * 80, 9 * 80);
		RENDERER.input = new AWTInput()
				{
					@Override
					public void keyPressed(KeyEvent e)
					{
						super.keyPressed(e);
						SISTEM.input(e);
					}
				};
		
		Engine.renderer = RENDERER;
		
		Engine.start();
	}
	
	public ArrayList<Log> allLogs;
	
	public ArrayList<Log> visibleLogs;

	public Font consoleFont;
	
	public Graphics2D graphics;
	
	public boolean hideDate;
	
	public boolean hideTime;
	
	public String input;
	
	public Color inputColor;
	
	public Color inputCadetColor;
	
	public int inputCadet;
	
	public float inputCadetLastMovementTime;
	
	public boolean inputInsert;
	
	@Override
	public void load()
	{
		// Register colors
		REGISTRY.add("sistem:beyaz_renk",		new Color(1.0F, 1.0F, 1.0F));
		REGISTRY.add("sistem:kırmızı_renk",		new Color(1.0F, 0.2F, 0.2F));
		REGISTRY.add("sistem:yeşil_renk",		new Color(0.2F, 1.0F, 0.2F));
		REGISTRY.add("sistem:mavi_renk",		new Color(0.2F, 0.2F, 1.0F));
		REGISTRY.add("sistem:sarı_renk",		new Color(1.0F, 1.0F, 0.2F));
		REGISTRY.add("sistem:mor_renk",			new Color(1.0F, 0.2F, 1.0F));
		REGISTRY.add("sistem:turkuaz_renk",		new Color(0.2F, 1.0F, 1.0F));
		REGISTRY.add("sistem:gri_renk",			new Color(0.2F, 0.2F, 0.2F));
		REGISTRY.add("sistem:açık_gri_renk",	new Color(0.6F, 0.6F, 0.6F));
		REGISTRY.add("sistem:siyah_renk",		new Color(0.0F, 0.0F, 0.0F));
		
		// Register log levels
		REGISTRY.add("sistem:hata_kayıdı",		new LogLevel(0, "HATA",		REGISTRY.get("sistem:kırmızı_renk", Color.class)));
		REGISTRY.add("sistem:uyarı_kayıdı",		new LogLevel(1, "UYARI",	REGISTRY.get("sistem:sarı_renk", Color.class)));
		REGISTRY.add("sistem:bilgi_kayıdı",		new LogLevel(2, "BİLGİ",	REGISTRY.get("sistem:turkuaz_renk", Color.class)));
		REGISTRY.add("sistem:mesaj_kayıdı",		new LogLevel(3, "MESAJ",	REGISTRY.get("sistem:mor_renk", Color.class)));
		REGISTRY.add("sistem:girdi_kayıdı",		new LogLevel(4, "GİRDİ",	REGISTRY.get("sistem:yeşil_renk", Color.class)));
		REGISTRY.add("sistem:ayıklama_kayıdı",	new LogLevel(5, "AYIKLAMA",	REGISTRY.get("sistem:açık_gri_renk", Color.class)));
		
		// Register fonts
		REGISTRY.add("sistem:consolas_20", new Font("Consolas", Font.ITALIC, 20));
		
		// Register commands
		REGISTRY.add("sistem:yansıt", new Eco(), Command.class);
		
		// Initialize fields
		allLogs = new ArrayList<>();
		visibleLogs = new ArrayList<>();
		consoleFont = REGISTRY.get("sistem:consolas_20", Font.class);
		graphics = ((AWTRenderer) Engine.renderer).graphics;
		input = "";
		inputColor = REGISTRY.get("sistem:yeşil_renk", Color.class);
		inputCadetColor = REGISTRY.get("sistem:beyaz_renk", Color.class);
		
		// Set the date color
		Log.dateColor = REGISTRY.get("sistem:beyaz_renk", Color.class);
		graphics.setBackground(REGISTRY.get("sistem:gri_renk", Color.class));
		
		log(new Log(REGISTRY.get("sistem:bilgi_kayıdı", LogLevel.class), "Sistem yüklendi!"));
	}

	@Override
	public void save()
	{
		
	}

	@Override
	public void frame()
	{
		int x = 10;
		int y = 10;
		
		graphics.setFont(consoleFont);
		FontMetrics metrics = graphics.getFontMetrics();
		
		if (RENDERER.getInput().keyPressed[KeyEvent.VK_F1])
			
			hideDate = !hideDate;
		
		if (RENDERER.getInput().keyPressed[KeyEvent.VK_F2])
			
			hideTime = !hideTime;
		
		synchronized (allLogs)
		{
			for (Log log : visibleLogs)
			{
				y += metrics.getHeight();
				x = 10;
				
				for (int i = 0; i < log.texts.size(); i++)
				{
					if ((hideDate && i == 0) || (hideTime && i == 1)) continue;
					
					Text text = log.texts.get(i);
					
					graphics.setColor(text.color);
					graphics.drawString(text.string, x, y);
					x += metrics.stringWidth(text.string);
				}
			}
		}
		
		graphics.setColor(inputColor);
		graphics.drawString("> " + input, 10, RENDERER.dimension.y - 10);
		
		float time = Engine.nanoTime();
		
		if ((time - inputCadetLastMovementTime) / 1000000000.0F < 0.5F || (int) Math.floor(time / 1000000000.0F * 3.0F) % 2 == 0)
		{
			graphics.setColor(inputCadetColor);
			
			if (inputInsert)
			
				graphics.drawString("_", 10 + metrics.stringWidth("> " + input.substring(0, inputCadet)), RENDERER.dimension.y - 10);
			
			else
			
				graphics.drawString("|", 10 + metrics.stringWidth("> " + input.substring(0, inputCadet)) - metrics.charWidth('|') / 2, RENDERER.dimension.y - 10);
		}
	}
	
	public synchronized void input(KeyEvent e)
	{
		char c = e.getKeyChar();
		int k = e.getKeyCode();
		
		if (k == KeyEvent.VK_ENTER)
		{
			if (input.length() > 0)
			{
				log(new Log(REGISTRY.get("sistem:girdi_kayıdı", LogLevel.class), input));
				
				String[] words = input.split(" ");
				String cmd = words[0];
				
				if (cmd.indexOf(':') < 0)
					
					cmd = "sistem:" + cmd;
				
				Command command = REGISTRY.get(cmd, Command.class);
				
				if (command == null)
				{
					log(new Log(REGISTRY.get("sistem:hata_kayıdı", LogLevel.class), "Komut \"" + cmd + "\" bulunamadı!"));
				}
				
				else
				{
					String[] args = new String[words.length - 1];
					System.arraycopy(words, 1, args, 0, args.length);
					command.run(args);
				}
				
				input = "";
				inputCadet = 0;
			}
		}
		
		else if (k == KeyEvent.VK_BACK_SPACE)
		{
			if (inputCadet > 0)
			{
				input = input.substring(0, inputCadet - 1) + input.substring(inputCadet);
				inputCadet--;
			}
		}
		
		else if (k == KeyEvent.VK_DELETE)
		{
			if (inputCadet < input.length())
			{
				input = input.substring(0, inputCadet) + input.substring(inputCadet + 1);
			}
		}
		
		else if (k == KeyEvent.VK_LEFT)
		{
			if (inputCadet > 0)
			{
				inputCadet--;
				inputCadetLastMovementTime = Engine.nanoTime();
			}
		}
		
		else if (k == KeyEvent.VK_RIGHT)
		{
			if (inputCadet < input.length())
			{
				inputCadet++;
				inputCadetLastMovementTime = Engine.nanoTime();
			}
		}
		
		else if (k == KeyEvent.VK_INSERT)
		{
			inputInsert = !inputInsert;
		}
		
		else if (validChar(c))
		{
			if (inputInsert && inputCadet != input.length()) input = input.substring(0, inputCadet) + c + input.substring(inputCadet + 1);
			else input = input.substring(0, inputCadet) + c + input.substring(inputCadet);
			inputCadet++;
		}
	}
	
	public void log(Log... logs)
	{
		synchronized (allLogs)
		{
			for (Log log : logs)
			{
				allLogs.add(log);
				visibleLogs.add(log);
			}
		}
	}
	
	public void clear()
	{
		visibleLogs.clear();
	}
	
	public boolean validChar(char c)
	{
		return Character.isAlphabetic(c) || Character.isDigit(c)
				|| c == ':'
				|| c == '_'
				|| c == '-'
				|| c == '+'
				|| c == '₺'
				|| c == '"'
				|| c == '\''
				|| c == ','
				|| c == '('
				|| c == ')'
				|| c == ' ';
	}
}
