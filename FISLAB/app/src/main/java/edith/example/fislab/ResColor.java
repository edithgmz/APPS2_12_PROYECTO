package edith.example.fislab;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class ResColor {//el resistor vale x1*10 + x2 + 10^x3 +- x4 donde x4 solo es dorado o plateado



    final static int BROWN[] = {}; //café es 1
    final static int RED[] = {Color.rgb(141, 115, 64),//rojo es 2
            Color.rgb(162, 62, 70),
            Color.rgb(138, 51, 60),
    }; //rojo es 2
    final static int YELLOW[] = {Color.rgb(151, 121, 65), //amarillo es 4
            Color.rgb(127, 104, 62),
            Color.rgb(141, 115, 64),
    };

    final static int PURPLE[] = {Color.rgb(116, 87, 117), //el morado vale 7
            Color.rgb(131, 119, 154),
            Color.rgb(141, 131, 164),
    };
    final int grey[] = {Color.GRAY,};


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isColor(int mycol, int[] color){
        int dif=10;
        boolean res=false;
        System.out.println("sistema corre");
        for ( int elcol :  color){ //seria mas eficiente con un while
            if ( Color.blue(mycol) >= Color.blue(elcol)-dif && Color.blue(mycol) <= Color.blue(elcol)+dif &&
                    Color.red(mycol) >= Color.red(elcol)-dif && Color.red(mycol) <= Color.red(elcol)+dif &&
                    Color.green(mycol) >= Color.green(elcol)-dif && Color.green(mycol) <= Color.green(elcol)+dif ){
                res =  true;
            }
            int imin =Color.blue(elcol)-dif;
            int imax =Color.blue(elcol)+dif;
            String smin = imin+"";
            String smax = imax+"";
            System.out.println("blue.-min: " +smin+", max: "+smax);
        }
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getDigit(int color){
        int dig=-1;
        if(ResColor.isColor(color,ResColor.BROWN)){
            dig=1;
        }else if(ResColor.isColor(color,ResColor.RED)){
            dig=2;
        }else if(ResColor.isColor(color,ResColor.YELLOW)){
            dig=4;
        }
        else if (ResColor.isColor(color, ResColor.PURPLE)){
            dig=7;
        }
        return dig;
    }

    public String calculaRes(int b1,int b2,int b3){
        double  val = b1*10+b2+Math.pow(10,b3); //valor
        String valCi= b1*10+b2 +"x10^"+b3;//valor en nota 100tifica.
        return valCi;
    }

}