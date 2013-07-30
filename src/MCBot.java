import org.jibble.pircbot.PircBot;


public class MCBot extends PircBot {
	private Interface ifc;
	String prefix;
	boolean showSender = true;
	boolean showChannel = false;
	public final static String DEFAULTSERVER = "irc.st-city.net";
	public final static String DEFAULTNICK = "MCBot";
	public final static String DEFAULTPREFIX = "+++";
	public final static String VERSION = "1.1";

	public MCBot(Interface ifc) {
		this.ifc = ifc;
		this.setLogin("mcb");
		this.setVersion("MCBot " + MCBot.VERSION);
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if(message.trim().substring(0, prefix.length()).equals(prefix)) {
			this.relayMessage(MCBot.trim(MCBot.trim(message).substring(prefix.length())), sender, channel);
		}
	}

	@Override
	public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
		if(target.equals(this.getNick()))
			this.relayMessage(MCBot.trim(notice), sourceNick);
	}
	
	@Override
	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		this.relayMessage(MCBot.trim(message), sender);
	}
	
	private void relayMessage(String message, String sender) {
		this.relayMessage(message, sender, null);
	}
	
	private void relayMessage(String message, String sender, String channel) {
		if(channel != null)
			ifc.addToLog(sender + channel + ": " + message);
		else
			ifc.addToLog(sender +  "#Query/Notice: " + message);
		if(showSender) {
			if(showChannel && channel != null) {
				message = "(" + sender + channel + ") " + message;
			} else {
				message = "(" + sender + ") " + message;
			}
		} else if(showChannel && channel != null) {
			message = "(" + channel + ") " + message;
		}
		
		String[] channels = this.getChannels();
		for (String c : channels) {
			if (!c.toLowerCase().equals(channel.toLowerCase())) {
				this.sendMessage(c, message);
			}
		}
	}
	
	public void setNick(String name) {
		this.setName(name);
		this.changeNick(name);
	}
	
	public static String trim(String str) {
		while(MCBot.isWhitespace(str.substring(0,1))) {
			str = str.substring(1);
		}
		while(MCBot.isWhitespace(str.substring(str.length() - 1, str.length()))) {
			str = str.substring(0, str.length() -1);
		}
		return str;
	}

	private static boolean isWhitespace(String str) {
		if(str.equals(" ")) return true;
		if(str.equals("\t")) return true;
		if(str.equals("\n")) return true;
		if(str.equals("\f")) return true;
		if(str.equals("\r")) return true;
		if(str.equals("\b")) return true;
		return false;
	}
}
