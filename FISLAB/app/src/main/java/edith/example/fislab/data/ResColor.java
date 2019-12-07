package edith.example.fislab.data;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class ResColor {//el resistor vale x1*10 + x2 + 10^x3 +- x4 donde x4 solo es dorado o plateado

    private final static int[] RED = {Color.rgb(141, 115, 64), //rojo es 2
                                      Color.rgb(162, 62, 70), Color.rgb(138, 51, 60)};

    private final static int[] YELLOW = {Color.rgb(151, 121, 65), //amarillo es 4
                                         Color.rgb(127, 104, 62), Color.rgb(141, 115, 64)};

    private final static int[] PURPLE = {Color.rgb(116, 87, 117), //morado es 7
                                         Color.rgb(131, 119, 154), Color.rgb(141, 131, 164)};

    private final static int[] GRAY = {Color.rgb(101, 165, 170), //gris es 8
                                       Color.rgb(101, 165, 170), Color.rgb(94, 131, 151), Color.rgb(61, 89, 110), Color.rgb(91, 100, 97),
                                       Color.rgb(91, 97, 97), Color.rgb(86, 97, 93), Color.rgb(89, 104, 97), Color.rgb(88, 97, 94)};

    private final static int[] BROWN = {Color.rgb(72, 70, 82), //café es 1
                                        Color.rgb(96, 91, 111), Color.rgb(81, 90, 108), Color.rgb(64, 57, 77), Color.rgb(31, 30, 38),
                                        Color.rgb(40, 50, 60), Color.rgb(89, 82, 74), Color.rgb(88, 86, 74)};

    @RequiresApi(api = Build.VERSION_CODES.O) public static boolean isColor(int mycol, int[] color) {
        int dif = 10;
        boolean res = false;

        for (int elcol : color) { //seria mas eficiente con un while
            if (Color.blue(mycol) >= Color.blue(elcol) - dif && Color.blue(mycol) <= Color.blue(elcol) + dif &&
                Color.red(mycol) >= Color.red(elcol) - dif && Color.red(mycol) <= Color.red(elcol) + dif &&
                Color.green(mycol) >= Color.green(elcol) - dif && Color.green(mycol) <= Color.green(elcol) + dif) {
                res = true;
            }

            int imin = Color.blue(elcol) - dif;
            int imax = Color.blue(elcol) + dif;
            String smin = imin + "";
            String smax = imax + "";
        }

        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O) public static int getDigit(int color) {
        int dig = -1;

        if (ResColor.isColor(color, ResColor.BROWN)) {
            dig = 1;
        } else if (ResColor.isColor(color, ResColor.RED)) {
            dig = 2;
        } else if (ResColor.isColor(color, ResColor.YELLOW)) {
            dig = 4;
        } else if (ResColor.isColor(color, ResColor.PURPLE)) {
            dig = 7;
        } else if (ResColor.isColor(color, ResColor.GRAY)) {
            dig = 8;
        }

        return dig;
    }

    public static String calculaRes(int b1, int b2, int b3, boolean tol) {
        double val = ((b1 * 10) + b2) + Math.pow(10, b3); //valor
        String sTol = "±";

        if (tol) {
            sTol += "5"; //dorado
        } else {
            sTol += "10"; //plateado
        }

        return ((b1 * 10) + b2) + "x10^" + b3 + sTol; //valor en nota 100tifica.
        //return val + sTol;
    }
}
