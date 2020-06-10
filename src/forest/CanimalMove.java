package forest;

public class CanimalMove {

    int[][] AnimalOnMap = new int[4][4];

    public CanimalMove(){
    }

    public int animalMoveX(){
        int i= (int) (Math.random()*4);
        //int[][] rabPosition = new int[4][4];
        //rabPosition[i][j] = 1;
        //return rabPosition[i][j];
        return i;
    }
    public int animalMoveY(){
        int i= (int) (Math.random()*4);
        return i;
    }



}
