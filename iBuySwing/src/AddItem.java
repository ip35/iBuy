import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.WebAuthSession;


public class AddItem extends JFrame implements ActionListener{
	private DropboxAPI<WebAuthSession> mDBApi;
	private JButton cancel = new JButton("Cancel");
	private JButton add = new JButton("Add");
	private String user, filename;
	private Item item;
	
	public AddItem(String user, String filename, DropboxAPI<WebAuthSession> mDBApi) 
	{
		super("iBuy - Add Item");
		this.user = user;
		this.filename = filename;
		this.item = new Item("Apple", "Food", "Grocery", 1, false);
		this.mDBApi = mDBApi;
		setSize(500,300);
        
        add.addActionListener(this);
        cancel.addActionListener(this);
        
        setLayout(new GridLayout(5,2));
	    add(new JTextField("Name"));
	    add(item.nameField);
	    add(new JTextField("Category"));
	    add(item.categoryField);
	    add(new JTextField("Store"));
	    add(item.storeField);
	    add(new JTextField("Importance (1=low and 9=high)"));
	    add(item.importanceField);
	    add(cancel);
	    add(add);
		
        setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancel)
  		{
			new List(user, filename, mDBApi);
  			setVisible(false);
  			dispose();
  		}
		if(e.getSource() == add)
  		{
			String name = Global.toFileName(item.nameField.getText());
			String category = Global.toFileName(item.categoryField.getText());
			String store = Global.toFileName(item.storeField.getText());
			String importance = item.importanceField.getText();
  			if(name.equals("") || category.equals("") || store.equals("") || importance.equals(""))
  				System.out.println("Please complete all fields");
  			else
  			{
  				//Append item parameters to end of list
  				String list = Global.getFile(mDBApi, "/" + user + "/" + Global.toFileName(filename) + ".txt");
  				list += name + "\t" + category + "\t" + store + "\t" + importance + "\tfalse\n";
  				Global.putFileOverwrite(mDBApi, "/" + user + "/" + Global.toFileName(filename) + ".txt", list);
  				
  				//Update list date
  				StringTokenizer st = new StringTokenizer(Global.getFile(mDBApi, "/" + user + "/lists.txt"));
				String newList = "";
				while(st.hasMoreTokens())
				{
					String token = st.nextToken();
					String token2 = st.nextToken();
					String token3 = st.nextToken();
					if(token.equals(Global.toFileName(filename)))
					{
						Date d = new Date(System.currentTimeMillis());
						newList += token + "\t" + token2 + "\t" + Global.toFileName(d.toString())  + "\n";
					}
					else
						newList += token + "\t" + token2 + "\t" + token3 + "\n";
				}
  				Global.putFileOverwrite(mDBApi, "/" + user + "/lists.txt", newList);
  				
  				new List(user, filename, mDBApi);
  	  			setVisible(false);
  	  			dispose();
  			}
  		}
	}
}
