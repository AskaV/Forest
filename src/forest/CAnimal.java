package forest;

public class CAnimal {

    private CCoord coord;

    public CAnimal(){
        this.coord = new CCoord();
    }

    public CCoord coord(){
        return this.coord;
    }

    public void setCoord(CCoord rabCoord){ //only for rabbits
        this.coord().x = rabCoord.x;
        this.coord().y = rabCoord.y;
    }

}
