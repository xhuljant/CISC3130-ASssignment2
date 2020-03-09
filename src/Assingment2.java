import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Assingment2 {

    public static void main(String[] args) {


        //scanner to read input from the user
        Scanner in = new Scanner(System.in);

        //making a que of strings with file names so that if user has one more than file they can all be read in in the order they were typed in
        Queue<String> filePaths = new LinkedList<>();

        //making a main linked list that will hold songs from all files that user wants to read from
        LinkedList<String> allSongs = new LinkedList<>();

        //using a stack to hold recent songs, if the stack grows above a certain size then the first song in that was listened to will be removed.
        Stack<String> recentSongs = new Stack<>();

        //taking input from user as to what files will be read
        System.out.println("Enter the file paths of all the files.(Type 'DONE' when done)");

        //running a loop that allows user to enter as many strings as they would like before stopping loop with 'done' keyword
        while (true) {
            String temp = in.next();
            //if the user doesnt type in done then the loop will continue and the typed line will be added to the filepath queue
            if (temp.equalsIgnoreCase("done"))
                break;
            filePaths.add(temp);
        }

        //if there is at least one file that the user entered in then the program will run
        while (filePaths.peek() != null) {

            //trys to create a new file with the file path that the user entered in
            try {
                BufferedReader br = new BufferedReader(new FileReader(filePaths.remove()));
                String line;

                //readsa line by line of the csv file and seperates based on commas to find the right information
                while ((line = br.readLine()) != null) {
                    String[] lineArr = line.split(",(?![^()]*\\))", -1);
                    String song = "";

                    //the second spot in the array is the song so the loop places that value into string var
                    for (int i = 0; i < lineArr.length; i++) {
                        if (i == 1)
                            song = lineArr[1];

                        //in order to make formatting and sorting easier, the program checks to see if the first char of song is " and removes it if it is
                        if (song.startsWith("\"")) {
                            String temp = song.substring(1, song.length() - 1);
                            song = temp;
                        }
                    }

                    //adds song to all songs linked list if its a actual song and not a empty line
                    if (!song.equalsIgnoreCase("track name") && !song.equalsIgnoreCase(""))
                        allSongs.add(song);
                }

              //catching a incorrect file path and stops program
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Program will terminate.");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //taking all songs that have been read from multiple files and sorting them from lowest to highest
        Collections.sort(allSongs, new songCompare());

        //starts 'playing' the songs read in from the files. User can stop playing songs by pressing any key
        System.out.println("The playlist will start playing. Hit 'Enter' to continue, hit any other button to stop.");
        String temp;
        while ((temp = in.nextLine()).equalsIgnoreCase("")) {


            //if the entered input from user is anything but enter, the loop stops
            if (!temp.equalsIgnoreCase(""))
                break;

            //loop terminates when theres no more songs to play
            if (allSongs.peek() == null) {
                System.out.println("No more songs in playlist.");
                break;
            }

            //plays a song and removes it from playlist
            String tempSong = allSongs.remove();
            System.out.print("PLaying : " + tempSong);

            //checks to see the size of the recents playlist, deletes first song added if the size is of recents playlist is bigger than 25
            if (recentSongs.size() < 25) {
                recentSongs.add(tempSong);
            } else {
                recentSongs.pop();
                recentSongs.push(tempSong);
            }
        }

        //shows the last n played songs
        System.out.println("\nThese are the last " + recentSongs.size() + " played songs:");

        int tempStackSize=recentSongs.size();
        for(int i=0;i<tempStackSize;i++){
            System.out.println(recentSongs.pop());
        }

    }
}

    //overrode compare class to make sorting linked list easier
    class songCompare implements Comparator<String> {

        @Override
        public int compare(String a, String b) {

            if (a.compareToIgnoreCase(b) > 0)
                return 1;
            else if (a.compareToIgnoreCase(b) == 0)
                return 0;
            else
                return -1;
        }
    }
