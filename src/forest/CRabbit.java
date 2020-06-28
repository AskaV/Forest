package forest;

public class CRabbit extends CAnimal{
    private int food;
    private CColor color;
    //private CCoord coord;


    public CRabbit() {
        this.food = 0;
        CColor rabbitColor = CColor.Invalid;
        this.color = rabbitColor.getRandomColor();
    }

    public void food(int food) {
        this.food += food;
    }

    //public void rName(){System.out.println(this.color);}
    public int rFood() {
        return this.food;
    }
    public CColor rColor() {return this.color;}

    public  void subtractionOfFood(){this.food-=20; if(this.food<0) this.food=0;}

    public void rInfo() {
        System.out.println("Rabbit color = " + this.color + ", food = " + this.food);
    }
    public void pColor() {
        System.out.printf("%12s", this.color);
    }
    public void pFood() {
        System.out.printf("%12d", this.food);
    }

}
