package rss2db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class simpleDBSource implements DBSource {

  protected String m_url;
  protected String m_user;
  protected String m_passwd;

  public simpleDBSource() throws IOException, ClassNotFoundException {
    this("jdbc.properties");
  }

  public simpleDBSource(String configFile) throws IOException, ClassNotFoundException {
    // TODO Auto-generated constructor stub

    String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    Properties props = new Properties();
    props.load(new FileInputStream(path + configFile));

    m_url = props.getProperty("url");
    m_user = props.getProperty("user");
    m_passwd = props.getProperty("passwd");

    Class.forName(props.getProperty("driver"));
  }

  @Override
  public Connection getConnection() throws SQLException {
    // TODO Auto-generated method stub
    return DriverManager.getConnection(m_url, m_user, m_passwd);
  }

  @Override
  public void closeConnection(Connection conn) throws SQLException {
    // TODO Auto-generated method stub
    conn.close();
  }

}
