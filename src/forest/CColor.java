package forest;

import java.util.Random;

public enum  CColor {

    Invalid,White,Black,Grey,Blue,BlackWhite,Ginger,Peach,Tan,Brown;
    //int length = CColor.values().length;



    public CColor getRandomColor(){

        int length = (CColor.values().length); //Длинна enum-a CColor
        int a = (int) (Math.random()*(length-1)+1);
        for (CColor color : CColor.values()) {
            if (color.ordinal() == a)
            {
                return color;
            }

        }
        return CColor.Invalid;
    }
}


