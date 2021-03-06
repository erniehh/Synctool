package theakki.synctool.Job.ConnectionTypes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import theakki.synctool.Job.NamedConnectionHandler;

/**
 * Created by theakki on 27.03.18.
 */

public abstract class StoredBase
{
    protected String _LocalPath = "";
    //protected int _Port = 0;

    protected String _ConnectionName = "";

    final private String TAG_LocalPath = "LocalPath";
    //final private String TAG_Port = "Port";

    final private String TAG_ConnectionName = "ConnectionName";

    public final static String ANONYMOUS_LOGIN = "Anonymous";


    protected void setValue(Element Node)
    {
        final String name = Node.getNodeName();
        if(name.compareToIgnoreCase(TAG_ConnectionName) == 0)
        {
            _ConnectionName = Node.getTextContent();
        }
        else if(name.compareToIgnoreCase(TAG_LocalPath) == 0)
        {
            _LocalPath = Node.getTextContent();
        }
        else
        {
            throw new IllegalArgumentException("Unknown Node '" + name + "'");
        }
    }

    protected void appendSettings(Document doc, Element node)
    {
        // Connection Name
        Element connectName = doc.createElement(TAG_ConnectionName);
        connectName.setTextContent(_ConnectionName);
        node.appendChild(connectName);

        // Local Path
        Element localPath = doc.createElement(TAG_LocalPath);
        localPath.setTextContent(_LocalPath);
        node.appendChild(localPath);
    }


    public boolean isAnonymous()
    {
        return ANONYMOUS_LOGIN.equals(_ConnectionName);
    }


    public String User()
    {
        return NamedConnectionHandler.getInstance().getConnection(_ConnectionName).User;
    }
    public void User(String user)
    {
        NamedConnectionHandler.Connection temp = NamedConnectionHandler.getInstance().getConnection(_ConnectionName);
        temp.User = user;
        NamedConnectionHandler.getInstance().update(_ConnectionName, temp);
    }


    public String Password()
    {
        if(isAnonymous())
            return "";

        return NamedConnectionHandler.getInstance().getConnection(_ConnectionName).Password;
    }
    public void Password(String password)
    {
        NamedConnectionHandler.Connection temp = NamedConnectionHandler.getInstance().getConnection(_ConnectionName);
        temp.Password = password;
        NamedConnectionHandler.getInstance().update(_ConnectionName, temp);
    }


    public String Url()
    {
        return NamedConnectionHandler.getInstance().getConnection(_ConnectionName).Url;
    }
    public void Url(String url)
    {
        NamedConnectionHandler.Connection temp = NamedConnectionHandler.getInstance().getConnection(_ConnectionName);
        temp.Url = url;
        NamedConnectionHandler.getInstance().update(_ConnectionName, temp);
    }


    public int Port()
    {
        return NamedConnectionHandler.getInstance().getConnection(_ConnectionName).Port;
    }
    public void Port(int port)
    {
        NamedConnectionHandler.Connection temp = NamedConnectionHandler.getInstance().getConnection(_ConnectionName);
        temp.Port = port;
        NamedConnectionHandler.getInstance().update(_ConnectionName, temp);
    }


    public String LocalPath(){ return _LocalPath; }
    public void LocalPath(String path){ _LocalPath = path; }

    public String ConnectionName()
    {
        return _ConnectionName;
    }
}
