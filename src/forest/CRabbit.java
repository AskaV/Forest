package forest;

import java.io.IOException;

public class CRabbit extends CAnimal{
    private int food;
    private CColor color;
    //private CCoord coord;
    private CFileWrite fileSDay;


    public CRabbit(String name) throws IOException {
        this.food = 0;
        CColor rabbitColor = CColor.Invalid;
        if (name == "rand") {
            this.color = rabbitColor.getRandomColor();
        }
        else {
            this.color = CColor.valueOf(name);
        }

        this.fileSDay= new CFileWrite();

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

    public void rFileWr(int Day) throws IOException {
        fileSDay.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt", String.valueOf((this.color))+" ");// сохраняем имя кролика
        fileSDay.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt",(Integer.toString(this.food))+"; ");// сохраняем еду кролика
    }
}
