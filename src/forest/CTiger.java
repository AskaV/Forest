package forest;

import java.io.IOException;

public class CTiger extends CAnimal {
    private int food;
    private String name;
    private CFileWrite fileSDay;

    public CTiger(String name) throws IOException {
        this.food = 0;
        this.fileSDay = new CFileWrite();
        this.name = name;
    }

    public void food(int food) {
        this.food += food;
    }

    public float age() {
        return (float) food / 100;
    }

    public void subtractionOfFood() {
        this.food -= 50; //(!)
        if (this.food < 0)
            this.food = 0;
    }

    public void comp(float par) {
        System.out.print((((float) this.food / 100 < par) ? "On " : "Ya ") + "starshe");
    }

    public String rname() {
        return this.name;
    }

    public int rFood() {
        return this.food;
    }

    public void tInfo() {
        System.out.println("Tiger name is " + this.name + ", food = " + this.food + ", Age = " + age());

    }

    public void tFileWr(int Day) throws IOException {
        fileSDay.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt", (this.name) + " ");// сохраняем имя тигра

        fileSDay.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt", (Integer.toString(this.food)) + "; ");// сохраняем еду тигра
    }


}
