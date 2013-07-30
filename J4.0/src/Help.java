
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;
import java.util.Enumeration;

public class Help {
  private static Properties   m_properties = new Properties();
  private static String m_propFilePath = new String("~");
  private static String m_propFileName = new String("~");
  private static boolean m_bInit=false;
  static void init()throws IOException{
    if(m_bInit==true)
      return;
    else{
      m_bInit=true;
      String userDir = System.getProperty("user.dir");
      m_propFilePath = userDir;
      m_propFileName = "system.properties";
      loadProperties();
    }
  }
  /********************************************************************************
  **  Private methods
  *********************************************************************************/
  private static void loadProperties()throws FileNotFoundException,
                                             IOException
    {
          //refresh properties
          m_properties = new Properties();
          try
            {
              loadProperties(m_properties, new File(m_propFilePath, m_propFileName));
            }
          catch(FileNotFoundException e)
            {
              throw  e;
            }
          catch(IOException e)
            {
              throw  e;
            }
       }
      /********************************************************************************
         **  Read from the given file and fill keys in the given property hashtable
         *********************************************************************************/
      private static void loadProperties(Properties theProperties, File theFile)throws FileNotFoundException,IOException
      {
        FileInputStream stream = null;
        try
        {
          stream = new FileInputStream(theFile);
        }
        catch(FileNotFoundException e)
        {
          throw e;
        }
        try
        {
          theProperties.load(stream);
        }
        catch (IOException e)
        {
          throw e;
        }
        finally
        {
          try
          {
            if (stream != null)
              stream.close();     // Make sure the stream is closed promptly...
          }
          catch (IOException e)
          {
            // do nothing...
          }
        }
        // Remove any leading/trailing blanks from the property values
        Enumeration e = theProperties.propertyNames();
        while (e.hasMoreElements())
        {
          Object o = e.nextElement();
          String key = (String)o;
          String val = theProperties.getProperty(key);
          String trimmedVal = val.trim();
          if (!val.equals(trimmedVal))
          {
            theProperties.put(key,trimmedVal);
          }
        }
      }
      /***************************************************************************
      *   Gets the specified property value as a stirng.
      ***************************************************************************/
      public static synchronized String getString (String key, String def) throws IOException
        {
         if(m_bInit == false)
            {
              init();
            }

          return (m_properties.getProperty(key, def));
      }
      /***************************************************************************
       *   gets the specified property value as a integer.
       ***************************************************************************/
       public static int getInt (String key, int def) throws IOException
         {
           String buf = getString(key, "~");
         int theInt = def;
         try
         {
           theInt = Integer.parseInt(buf);
         }
         catch(NumberFormatException e)
         {
         }
           return (theInt);
      }
       public static double getDouble (String key, int def) throws IOException
       {
         String buf = getString(key, "~");
       int theInt = def;
       double res=0.0;
       try
       {
         res = Double.parseDouble(buf);
       }
       catch(NumberFormatException e)
       {
       }
         return (res);
    }
}

