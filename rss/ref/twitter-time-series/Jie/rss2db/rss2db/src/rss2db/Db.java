package rss2db;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author
 * 
 */
public class Db extends simpleDBSource {

  private Connection conn;
  private Statement stmt;

  /**
   * @param configFile
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public Db(String configFile) throws IOException, ClassNotFoundException, SQLException {
    super(configFile);
    // TODO Auto-generated constructor stub
  }


  /**
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Db() throws IOException, ClassNotFoundException {
    super();
    // TODO Auto-generated constructor stub
  }

  public void connectDb() throws SQLException {
    conn = (Connection) getConnection();
    stmt = conn.createStatement();
  }

  public void close() throws SQLException {
    stmt.close();
    this.closeConnection(conn);
  }

  public int addToDB(String url, String source, String title, String content) {
    String sql;
    try {
        sql =
            String.format("INSERT INTO news (url, source, title, content) "
                + "VALUES('%s', '%s', '%s', '%s')", 
                url, source, title, content);
        //System.out.println(sql);
        stmt.executeUpdate(sql);
    } catch (SQLException e) {
    }
    return 0;
  }

}
