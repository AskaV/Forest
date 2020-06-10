package forest;

public class CTiger {
    private int food;
    private  String name;
    private CCoord coord;

    public CTiger(String name){
        this.name = name;
        this.food = 0;
        this.coord = new CCoord();
    }

    public void food (int food) {
        this.food += food;
    }

    public float age(){
        return (float) food/100;
    }

    public  void subtractionOfFood(){this.food-=50; if(this.food<0) this.food=0;}

   // public void twoDayGo(){ if (this.food=0) ;}

    public CCoord coord(){
        return this.coord;
    }

    public void comp(float par){
        System.out.print((((float)this.food/100< par) ?"On ":"Ya ") + "starshe");
    }
    public String rname(){ return this.name;}
    public int rFood(){
        return this.food;
    }

    public void tInfo() {
        System.out.println("Tiger name is " + this.name+", food = " + this.food+", Age = " + age());

    }
}
