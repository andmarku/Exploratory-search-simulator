import java.io.PrintWriter;

import java.io.IOException;

// TODO: 2019-10-29 save results to csv file
// TODO: 2019-10-29 fix curl stuff so that I can use the elastic search api properly
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Running");
        Simulator.mySimulator();
        System.out.println("Finished");
    }

    private static boolean runTests(int numberOfCalls) throws IOException {

        //PrintWriter out = new PrintWriter("testTimeSearch.txt");
        int numberOfLines = 0;
        for(int i=0;i<numberOfCalls; i++){
            if(i%100000 == 0){
                System.out.println("Reached iteration: " + i);
            }
        }
        System.out.println(numberOfLines);
        //out.close();
        return true;
    }

}// end of class
