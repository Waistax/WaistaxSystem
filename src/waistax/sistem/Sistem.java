package waistax.sistem;

import waistax.engine.*;
import waistax.engine.renderer.*;
import waistax.math.*;
import waistax.registry.*;

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
	public static final String VERSION = "0.1";
	
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
					public void keyTyped(KeyEvent e)
					{
						SISTEM.input(e.getKeyChar());
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
		REGISTRY.add("sistem:ayıklama_kayıdı",	new LogLevel(4, "AYIKLAMA",	REGISTRY.get("sistem:açık_gri_renk", Color.class)));
		
		// Register fonts
		REGISTRY.add("sistem:consolas_20", new Font("Consolas", Font.PLAIN, 20));
		
		// Initialize fields
		allLogs = new ArrayList<>();
		visibleLogs = new ArrayList<>();
		consoleFont = REGISTRY.get("sistem:consolas_20", Font.class);
		graphics = ((AWTRenderer) Engine.renderer).graphics;
		input = "";
		inputColor = REGISTRY.get("sistem:yeşil_renk", Color.class);
		
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
		return Character.isAlphabetic(c)
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
	
	public synchronized void input(char c)
	{
		if (c == '\n')
		{
			if (input.length() > 0)
			{
				log(new Log(REGISTRY.get("sistem:mesaj_kayıdı", LogLevel.class), input));
				input = "";
			}
		}
		
		else if (c == '\b')
		{
			if (input.length() > 0)
			{
				input = input.substring(0, input.length() - 1);
			}
		}
		
		else if (validChar(c))
		{
//			LogLevel debugLevel = REGISTRY.get("sistem:ayıklama_kayıdı", LogLevel.class);
//			Text firstPart = new Text("Karakter girildi ", debugLevel.color);
//			Text charPart = new Text("" + c, REGISTRY.get("sistem:sarı_renk", Color.class));
//			log(new Log(debugLevel, firstPart, charPart));
			input += c;
		}
	}
}
