package MainPackage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * My tools to make life easier
 * Reading text files, finding line number of given words,
 * read line at index in file (example: read 10th line of given file)
 * read multiple lines at given indexes
 * write to file array of lines
 */
public class Tools {

    //- - - - - - - - - - - -   READ FILE    - - - - - - - - - - - -
    public static synchronized ArrayList<String> readFile(String filePath){
        ArrayList<String> lines=new ArrayList<>();
        Scanner scanner;
        try{
            File file =new File(filePath);
            scanner=new Scanner(file);
            while(scanner.hasNext()){
                lines.add(scanner.nextLine());
            }
            scanner.close();
            return lines;
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //- - - - - - - - - - -    READ SEPARATE LINE    - - - - - - - - - - - - -

    /**
     * Read given number of the line in txt file
     * @param index number of the line that should be read
     * @param filePath Path to a file
     * @return line of text, if not found return empty string
     */
    public static synchronized String readAtIndex(int index,String filePath){
        String line;
        Scanner scanner;
        try{
            File file =new File(filePath);
            scanner=new Scanner(file);
            int i=0;
            while(scanner.hasNext()){
                line=scanner.nextLine();
                if(i==index) return line;
            }
            scanner.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    //- - - - - - - - - - -    READ FROM INDEX TO INDEX    - - - - - - - - - - - - -

    /**
     * Read multiple lines from a file
     * Example: first line 2, last line 5
     * returns 2,3,4 and 5 line as a List of strings
     * @param startIndex First line to be read
     * @param lastIndex Last line to be read
     * @param filePath File path
     * @return List containing strings, which are from first requested line to requested last line
     */
    public static synchronized ArrayList<String> readMultipleLines(int startIndex,int lastIndex,String filePath){
        ArrayList<String> lines=new ArrayList<>();
        Scanner scanner;
        try{
            File file =new File(filePath);
            scanner=new Scanner(file);
            int index=0;
            while(scanner.hasNext()&&index<=lastIndex){
                if(index>=startIndex){
                    lines.add(scanner.nextLine());
                }
                else scanner.nextLine();
                index++;
            }
            scanner.close();
            return lines;
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //- - - - - - - - - - - -    GET INDEX OF WORD    - - - - - - - - -

    /**
     * Searching for a word, if found - returns its index
     * @param word String of searching word
     * @param filePath Path to a file
     * @return number of a line where word was found
     */
    public static synchronized int getIndexOfWord(String word,String filePath){
        ArrayList<String> lines= readFile(filePath);
        for(int i=0;i<lines.size();i++){
            if(lines.get(i).contains(word)) return i;
        }
        return -1;
    }

    //- - - - - - - - - - - -   WRITE TO FILE    - - - - - - - - - - - -

    /**
     * Append list of strings to existing file, each list is a new line in txt file
     * @param lines list of strings
     * @param filePath path to a file
     */
    public static synchronized void writeFile(ArrayList<String> lines, String filePath){
        try{
            FileWriter fw=new FileWriter(filePath);
            fw.write("");
            for(String s:lines){
                fw.append(s);
                fw.append("\n");
            }
            fw.close();
        }catch(IOException e){e.printStackTrace();}
    }
}
