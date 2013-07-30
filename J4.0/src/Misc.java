/*
* The contents of this file are subject to the BT "ZEUS" Open Source 
* Licence (L77741), Version 1.0 (the "Licence"); you may not use this file 
* except in compliance with the Licence. You may obtain a copy of the Licence
* from $ZEUS_INSTALL/licence.html or alternatively from
* http://www.labs.bt.com/projects/agents/zeus/licence.htm
* 
* Except as stated in Clause 7 of the Licence, software distributed under the
* Licence is distributed WITHOUT WARRANTY OF ANY KIND, either express or 
* implied. See the Licence for the specific language governing rights and 
* limitations under the Licence.
* 
* The Original Code is within the package zeus.*.
* The Initial Developer of the Original Code is British Telecommunications
* public limited company, whose registered office is at 81 Newgate Street, 
* London, EC1A 7AJ, England. Portions created by British Telecommunications 
* public limited company are Copyright 1996-9. All Rights Reserved.
* 
* THIS NOTICE MUST BE INCLUDED ON ANY COPY OF THIS FILE
*/



import java.awt.*;
import java.util.*;
import java.io.*;


public class Misc {
   static final String DELIMITERS = "\n\r\t !%^&*()-+={[]}~,.:;#|\\";
   public static final String OPAQUE_CHAR = "#";
   public static final String QUOTE = "\"";

   public static String literalToString(String s) {
      if ( s.startsWith(QUOTE) && s.endsWith(QUOTE) )
         return unescape(s.substring(1,s.length()-1));
      else
         return unescape(s);
   }
   public static String opaqueToString(String s) {
      return s.substring(1,s.length()-1);
   }

   public static String escape(String str) {
      StringBuffer retval = new StringBuffer();
      char ch;
      for (int i = 0; i < str.length(); i++) {
        switch (str.charAt(i)) {
           case 0 :
              continue;
           case '\b':
              retval.append("\\b");
              continue;
           case '\t':
              retval.append("\\t");
              continue;
           case '\n':
              retval.append("\\n");
              continue;
           case '\f':
              retval.append("\\f");
              continue;
           case '\r':
              retval.append("\\r");
              continue;
           case '\"':
              retval.append("\\\"");
              continue;
           case '\'':
              retval.append("\\\'");
              continue;
           case '\\':
              retval.append("\\\\");
              continue;
           default:
              if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
                 String s = "0000" + Integer.toString(ch, 16);
                 retval.append("\\u" + s.substring(s.length() - 4, s.length()));
              } else {
                 retval.append(ch);
              }
              continue;
        }
      }
      return retval.toString();
   }

   public static final String unescape(String str) {
      StringBuffer retval = new StringBuffer();
      char ch;
      for(int i = 0; i < str.length(); ) {
         ch = str.charAt(i++);
         if ( ch != '\\' )
            retval.append(ch);
         else {
           ch = str.charAt(i++);
           switch(ch) {
           case 0 :
              continue;
           case 'b':
              retval.append("\b");
              continue;
           case 't':
              retval.append("\t");
              continue;
           case 'n':
              retval.append("\n");
              continue;
           case 'f':
              retval.append("\f");
              continue;
           case 'r':
              retval.append("\r");
              continue;
           case '"':
              retval.append("\"");
              continue;
           case '\'':
              retval.append("\'");
              continue;
           case '\\':
              retval.append("\\");
              continue;
           default:
              // Don't know leave as is
              retval.append("\\");
              retval.append(ch);
              continue;
           }
         }
      }
      return retval.toString();
   }


   public static String relativePath(String file) {
      if ( file == null ) return null;
      return relativePath(System.getProperty("user.dir"),file);
   }
   public static String relativePath(String dir, String file) {
      if ( file == null ) return null;
      return relativePath(dir,new File(file));
   }
   public static String relativePath(String dir, File file) {
      if ( file == null ) return null;

      if ( dir == null )
         dir = System.getProperty("user.dir");

      File d = new File(dir);
      try {
          dir = d.getCanonicalPath();
      }
      catch(IOException e) {
         dir = d.getAbsolutePath();
      }

      String filename;
      try {
         filename = file.getCanonicalPath();
      }
      catch(IOException e) {
         filename = file.getAbsolutePath();
      }
      File f;

      int count = 0;
      while( dir != null ) {
        if ( filename.startsWith(dir) && !dir.equals(File.separator) ) {
           String relpath = "";
           for(int i = 0; i < count; i++ ) {
              relpath += "..";
              if ( i+1 < count )
                 relpath += File.separator;
           }
           relpath += filename.substring(dir.length());
           if ( relpath.startsWith(File.separator) )
              return "." + relpath;
           else if ( relpath.equals("") )
              return ".";
           else
              return relpath;
        }
        count++;
        f = new File(dir);
        dir = f.getParent();
      }
      if ( filename.equals("") )
         return ".";
      else
         return filename;
   }

   public static String spaces(int sp ) {
      String tabs = "";
      for( int i = 0; i < sp; i++ ) tabs += " ";
      return tabs;
   }
   public static String concat(String prefix, int[] data) {
      for(int i = 0; data != null && i < data.length; i++ )
         prefix += " " + data[i];
      return prefix;
   }
   public static String concat(int[] data) {
      String prefix = "";
      for( int i = 0; data != null && i < data.length; i++ )
         prefix += data[i] + " ";
      return prefix.trim();
   }
   public static String concat(String prefix, double[] data) {
      for(int i = 0; data != null && i < data.length; i++ )
         prefix += " " + data[i];
      return prefix;
   }
   public static String concat(double[] data) {
      String prefix = "";
      for( int i = 0; data != null && i < data.length; i++ )
         prefix += data[i] + " ";
      return prefix.trim();
   }

   public static String concat(String prefix, Object[] data) {
      for(int i = 0; data != null && i < data.length; i++ )
         prefix += " " + data[i];
      return prefix;
   }
   public static String concat(Object[] data) {
      String prefix = "";
      for( int i = 0; data != null && i < data.length; i++ )
         prefix += data[i] + " ";
      return prefix.trim();
   }
   public static String concat(String prefix, Vector data) {
      for(int i = 0; data != null && i < data.size(); i++ )
         prefix += " " + data.elementAt(i);
      return prefix;
   }
   public static String concat(Vector data) {
      String prefix = "";
      for( int i = 0; data != null && i < data.size(); i++ )
         prefix += data.elementAt(i) + " ";
      return prefix.trim();
   }
   public static String concat(HashSet data) {
      String prefix = "";
      
      Enumeration enum1 = Collections.enumeration(data);
      while( enum1.hasMoreElements() )
         prefix += enum1.nextElement() + " ";
      return prefix.trim();
   }

   public static String substitute(String in, String search, String replace) {
      StringTokenizer st = new StringTokenizer(in,DELIMITERS,true);
      String token;
      String s = "";
      while( st.hasMoreTokens() ) {
         token = st.nextToken();
         if ( token.equals(search) )
            s += replace;
         else
            s += token;
      }
      return s;
   }

   public static boolean member(int item, int[] List) {
      if ( List == null ) return false;
      for(int j = 0; j < List.length; j++ )
         if ( item == List[j] ) return true;
      return false;
   }
   public static boolean member(String item, String[] List) {
      if ( List == null ) return false;
      for(int j = 0; j < List.length; j++ )
         if ( item.equals(List[j]) ) return true;
      return false;
   }

   public static Vector intersection(Vector left, Vector right) {
      Vector result = new Vector();
      Object item;
      for(int j = 0; j < left.size(); j++ ) {
         item = left.elementAt(j);
         if ( !result.contains(item) && right.contains(item) )
            result.addElement(item);
      }
      return result;
   }
   public static Vector union(Vector left, Vector right) {
      Vector result = new Vector();      
      Object item;
      for(int j = 0; j < left.size(); j++ ) {
         item = left.elementAt(j);
         if ( !result.contains(item) )
            result.addElement(item);
      }
      for(int j = 0; j < right.size(); j++ ) {
         item = right.elementAt(j);
         if ( !result.contains(item) )
            result.addElement(item);
      }
      return result;
   }
   public static Vector difference(Vector left, Vector right) {
      Vector result = new Vector();
      Object item;
      for(int j = 0; j < left.size(); j++ ) {
         item = left.elementAt(j);
         if ( !result.contains(item) && !right.contains(item) )
            result.addElement(item);
      }
      return result;
   }
   public static boolean isSubset(Vector subset, Vector superset) {
      Object item;
      for(int j = 0; j < subset.size(); j++ ) {
         item = subset.elementAt(j);
         if ( !superset.contains(item) )
            return false;
      }
      return true;
   }

   public static boolean sameVector(Vector left, Vector right) {
      if ( left.size() != right.size() ) return false;

      Vector diff = difference(left,right);
      if ( !diff.isEmpty() ) return false;

      diff = difference(right,left);
      return diff.isEmpty();
   }

   public static boolean isNumber(String s) {
      return isLong(s) || isDouble(s);
   }
   public static boolean isLong(String s) {
      try {
         long x = Long.parseLong(s);
         return true;
      }
      catch(NumberFormatException e) {
         return false;
      }
   }
   public static boolean isDouble(String s) {
      try {
         double x = Double.parseDouble(s);
         return true;
      }
      catch(NumberFormatException e) {
         return false;
      }
   }

   public static void sort(String[] data) {
      if ( data == null ) return;
      boolean swapped = true;
      while( swapped ) {
         swapped = false;
         for( int i = 0; i < data.length-1; i++ )
            if ( data[i].compareTo(data[i+1]) > 0 ) {
               String tmp = data[i];
               data[i] = data[i+1];
               data[i+1] = tmp;
               swapped = true;
            }
      }
   }

   public static void sort(Vector data) {
      if ( data == null ) return;
      if ( data.isEmpty() ) return;
      if ( !( data.elementAt(0) instanceof String) ) return;

      boolean swapped = true;
      while( swapped ) {
         swapped = false;
         for( int i = 0; i < data.size()-1; i++ )
            if ( ((String)data.elementAt(i)).compareTo(
                 (String)data.elementAt(i+1)) > 0 ) {
               Object tmp = data.elementAt(i);
               data.setElementAt(data.elementAt(i+1),i);
               data.setElementAt(tmp,i+1);
               swapped = true;
            }
      }
   }

   public static int whichPosition(String data, String[] items) {
      if ( data != null && items != null )
         for( int i = 0; i < items.length; i++ ) {
            if ( data.equals(items[i]) ) return i;
         }
      return -1;
   }
   public static int whichPosition(String data, Vector items) {
      if ( data != null && items != null )
         for( int i = 0; i < items.size(); i++ ) {
            if ( data.equals((String)items.elementAt(i)) ) return i;
         }
      return -1;
   }

   public static Vector flatten(Vector data) {
      Vector result = new Vector();
      if ( data == null || data.isEmpty() ) return result;

      Object obj;
      for( int i = 0; i < data.size(); i++ ) {
         obj = data.elementAt(i);
         if ( obj instanceof Vector ) {
            Vector v = flatten((Vector)obj);
            for( int j = 0; j < v.size(); j++ )
               result.addElement(v.elementAt(j));
         }
         else
            result.addElement(obj);
      }
      return result;
   }

   public static Vector copyVector(Vector data) {
      if ( data == null ) return null;
      Vector result = new Vector();
      if ( data.isEmpty() ) return result;

      Object obj;
      for( int i = 0; i < data.size(); i++ ) {
         obj = data.elementAt(i);
         if ( obj instanceof Vector ) {
            Vector v = copyVector((Vector)obj);
            result.addElement(v);
         }
         else
            result.addElement(obj);
      }
      return result;
   }

   public static String[] stringArray(String s) {
      StringTokenizer st = new StringTokenizer(s);
      Vector data = new Vector();
      while( st.hasMoreTokens() )
         data.addElement(st.nextToken());
      return stringArray(data);
   }
   public static String[] stringArray(Object[] data) {
      if ( data == null ) return new String[0];
      String[] result = new String[data.length];
      for( int i = 0; i < data.length; i++ )
         result[i] = data[i].toString();
      return result;
   }
   public static String[] stringArray(Vector data) {
      if ( data == null ) return new String[0];
      String[] result = new String[data.size()];
      for( int i = 0; i < data.size(); i++ )
         result[i] = (String)data.elementAt(i);
      return result;
   }
   public static Vector stringVector(String[] data) {
      Vector result = new Vector();
      if ( data != null )
         for( int i = 0; i < data.length; i++ )
            result.addElement(data[i]);
      return result;
   }

   public static String decimalPlaces(double x, int num) {
      return decimalPlaces(Double.toString(x),num);
   }
   public static String decimalPlaces(String str,int num) {
      String exp = "";
      int index = str.indexOf("E");
      if ( index != -1 ) {
         exp = str.substring(index);
      }
      index = str.indexOf(".");
      if ( index == -1 ) {
         String cstr = str + ".";
         for( int i = 0; i < num; i++ )
            cstr += "0";
         return cstr+exp;
      }
      else {
         int len = str.length();
         if ( len > index + num )
            return str.substring(0,index+num+1)+exp;
         else {
            String cstr = str;
            for( int i = 0; i < num+index+1-len; i++ )
               cstr += "0";
            return cstr+exp;
         }
      }
   }

   public static boolean isZero(double x) {
      return Math.abs(x) < 1.0E-12;
   }
}
