import java.lang.String;
import java.io.File;


public class Tree {

    public static void outputHelper(File f, boolean onlyDir, boolean color, boolean layer, int layerNum, boolean hidden, String space) {
        String red = "\u001B[31m"; // red for directories
        String green = "\u001B[32m"; // green for executables
        String colorReset = "\033[0m";
        if(!color) {
            red = "";
            green = "";
        }
        //String arr[] = f.list();
        File[] fileList = f.listFiles();
        java.util.Arrays.sort(fileList);
        if(fileList != null) {
            int layerTemp = layerNum - 1;
            for(int i = 0; i < fileList.length; i++) {

                if (!hidden) {
                    if (onlyDir) {
                        if (fileList[i].isDirectory()) {
                            System.out.println(red + space + fileList[i].getName());
                            System.out.print(colorReset);
                        }
                    } else {
                        if (fileList[i].isDirectory()) {
                            System.out.println(red + space + fileList[i].getName());
                            System.out.print(colorReset);
                        } else if (fileList[i].canExecute()) {
                            System.out.println(green + space + fileList[i].getName());
                            System.out.print(colorReset);
                        } else {
                            System.out.println(space + fileList[i].getName());
                            System.out.print(colorReset);
                        }
                    }
                } else {
                    if (onlyDir) {
                        if (fileList[i].isDirectory() && !fileList[i].isHidden()) {
                            System.out.println(red + space + fileList[i].getName());
                            System.out.print(colorReset);
                        }
                    } else {
                        if (fileList[i].isDirectory() && !fileList[i].isHidden()) {
                            System.out.println(red + space + fileList[i].getName());
                            System.out.print(colorReset);
                        } else if (fileList[i].canExecute() && !fileList[i].isHidden()) {
                            System.out.println(green + space + fileList[i].getName());
                            System.out.print(colorReset);
                        } else {
                            System.out.println(space + fileList[i].getName());
                            System.out.print(colorReset);
                        }
                    }
                }

                if (layer && layerNum > 0) {
                    String spaceTemp = space + "  ";
                    outputHelper(fileList[i], onlyDir, color, layer, layerTemp, hidden, spaceTemp);
                }
            }
        }

    }

    public static void output(boolean onlyDir, boolean color, boolean layer, int layerNum, String dir, boolean hidden) {


        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

        File f;
        if(dir.equals("") || dir.equals("/")) {
            f = new File(System.getProperty("user.dir"));
        } else {
            f = new File(System.getProperty("user.dir") + "\\" + dir);
        }
        if(!f.exists()) {
            System.out.println(f.getName() + " doesn't exist.");
            return;
        }
        String space = new String("  ");

        //System.out.println("Correct Directory = " + f.getPath());
        outputHelper(f, onlyDir, color, layer, layerNum, hidden, space);

    }

    public static void main(String[] args) {

        boolean help = false;
        String dir = new String();
        boolean onlyDir = false;
        boolean color = true;
        boolean layer = false;
        int layerNum = 1;
        boolean hidden = false;
        String errorMessage = new String();
        boolean error = false;

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-help")|| args[i].equals("-HELP")) {
                help = true;
            } else if (args[i].equals("-d") || args[i].equals("-D")) {
                onlyDir = true;
            } else if (args[i].equals("-a") || args[i].equals("-A")) {
                hidden = true;
            } else if (args[i].equals("-c") || args[i].equals("-C")) {
                if((i+1) >= args.length) {
                    errorMessage = "'-c' argument is not provided. provide true or false";
                    error = true;
                } else if (args[i+1].equals("true")) {
                    color = true;
                } else if (args[i+1].equals("false")) {
                    color = false;
                } else {
                    errorMessage = "'-c' argument is invalid. provide true or false";
                    error = true;
                }
            } else if (args[i].equals("-l") || args[i].equals("-L")) {
                if((i+1) >= args.length) {
                    errorMessage = "'-l' argument is not provided. provide int greater than 0";
                    error = true;

                } else {
                    try {
                        layerNum = Integer.parseInt(args[i+1]);
                        layer = true;
                        if(layerNum < 1) {
                            errorMessage = "'-l' argument is invalid. provide int greater than 0";
                            error = true;
                        }
                    } catch(NumberFormatException e) {
                        errorMessage = "'-l' argument is invalid. provide int greater than 0";
                        error = true;
                    }
                }
            } else if (i == 0) {
                dir = args[0];
            }
        }
        if(help) {
            System.out.println("  Usage: tree [dir] [-option [parameter]]");
            System.out.println("  [dir]                     :: directory to start traversal [.]");
            System.out.println("  -help                     :: display this help and exit [false]");
            System.out.println("  -c true|false             :: show entries colorized [true]");
            System.out.println("  -d                        :: list directories only [false]");
            System.out.println("  -l n                      :: maximum display depth [1]");
            System.out.println("  -a                        :: show hidden files [false]");
        } else if (error) {
            System.out.println(errorMessage);
        } else {
            output(onlyDir, color, layer, layerNum, dir, hidden);
        }

    }
}


