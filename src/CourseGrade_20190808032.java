import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
//Author: Ahmet Can DERICIOGLU
//Date: 16.01.2021

public class CourseGrade_20190808032 {

    public static String gradeLetter(double grade){
        if(grade>=88)
            return "AA";
        else if (grade>=81)
            return "BA";
        else if (grade>=74)
            return "BB";
        else if (grade>=67)
            return "CB";
        else if (grade>=60)
            return "CC";
        else if (grade>=53)
            return "DC";
        else if (grade>=46)
            return "DD";
        else if (grade>=35)
            return "FD";
        else
            return "FF";
    }
    public static double gpaPoints(double grade){
        if(grade>=88)
            return 4.0;
        else if(grade>=81)
            return 3.5;
        else if(grade>=74)
            return 3.0;
        else if(grade>=67)
            return 2.5;
        else if(grade>=60)
            return 2.0;
        else if(grade>=53)
            return 1.5;
        else if(grade>=46)
            return 1.0;
        else if(grade>=35)
            return 0.5;
        else return 0.0;
    }
    public static String status(double grade){
        if(grade>=60)
            return "passed";
        else if(grade>=46)
            return "conditionally passed";
        else return "failed";
    }
    public static int countCategory(String filename) throws Exception{
        Scanner asd = new Scanner(new File(filename));
        int count = 0;
        while (asd.hasNext()){
            asd.nextLine();
            count++;
        }
    return count;}
    public static void getCategory(String[] category,int[] quantity,int[]
            weight, String filename) throws Exception{
        Scanner asd = new Scanner(new File(filename));
        int i = 0;
        while(asd.hasNext()){
            category[i] = asd.next();
            quantity[i] = asd.nextInt();
            weight[i] = asd.nextInt();
            i++;
        }
        asd.close();
    }
    public static void writeGrades(String[] student,double[] grades,
                                   String basefilename) throws Exception{
        String C = basefilename + "_CourseDetails.txt";
        String S = basefilename + "_StudentScores.txt";
        Scanner course = new Scanner(new File(C));
        Scanner scores = new Scanner(new File(S));

        String[] category = new String[countCategory(C)];
        int[] quantity = new int[countCategory(C)];
        int[] weight = new int[countCategory(C)];
        getCategory(category, quantity, weight, C);


        int totalquantity = 0;
        for (int i = 0; i < quantity.length; i++)
            totalquantity += quantity[i];

        double[][] allgrades = new double[student.length][totalquantity];

        for (int i = 0;i<student.length;i++){
            int totalscore = 0;
            scores.next();
            while(scores.hasNextDouble()){
                totalscore++;
                scores.nextDouble();
            }
            if (totalscore!= totalquantity){
                PrintWriter output2 = new PrintWriter(basefilename+
                        "_log.txt");
                output2.println("ERROR: missed score - " +
                        "please declare all scores");
                output2.close();
            System.exit(1);
            }
        }
        scores.close();
        scores =new Scanner(new File(S));

        for (int i = 0; i < countCategory(S); i++) {
            if (scores.hasNext())
                student[i] = scores.next();
            int j = 0;
            while (scores.hasNextDouble()) {
                allgrades[i][j] = scores.nextDouble();
                j++;
            }
        }
        for (int i = 0; i < student.length; i++){
            int z = 0;
            double num2 = 0;
            for (int j = 0; j < quantity.length; j++){
                double num1 = 0;
                for (int k = 0; k < quantity[j]; k++,z++){
                    num1 += allgrades[i][z];
                }
                num2 = ((num1/quantity[j])*(weight[j]))/100;
                grades[i]+=num2;
            }
        }
        for (int i = 0;i<student.length;i++){
            for (int j = 0;j<allgrades[i].length;j++)
                if (allgrades[i][j]<0){
                    grades[i]=-grades[i];
                    break;
                }
        }
        PrintWriter output = new PrintWriter(basefilename+
                "_StudentGrades.txt");
        for (int i = 0;i<student.length;i++){
            if(grades[i]<0){}
            else {
                output.println(student[i] + " " + grades[i] +
                        " " + gradeLetter(grades[i]) +
                        " " + gpaPoints(grades[i]) +
                        " " + status(grades[i]));
            }
        }
        output.close();

        PrintWriter output2 = new PrintWriter(basefilename +
                "_log.txt");
        for (int i = 0; i<grades.length;i++) {
            if (grades[i]<0){
            output2.println("ERROR: Student "+student[i]+
                    " - cannot calculate due to invalid grade entered");

            }
        }output2.close();
    }
    public static void main(String[] args) throws Exception {
        File basefilename = new File(args[0]);
        String C = basefilename + "_CourseDetails.txt";
        String S = basefilename + "_StudentScores.txt";
        Scanner course = new Scanner(new File(C));
        Scanner scores = new Scanner(new File(S));

        String[] student = new String[countCategory(S)];
        double[] grades = new double[countCategory(S)];

        String[] category = new String[countCategory(C)];
        int[] quantity = new int[countCategory(C)];
        int[] weight = new int[countCategory(C)];
        getCategory(category, quantity, weight, C);

        int weightsum = 0;
        for (int i = 0;i<weight.length;i++){
            weightsum += weight[i];
        }
        if (weightsum!=100) {
            PrintWriter output2 = new PrintWriter(basefilename +
                    "_log.txt");
            output2.print("ERROR: Course details - invalid weight " +
                            "- does not sum to 100");
            output2.close();
            System.exit(1);
        }
        for (int i = 0;i<weight.length;i++){
            if (weight[i]<=0){
                PrintWriter output2 = new PrintWriter(basefilename +
                        "_log.txt");
                output2.print("ERROR: Course details - invalid weight " +
                        "- negative weight");
                output2.close();
                System.exit(1);
            }
        }
        for (int i = 0;i<quantity.length;i++){
            if (quantity[i]<=0){
                PrintWriter output2 = new PrintWriter(basefilename +
                        "_log.txt");
                output2.print("ERROR: Course details - invalid quantity " +
                        "- negative quantity");
                output2.close();
                System.exit(1);
            }
        }
        writeGrades(student,grades,args[0]);
    }
}
