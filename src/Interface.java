import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.jibble.pircbot.NickAlreadyInUseException;

public class Interface implements ActionListener, WindowListener {

	private JFrame fenster;
	private JTabbedPane tabs;
	private JButton connect;
	private JButton addChan;
	private JButton delChan;
	private JTextField server;
	private JTextField nick;
	private JTextField prefix;
	private JCheckBox showNick;
	private JCheckBox showChan;
	private JList<String> channels;
	public JTextArea log;
	private DefaultListModel<String> channelmodel;
	public JScrollPane logp;
	private MCBot mcb;
	private JMenuItem connectmenu;
	private JButton changeNick;


	public Interface () {
		this.mcb = new MCBot(this);
		fensterErzeugen();
	}

	private void fensterErzeugen() {
		this.fenster = new JFrame("MCBot");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(fenster, "Ohh - Kaputt\nMacht aber nix.");
		}

		this.menueErzeugen();

		Container cp = this.fenster.getContentPane();
		cp.setLayout(new BorderLayout());


		tabs = new JTabbedPane();
		cp.add(tabs);

		JPanel allgp = new JPanel(new BorderLayout());
		JPanel auffueller = new JPanel();
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(0,2,10,10));
		tabs.addTab("Allgemein", allgp);
		allgp.add(auffueller);
		//			auffueller.setBackground(new Color(0,0,0));
		allgp.add(grid,BorderLayout.NORTH);

		JLabel item = new JLabel("Server");
		grid.add(item);
		server = new JTextField(MCBot.DEFAULTSERVER);
		grid.add(server);

		item = new JLabel("Botname");
		grid.add(item);
		nick = new JTextField(MCBot.DEFAULTNICK);
		grid.add(nick);

		item = new JLabel("Prefix");
		grid.add(item);
		prefix = new JTextField(MCBot.DEFAULTPREFIX);
		grid.add(prefix);

		item = new JLabel("Nick anzeigen");
		grid.add(item);
		showNick = new JCheckBox("", true);
		showNick.addActionListener(this);
		showNick.setActionCommand("updateShow");
		grid.add(showNick);

		item = new JLabel("Channel anzeigen");
		grid.add(item);
		showChan = new JCheckBox("", false);
		showChan.addActionListener(this);
		showChan.setActionCommand("updateShow");
		grid.add(showChan);

		this.connect = new JButton("Verbinden");
		connect.addActionListener(this);
		connect.setActionCommand("connect");
		grid.add(connect);

		this.changeNick = new JButton("Nick wechseln");
		this.changeNick.addActionListener(this);
		this.changeNick.setActionCommand("changeNick");
		this.changeNick.setEnabled(false);
		grid.add(this.changeNick);

		JPanel chanp = new JPanel(new BorderLayout());
		tabs.addTab("Channels", chanp);
		channelmodel = new DefaultListModel<String>();
		channels = new JList<String>(channelmodel);
		chanp.add(channels, BorderLayout.CENTER);


		JPanel buttonp = new JPanel(new GridLayout(0,2));
		chanp.add(buttonp, BorderLayout.SOUTH);

		addChan = new JButton("Hinzufügen");
		addChan.addActionListener(this);
		addChan.setActionCommand("addChan");
		buttonp.add(addChan);

		delChan = new JButton("Entfernen");
		delChan.addActionListener(this);
		delChan.setActionCommand("delChan");
		buttonp.add(delChan);


		log = new JTextArea("MCBot " + MCBot.VERSION);
		log.setEditable(false);
		logp = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabs.addTab("Log", logp);

		fenster.pack();
		fenster.setSize(500,500);
		fenster.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenster.addWindowListener(this);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		fenster.setLocation((d.width - fenster.getWidth()) / 2 , (d.height - fenster.getHeight()) / 2);

		fenster.setVisible(true);
	}

	private void menueErzeugen() {
		JMenuBar menubar = new JMenuBar();
		fenster.setJMenuBar(menubar);

		JMenu menu;
		JMenuItem item;

		menu = new JMenu("Bot");
		menubar.add(menu);

		item = new JMenuItem("Verbinden");
		item.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(this);
		item.setActionCommand("connect");
		item.setMnemonic('D');
		this.connectmenu = item;
		menu.add(this.connectmenu);

		menu.addSeparator();

		item = new JMenuItem("Beenden");
		item.addActionListener(this);
		item.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
		item.setActionCommand("quit");
		item.setMnemonic('B');
		menu.add(item);

		menu = new JMenu("?");
		menubar.add(menu);

		item = new JMenuItem("Info");
		item.addActionListener(this);
		item.setActionCommand("info");
		item.setMnemonic('I');
		menu.add(item);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("connect"))
			this.connect();
		else if(e.getActionCommand().equals("quit"))
			this.quit();
		else if(e.getActionCommand().equals("info"))
			this.info();
		else if(e.getActionCommand().equals("addChan"))
			this.addChan();
		else if(e.getActionCommand().equals("delChan"))
			this.delChan();
		else if(e.getActionCommand().equals("changeNick"))
			this.changeNick();
		else if(e.getActionCommand().equals("updateShow"))
			this.updateShow();
		else
			JOptionPane.showMessageDialog(fenster, e.getActionCommand(), "Action Command",JOptionPane.INFORMATION_MESSAGE);

	}

	private void updateShow() {
		this.mcb.showSender = this.showNick.isSelected();
		this.mcb.showChannel = this.showChan.isSelected();
		String logs = "";
		if(this.mcb.showSender) logs += " showNick";
		if(this.mcb.showChannel) logs += " showChan";		
		this.addToLog("Nick/Channel zeigen:" + logs);
	}

	private void connect() {
		if(!this.mcb.isConnected()) {
			this.setConnected(true);
			this.mcb.setNick(this.nick.getText().trim());
			this.mcb.prefix = this.prefix.getText().trim();
			this.mcb.showSender = this.showNick.isSelected();
			this.mcb.showChannel = this.showChan.isSelected();

			String logs = "Nick: " + this.nick.getText().trim() + " - Präfix: " + this.prefix.getText().trim();
			if(this.showNick.isSelected()) logs += " - showNick";
			if(this.showChan.isSelected()) logs += " - showChan";
			this.addToLog(logs);
			this.addToLog("Verbinde zu " + this.server.getText().trim() + "...");
//			this.mcb.setVerbose(true);
			try {
				this.mcb.connect(this.server.getText().trim());
			} catch (NickAlreadyInUseException e) {
				JOptionPane.showMessageDialog(fenster, "Nick wird bereits benutzt, wähle einen anderen.", "Fehler", JOptionPane.ERROR_MESSAGE);
				this.setConnected(false);
				this.addToLog("Nick in Verwendung --> Getrennt.");
				return;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(fenster, "Unbekannter Fehler.\n" + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
				this.setConnected(false);
				this.addToLog(e.getMessage() + " --> Getrennt.");
				return;
			}
			Object[] channels = this.channelmodel.toArray();
			String chans = "Betrete: ";
			for (Object c : channels) {
				this.mcb.joinChannel((String) c);
				chans += (String) c + " ";
			}
			this.addToLog(chans);
		} else {
			this.mcb.quitServer("MCBot " + MCBot.VERSION);
			this.addToLog("Verbindung Getrennt");
			this.setConnected(false);
		}
	}

	private void setConnected(boolean state) {
		if(state) {
			this.nick.setEnabled(false);
			this.server.setEnabled(false);
			this.prefix.setEnabled(false);
			this.connect.setText("Trennen");
			this.connectmenu.setText("Trennen");
			this.changeNick.setEnabled(true);
		} else {
			this.nick.setEnabled(true);
			this.server.setEnabled(true);
			this.prefix.setEnabled(true);
			this.connect.setText("Verbinden");
			this.connectmenu.setText("Verbinden");
			this.changeNick.setEnabled(false);
		}
	}

	public void addToLog(String string) {
		this.log.setText(this.log.getText() + "\n" + string);
		JScrollBar bar = this.logp.getVerticalScrollBar();
		bar.setValue( bar.getMaximum() );
	}

	private void quit() {
		mcb.quitServer("MCBot " + MCBot.VERSION);
		this.addToLog("Verbindung Getrennt");
		System.exit(0);
	}

	private void info() {
		JOptionPane.showMessageDialog(fenster, 
				"<html><body><h1>MCBot</h1>Bot für Multichannel-Rollenspiel<br>Version: " + MCBot.VERSION + "<br><br>F&uuml;r das Trekzone Network Rollenspiel<br>" + 
				"By Jason Myrdin alias Daniel Müllers<br>Basiert auf PIRCBOT - http://www.jibble.org/pircbot.php", "Über MCBot", JOptionPane.INFORMATION_MESSAGE);
	}

	private void addChan() {
		String chan = JOptionPane.showInputDialog(fenster, "Bitte Channel eingeben.", "Channel hinzufügen", JOptionPane.QUESTION_MESSAGE);
		if(!chan.isEmpty() && chan != null && !(channelmodel.contains(chan.trim()) || channelmodel.contains("#" + chan.trim()))) {
			if(!chan.substring(0, 1).equals("#"))
				chan = "#" + chan;
			this.channelmodel.addElement(chan.trim());
			this.mcb.joinChannel(chan);
			this.addToLog("Betrete: " + chan);
		}
		
	}

	private void delChan() {
		int[] indices = this.channels.getSelectedIndices();
		int k = 0;
		for(int i  : indices) {
			String chan = this.channelmodel.getElementAt(i - k);
			this.mcb.partChannel(chan);
			this.addToLog("Verlasse: " + chan);
			this.channelmodel.removeElement(chan);
			k++;
		}
	}
	
	private void changeNick() {
		String nick = JOptionPane.showInputDialog(fenster, "Bitte neuen Nick.", "Nick ändern", JOptionPane.QUESTION_MESSAGE);
		if(!nick.isEmpty() && nick != null) {
			this.mcb.setNick(nick);
			this.addToLog("Nick wechseln zu: " + nick);
		}
	}

	
	@Override
	public void windowClosed(WindowEvent e) {
		this.quit();
	}
	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
