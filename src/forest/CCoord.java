package forest;

public class CCoord {
    int x;
    int y;

    public CCoord(){
        x = 0;
        y = 0;
    }
    public CCoord(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setCoord(int x, int y){
        this.x = x;
        this.y = y;
        return;
    }

    public void generate(int maxLenOX, int maxLenOY){
        x = randomNumber(maxLenOX);
        y = randomNumber(maxLenOY);
    }

    private int randomNumber(int range){
        return  (int) (Math.random()*range);
    }


    //генерация позиции исключая углы
    public void generate2(CCoord rangeX, CCoord rangeY){//min and max значения ячеек в лесу
        this.x = randomNumber2(rangeX);
        this.y = randomNumber2(rangeY);
    }
    private int randomNumber2(CCoord range){
        return (int) (Math.random()*(range.y-range.x)+range.x); // x - min, y -max
    }

}
