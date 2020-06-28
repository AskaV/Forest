package forest;

import java.io.IOException;
import java.io.FileWriter;
import java.util.LinkedList;

public class CForest {
    LinkedList<CTiger> tigers;//инициализируем пустой
    LinkedList<CRabbit> rabbits;
    //вся генерация с 0

    CFileWrite fileLog;
    CFileWrite fileLog1;
    CFileWrite fileLog2;

    int FoodInOneSellMax = 15;     // Сколько еды генерируется в одной ячейке в день
    int FoodInOneSellMin = 10;
    int monthDays = 30;         //  Количество дней в месяце
    //10 - Количество стартовых кроликов
    int rabbitStage = 4;        //4 этапа движения по лесу в день у зайцев
    int tigerStage = 1;         //2 этапа движения по лесу в день у тигров

    int oX = 4;                 //длинна поля по х
    int oY = 4;                 //длинна поля по y


    int[][] food = new int[oX][oY];
    //dayGenFruits[номер дня][фрукты сген. по ох][фрукты сген. по оу]
    int[][][] dayGenFruits = new int[monthDays + 1][][];//+ monthDays+1 потому что начинаем отсчет дней с 1 а не 0

    public CForest() {
        this.tigers = new LinkedList<>();
        this.rabbits = new LinkedList<>();
    }

    public int initialise() throws IOException {
        for (int i = 0; i < 8; i++) {
            this.addRabbit();
        }
        addTigra("Alena");
        addTigra("Kostik");

        this.fileLog= new CFileWrite();
        this.fileLog.newFile("log1.txt");
        this.fileLog1= new CFileWrite();
        this.fileLog1. newFile("log2.txt");
        this.fileLog2= new CFileWrite();
        this.fileLog2. newFile("log3.txt");

        return monthStage(monthDays);                                                                           //зачем тут ретурн, а не то что выше на 1 строчку???????
    }

    public int monthStage(int monthDays) throws IOException { // определение этапа месяца
        for (int dayCount = 1; dayCount <= (monthDays); dayCount++) {//месяц жизни леса
            initFood(dayCount); //генерация еды в лесу
            lesShowInfo(dayCount);

            for (int j = 1; j <= rabbitStage; j++)//4 этапа хождения кроликов за 1 день
            {
                dayRabbitMove();
                if (j == rabbitStage) RabbitShowInfo();
            }
            for (int i = 1; i <= tigerStage; i++)//2 этапа хождения тигров за 1 день
            {
                log("\n Day " + dayCount + " xod " + i + ": ");
                log2("\n Day " + dayCount + " xod " + i + ": ");
                for (CTiger tiger : tigers) {
                    dayTigerMove(tiger, false);
                }
            }
            for (CTiger tiger : tigers) {
                if (tiger.rFood() == 0) {
                    log("\n \tAditional move of tiger " + tiger.rname() + "; Food before move = " + tiger.rFood() + ". ");
                    dayTigerMove(tiger, true);
                }
            }

            tigraGo(dayCount); //условие: каждые 2 дня если у тигра 0 еды он уходит

            for (CTiger tiger : tigers) {     //в конце дня отнимаем у тигров 50 еды
                tiger.subtractionOfFood();
            }

            log("\n Rab before go\n");
            RabbitShowInfo();
            rabbitGo();
            log("\n Rab died with 0 food\n");
            RabbitShowInfo();

            for (CRabbit rabbit : rabbits) {     //проверяем кроликов на еду
                rabbit.subtractionOfFood();
            }

            log("\n\n Rab -20 food \n");
            RabbitShowInfo();
            rabbitSelection(); //в конце дня добавляем новых кролей, только для пар у кого еды не 0
            log("\n add new reb\n");
            RabbitShowInfo();
            lesShowInfoNight(dayCount);

            //if (this.tigers.size() == 0) {//если умерло два тигра то стопаем программу
            if (this.tigers.size() < 2){ // если умер хоть 1 тигрто стопаем
                log2 ("tiger died, stop program");
                return 1;
            }
        }
        printFruits();
        return 0;
    }

    public void initFood(int dayOfMonth) {
        dayGenFruits[dayOfMonth] = new int[oX][oY];         //создаем двухмерный масив в трехмерном для каждого дня
        for (int i = 0; i < oX; i++) {
            for (int j = 0; j < oY; j++) {
                this.food[i][j] = (int) (Math.random() * ((this.FoodInOneSellMax - FoodInOneSellMin) + 1)) + FoodInOneSellMin; //генерация от 20 до FoodInOneSell(40)
                                this.dayGenFruits[dayOfMonth][i][j] = this.food[i][j];//запись еды в трехмерный массив в лесу с сохранением дня
            }
        }
    }

    // печать фруктов на старте каждого дня
    public void printFruits() throws IOException{ // печать фруктов на старте каждого дня
        for (int a = 1; a <= monthDays; a++) {//начинаем с одного потому что месяц в лесу начинается с числа 1
            for (int y = 0; y < oY; y++) {
                for (int x = 0; x < oX; x++) {
                    log3("arr[" + a + "][" + x + "][" + y + "] = " + dayGenFruits[a][x][y]);
                }
                log3("\n");
            }
            log3("\n");
        }
    }

    //спросить у кролика куда ходим
    public void dayRabbitMove() {        //спросить у кролика куда ходим

        int[][] RabbitPos = new int[oX][oY];

        LinkedList<CCoord> rabFreeList = new LinkedList<>();


        for (int i=0;i<(oX);i++){
            for(int j=0;j<oY;j++){
                //CCoord rabFree = new CCoord(i,j);
                rabFreeList.add(new CCoord(i,j));
            }
        }
            if (rabbits.size()>16) {
            int d = 0;
            }

        for (int count = 0; count < this.rabbits.size(); count++) {

            if(rabFreeList.size()==0){

                CRabbit rabbit = rabbits.get(count);
                int maxFood = this.food[0][0];
                CCoord maxFoodCoord = new CCoord();
                for (int i=0;i<oX;i++){
                    for(int j=0;j<oY;j++){
                        if(maxFood<this.food[i][j]){
                            maxFood = this.food[i][j];
                            maxFoodCoord =new CCoord(i,j);
                        }
                    }
                }
                //int i = (int) (Math.random()*rabEatMaxFoodList.size());
                rabbit.setCoord(maxFoodCoord);
                int cellFood = getAvailableFood(rabbit.coord().x, rabbit.coord().y);
                rabbit.food(cellFood);
                this.food[rabbit.coord().x][rabbit.coord().y] -= cellFood;
                continue;
            }
            CRabbit rabbit = rabbits.get(count);
            int i = (int) (Math.random()*rabFreeList.size());
            rabbit.setCoord(rabFreeList.get(i));
            rabFreeList.remove(i);


            int cellFood = getAvailableFood(rabbit.coord().x, rabbit.coord().y);
            rabbit.food(cellFood);
            this.food[rabbit.coord().x][rabbit.coord().y] -= cellFood;
        }
    }

    public void rabbitMoveForMaxFoodonMap(int rabbitPos){


    }

    public void dayTigerMove(CTiger tiger, boolean isExtraMove) throws IOException {               //спросить у тигра куда ходим
        LinkedList<Integer> killRab = new LinkedList<>();

        if(isExtraMove) {//аналогично isExtraMove == true
            CCoord minMaxX = new CCoord();
            CCoord minMaxY = new CCoord();
            minMaxX.x = 1;
            minMaxX.y = oX - 1;
            minMaxY.x = 1;
            minMaxY.y = oY - 1;
            tiger.coord().generate2(minMaxX, minMaxY);}
        else
            tiger.coord().generate(oX, oY);    //генерим координаты тиграм

        //пройтись по всем ячейкам, узнать какие зайци тут находятся, и ими покормить тигра
        for (int count = 0; count < this.rabbits.size(); count++) {                //итерации по зайцам через счетчик от 0 до количества зайцев
            CRabbit rabbit = rabbits.get(count);    //  for (CRabbit rabbit : rabbits) {
            //rabbit.coord().generate(oX, oY);//генерим координаты кроликам
            if (tiger.coord().x == rabbit.coord().x && tiger.coord().y == rabbit.coord().y) {
                //если тигр уже наелся, то след зайца он не будет кушать
                if (tiger.rFood() >= 100) continue;//переход на след итерацию в цикле
                tiger.food(rabbit.rFood());//кормим тигра на еду полученную у кролика
                PrintTigerFood();
                killRab.add(count);// запись кого надо удалить
                //rabbits.remove(count); так делать небезопасно
            }
        }
        killRabbit(killRab);
    }

    public void killRabbit(LinkedList<Integer> killRab) {//удалить зайца из леса безопасно
        for (int a = killRab.size() - 1; a >= 0; a--) {
            //int b = killRab.get(a);
            rabbits.remove((int)killRab.get(a));  //тут ексепшен, разобратся https://prnt.sc/sjaxaj
        }
    }

    public void rabbitSelection() { //в конце дня добавляем новых кролей, только для пар у кого еды не 0

        int rabitSize = 0;
        for (CRabbit rabbit : rabbits) {  //узнаем количество кролей с едой не 0
            if (rabbit.rFood() > 0)
                rabitSize++;
        }

        if ((rabitSize % 2) == 0) {         //добавляем новых кролей, для пар, у которых еды не 0
            for (int i = 0; i < (rabitSize / 2); i++) {
                this.addRabbitFirst();
            }
        } else {
            for (int i = 0; i < ((rabitSize - 1) / 2); i++) {
                this.addRabbitFirst();
            }
        }
    }

    public void tigraGo(int i) throws IOException{ //условие: каждые 2 дня если у тигра 0 еды он уходит

        LinkedList<Integer> killTiger = new LinkedList<>();
        if (i % 2 == 0) {
            for (int count = 0; count < this.tigers.size(); count++) {
                CTiger tiger = this.tigers.get(count);
                if (tiger.rFood() > 0)
                    continue;
                log2(" Funk tigraGO - Tiger " + tiger.rname() + " has food " + tiger.rFood() + ";" + "\n Start loop search:");
                if (foodSearchAround(count) > 0)
                    continue;//наш дополнительный поиск еды по кругу
                killTiger.add(count);
            }
        }
        for (int a = killTiger.size() - 1; a >= 0; a--) {//удалить тигра из леса
            //int b = killTiger.get(a);
            this.tigers.remove((int)killTiger.get(a));
        }
    }

    public int foodSearchAround(int tigerNumberInLeast) throws IOException{
        CTiger tiger = tigers.get(tigerNumberInLeast);

        LinkedList<Integer> killRab = new LinkedList<>();

        int minX = tiger.coord().x - 1;
        int minY = tiger.coord().y - 1;
        int maxX = tiger.coord().x + 1;
        int maxY = tiger.coord().y + 1;
        log2(" tiger " + tiger.rname() + " has coordinats x = " + tiger.coord().x + ", y = " + tiger.coord().y + "; \n");
        for (int i = minX; i <= maxX; i++) {
            if (i < 0 || i > oX)
                continue;
            for (int j = minY; j <= maxY; j++) {
                if (j < 0 || i > oY)
                    continue;
                log2("\n v cikle  x=" + i + ", y =" + j);

                //для лог2 печать всех кроликов на карте
                int[][] tempRabOnMap = new int[oX][oY];
                log2("\n Rabbits on map before tiger around food Search\n");
                for (int count = 0; count < this.rabbits.size(); count++) {
                    CRabbit rabbit = rabbits.get(count);    //  for (CRabbit rabbit : rabbits)
                    tempRabOnMap[rabbit.coord().x][rabbit.coord().y] += rabbit.rFood();

                }
                for (int a = 0; a < oX; a++) {
                    for (int b = 0; b < oY; b++) {
                        log2(" " + Integer.toString(tempRabOnMap[a][b]) + " ");
                    }
                    log2("\n");

                }
                log2("\n");

                for (int count = 0; count < this.rabbits.size(); count++) {    //итерации по зайцам через счетчик от 0 до количества зайцев
                    CRabbit rabbit = rabbits.get(count);    //  for (CRabbit rabbit : rabbits)
                    if (i == rabbit.coord().x && j == rabbit.coord().y) {
                        log2("|| Tiger find _" + rabbit.rColor() + "_ rabbit with food " + rabbit.rFood());
                        tiger.food(rabbit.rFood()); //кормим тигра
                        log2("  || tiger food after eateng rabbit= " + tiger.rFood());
                        tiger.coord().x = i;
                        tiger.coord().y = j;
                        killRab.add(count);
                        //break;
                    }
                }
                killRabbit(killRab);
                killRab.clear();
                if (tiger.rFood() > 0) break;
            }
            if (tiger.rFood() > 0) break;
        }
        return tiger.rFood();
    }

    //условие: каждый день если у кролика 0 еды он умирает
    public void rabbitGo() {
        LinkedList<Integer> killRabbit = new LinkedList<>();
        for (int count = 0; count < this.rabbits.size(); count++) {
            CRabbit rabbit = rabbits.get(count);
            if (rabbit.rFood() > 0) continue;
            killRabbit.add(count);
        }
        for (int i = killRabbit.size() -1; i >= 0; i--) {//удалить кролика из леса
            //int b = killRabbit.get(i);
            //rabbits.remove(b);
            rabbits.remove((int)killRabbit.get(i));
        }
    }

    public int getAvailableFood(int i, int j) {
        int rMaxFood = 15;
        if (this.food[i][j] > rMaxFood) return rMaxFood;
        else return this.food[i][j];
    }

    // ************************************************Разное по мелочи************************************************
    public void addTigra(String name) {
        this.tigers.add(new CTiger(name));
    }

    public void addRabbit() {
        this.rabbits.add(new CRabbit());
    }

    public void addRabbitFirst() {
        this.rabbits.addFirst(new CRabbit());
    }


    public int getForestFood() {//Показывает сумму еды в лесу
        int food = 0;
        for (int i = 0; i < oX; i++) {
            for (int j = 0; j < oY; j++) {
                food += this.food[i][j];
            }
        }
        return food;
    }

    public void lesShowInfo(int i) throws IOException {
        log(" \n \n Day " + i + ". Food in forest = " + Integer.toString(getForestFood()));
        log(";  Food in tigers : ");
        PrintTigerFood();
        log("**************");
        log(" Food generated for rabbits:  ");
        FoodShowInfovStroky();
        log("\n");
        //напечатать в конце этапа список кроликов
        //печатать каждый этап сколько  сьел какой тигр
    }

    public void lesShowInfoNight(int i) throws IOException {
        log(" \n \n Night " + i + ". Food in forest = " + Integer.toString(getForestFood()));
        log(";  Food in tigers : ");
        PrintTigerFood();
        //log("\n");
        log("**************");
        log(" Food of the end of the day:  ");
        FoodShowInfovStroky();
        //напечатать в конце этапа список кроликов
        //печатать каждый этап сколько  сьел какой тигр
    }

    public void PrintTigerFood() throws IOException {
        for (CTiger tiger : tigers) {
            log(tiger.rname() + " - " + tiger.rFood() + ", ");
        }
    }

    public void FoodShowInfovStroky() throws IOException {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                log(" " + this.food[i][j] + " ");
            }
        }
        log("\n");
    }

    public void FoodShowInfo() throws IOException {
        //log("\n Food generated for rabbits:");
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                log(" " + this.food[i][j] + " ");
            }
            log("\n");
        }
    }

    public void RabbitShowInfo() throws IOException {

        log("| Rabbit name |");
        for (CRabbit rabbit : this.rabbits) {
            log(rabbit.rColor() + "|"); //для красивой печати вместо этого печатать pColor();
            log("|");
        }
        log("\n");
        log("| Rabbit food |");
        for (CRabbit rabbit : this.rabbits) {
            log(Integer.toString(rabbit.rFood()) + "|"); //для красивой печати вместо этого печатать pFood();

        }
        //log("\n" + "\n");
    }

    public void lesShowInfof() throws IOException { //просто как пример записи
        log("\nInitial food in animals:");
        for (CTiger tiger : tigers) {    //for (int i=0; i<this.tiger.size();i++){
            tiger.tInfo();  //this.tiger.get(i).tInfo();
        }
    }

    public void log(String msg) throws IOException { // версия 1 печати информации для леса(тут инфа по ходам тигров, кроликов и еды в лесу)
        fileLog.WriteFile("E:\\Git\\Forest\\log1.txt",msg);
        //System.out.print(msg);
    }

    public void log2(String msg)  throws IOException { // версия 2 печати информации для леса, тут проверка доп хода тигров
        fileLog1.WriteFile("E:\\Git\\Forest\\log2.txt",msg);

        //System.out.print(msg);
    }

    public void log3(String msg) throws IOException { // печать генерируемых фруктов
        fileLog2.WriteFile("E:\\Git\\Forest\\log3.txt",msg);
        //System.out.print("\t" + msg);
    }


}
