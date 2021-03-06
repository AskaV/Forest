package forest;

import java.io.IOException;
import java.util.LinkedList;
import java.nio.file.*;//Path, Paths;
import java.util.Scanner;

public class CForest {
    LinkedList<CTiger> tigers;//инициализируем пустой
    LinkedList<CRabbit> rabbits;
    //вся генерация с 0

    CFileWrite fileLog;
    CFileWrite fileLog1;
    CFileWrite fileLog2;
    CFileWrite dayLog;

    int saveDay = 15;//(!) //день который мы будем сохранять в файл

    int FoodInOneSellMax = 15;//(!)     // Сколько еды генерируется в одной ячейке в день
    int FoodInOneSellMin = 10;
    int monthDay = 1;//(!)
    int monthDays = 30;         //  Количество дней в месяце
    //10 - Количество стартовых кроликов
    int rabbitStage = 4;        //4 этапа движения по лесу в день у зайцев
    int tigerStage = 1;         //2 этапа движения по лесу в день у тигров

    int oX = 4;                 //длинна поля по х
    int oY = 4;                 //длинна поля по y

    int saveDayPointAct = 15;//(!)        //номер дня который мы будем сохранять в файл


    int[][] food = new int[oX][oY];
    //dayGenFruits[номер дня][фрукты сген. по ох][фрукты сген. по оу]
    int[][][] dayGenFruits = new int[monthDays + 1][][];//(!) //+ monthDays+1 потому что начинаем отсчет дней с 1 а не 0

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
        logFileCreate();//создание файлов для логов
        return monthStage(monthDays, 1);
    }

    public int loadDayFromFile() throws IOException {
        Path pathP = Paths.get("E:\\Git\\Forest\\log\\dayLog.txt");
        Scanner scanN = new Scanner(pathP);
        //scanN.useDelimiter("; ");//нуждно обработать что если в файле не будет пробела то вывалится ексепшн

        String nDay = scanN.next();
        String nTiger = scanN.next();
        String nRabbit = scanN.next();
        scanN.nextLine();
        //String dayTigerRabInfo =scanN.nextLine();
        String tigerArray = scanN.nextLine();
        String rabbitArray = scanN.nextLine();
        System.out.println("Our Day = " + nDay);
        System.out.println("Количество тигров = " + nTiger);
        System.out.println("Количество кроликов = " + nRabbit);
        //for(int i=0; i<Integer.valueOf(nTiger);i++){ }
        System.out.println("Tiger = " + tigerArray);
        System.out.println(rabbitArray);
        //     while(scanN.hasNext()) {
        //scanN.nextInt();
        //         System.out.println(scanN.next());
        //         System.out.print(loadedDay);
        //     }
        //    String dayCount = scanN.next();
        //log(dayCount);
        //читаем построчно
        //while(scanN.hasNext()){
        //String line = scanN.nextLine();
        //log(line);
        //}
        scanN.close();
        this.monthDay = Integer.valueOf(nDay);
        String[] items = tigerArray.split("; ");
        for (int i = 0; i < Integer.valueOf(nTiger); i++) {
            String[] a = items[i].split(" "); //(!)
            //addTigra(a[0]);
            CTiger tiger = this.addTgr(a[0]);
            tiger.food(Integer.valueOf(a[1]));
        }

        String[] Ritems = rabbitArray.split("; ");
        for (int i = 0; i < Integer.valueOf(nRabbit); i++) {
            String[] a = Ritems[i].split(" ");
            //this.addRabbit(a[0]);
            CRabbit rabbit = addRbt(a[0]);// rabbits.get(i);
            //rabbit.color = CColor.valueOf(a[0]);
            rabbit.food(Integer.valueOf(a[1]));
        }
        logFileCreate();

        return monthStage(monthDays, Integer.valueOf(nDay));
    }

    public int monthStage(int monthDays, int dayCount) throws IOException { // определение этапа месяца
        log("Rabbits on the start of " + dayCount + " day");

        for (; dayCount <= (monthDays); dayCount++) {//месяц жизни леса
            initFood(dayCount); //генерация еды в лесу
            lesShowInfo(dayCount);

            for (int j = 1; j <= rabbitStage; j++)//4 этапа хождения кроликов за 1 день
            {
                dayRabbitMove();
                if (j == rabbitStage)
                    RabbitShowInfo();
            }
            for (int i = 1; i <= tigerStage; i++)//2 этапа хождения тигров за 1 день
            {
                log1("\n Day " + dayCount + " xod " + i + ": ");
                log2("\n Day " + dayCount + " xod " + i + ": ");
                for (CTiger tiger : tigers) {
                    dayTigerMove(tiger, false);
                }
            }
            for (CTiger tiger : tigers) {
                if (tiger.rFood() == 0) {
                    log1("\n \tAditional move of tiger " + tiger.rname() + "; Food before move = " + tiger.rFood() + ". ");
                    dayTigerMove(tiger, true);
                }
            }

            tigraGo(dayCount); //условие: каждые 2 дня если у тигра 0 еды он уходит

            for (CTiger tiger : tigers) {     //в конце дня отнимаем у тигров 50 еды
                tiger.subtractionOfFood();
            }

            log1("\n Rab before go\n");
            RabbitShowInfo();
            rabbitGo();
            log1("\n Rab died with 0 food\n");
            RabbitShowInfo();

            for (CRabbit rabbit : rabbits) {     //проверяем кроликов на еду
                rabbit.subtractionOfFood();
            }

            log1("\n\n Rab -20 food \n");
            RabbitShowInfo();
            rabbitSelection(); //в конце дня добавляем новых кролей, только для пар у кого еды не 0
            log1("\n add new reb\n");
            RabbitShowInfo();
            lesShowInfoNight(dayCount);

            //if (this.tigers.size() == 0) {//если умерло два тигра то стопаем программу
            if (this.tigers.size() < 2) { // если умер хоть 1 тигрто стопаем
                log2("tiger died, stop program");
                return 1;
            }
            saveDayToFile(dayCount);
            //loadDayFromFile();

        }
        printFruits();
        return 0;
    }


    public void saveDayToFile(int dayCount) throws IOException {
        if (dayCount != saveDayPointAct) {
            return;
        }
        //строка 1 - №дня__к-во тигров__к-во кроликов   dayLog.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt", (Integer.toString(dayCount) + " " + Integer.toString(this.tigers.size()) + " " + Integer.toString(this.rabbits.size()) + " ;\n"));

        for (CTiger tiger : tigers) {                 //Сохраняем информацию в файл о тиграх
            tiger.tFileWr(saveDayPointAct);
        }
        dayLog.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt", ("\n"));

        for (CRabbit rabbit : rabbits) {                 //Сохраняем информацию в файл о кроликах
            rabbit.rFileWr(saveDayPointAct);
        }

        dayLog.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt", ("\n"));

        // сохраняем размерность леса (в теории если надо)
        //сохраняем текущюю еду в лесу на конец дня
        for (int i = 0; i < oX; i++) {
            for (int j = 0; j < oY; j++) {
                dayLog.WriteFile("E:\\Git\\Forest\\log\\dayLog.txt", (Integer.toString(this.food[i][j]) + " "));

            }
        }
    }

    public void logFileCreate() throws IOException {
        this.fileLog = new CFileWrite();
        this.fileLog.newFile("log1_Old.txt");
        this.fileLog1 = new CFileWrite();
        this.fileLog1.newFile("log2_Old.txt");
        this.fileLog2 = new CFileWrite();
        this.fileLog2.newFile("log3_Old.txt");
        this.dayLog = new CFileWrite();
        this.dayLog.newFile("dayLog.txt");
    }

    public void initFood(int dayOfMonth) {
        this.dayGenFruits[dayOfMonth] = new int[oX][oY];         //создаем двухмерный масив в трехмерном для каждого дня
        for (int i = 0; i < oX; i++) {
            for (int j = 0; j < oY; j++) {
                this.food[i][j] = (int) (Math.random() * ((this.FoodInOneSellMax - FoodInOneSellMin) + 1)) + FoodInOneSellMin; //генерация от 20 до FoodInOneSell(40)
                this.dayGenFruits[dayOfMonth][i][j] = this.food[i][j];//запись еды в трехмерный массив в лесу с сохранением дня
            }
        }
    }

    // печать фруктов на старте каждого дня
    public void printFruits() throws IOException { // печать фруктов на старте каждого дня
        for (int a = monthDay; a <= monthDays; a++) {//начинаем с одного потому что месяц в лесу начинается с числа 1
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

        int[][] RabbitPos = new int[oX][oY]; //(!)
        LinkedList<CCoord> rabFreeList = new LinkedList<>();
        for (int i = 0; i < (oX); i++) {
            for (int j = 0; j < oY; j++) {
                //CCoord rabFree = new CCoord(i,j);
                rabFreeList.add(new CCoord(i, j));
            }
        }

        if (rabbits.size() > 16) { //(!)
            int d = 0;
        }

        for (int count = 0; count < this.rabbits.size(); count++) {
            if (rabFreeList.isEmpty() ) { //rabFreeList.size() == 0
                CRabbit rabbit = this.rabbits.get(count);
                int maxFood = this.food[0][0];
                CCoord maxFoodCoord = new CCoord();
                for (int i = 0; i < oX; i++) {
                    for (int j = 0; j < oY; j++) {
                        if (maxFood < this.food[i][j]) {
                            maxFood = this.food[i][j];
                            maxFoodCoord = new CCoord(i, j);//(!) // maxFoodCoord.setCoord(i, j)
                        }
                    }
                }
                //int i = (int) (Math.random()*rabEatMaxFoodList.size());
                rabbit.setCoord(maxFoodCoord);
                int cellFood = getAvailableFood(rabbit.coord().x, rabbit.coord().y);
                rabbit.food(cellFood);
                this.food[rabbit.coord().x][rabbit.coord().y] -= cellFood;
            }
            else {
                CRabbit rabbit = rabbits.get(count);
                int i = (int) (Math.random() * rabFreeList.size()); //(!)
                rabbit.setCoord(rabFreeList.get(i));
                rabFreeList.remove(i);

                int cellFood = getAvailableFood(rabbit.coord().x, rabbit.coord().y);
                rabbit.food(cellFood);
                this.food[rabbit.coord().x][rabbit.coord().y] -= cellFood;
            }
        }
    }

    public void rabbitMoveForMaxFoodonMap(int rabbitPos) { //(!)


    }

    public void dayTigerMove(CTiger tiger, boolean isExtraMove) throws IOException {               //спросить у тигра куда ходим
        LinkedList<Integer> killRab = new LinkedList<>();

        if (isExtraMove) {//аналогично isExtraMove == true
            CCoord minMaxX = new CCoord();
            CCoord minMaxY = new CCoord();
            minMaxX.x = 1;
            minMaxX.y = oX - 1;
            minMaxY.x = 1;
            minMaxY.y = oY - 1;
            tiger.coord().generate2(minMaxX, minMaxY);
        } else
            tiger.coord().generate(oX, oY);    //генерим координаты тиграм

        //пройтись по всем ячейкам, узнать какие зайци тут находятся, и ими покормить тигра
        for (int count = 0; count < this.rabbits.size(); count++) {                //итерации по зайцам через счетчик от 0 до количества зайцев
            CRabbit rabbit = rabbits.get(count);    //  for (CRabbit rabbit : rabbits) {
            //rabbit.coord().generate(oX, oY);//генерим координаты кроликам
            if (tiger.coord().x == rabbit.coord().x && tiger.coord().y == rabbit.coord().y) {
                //если тигр уже наелся, то след зайца он не будет кушать
                if (tiger.rFood() >= 100)
                    continue;//переход на след итерацию в цикле

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
            rabbits.remove((int) killRab.get(a));  //тут ексепшен, разобратся https://prnt.sc/sjaxaj
        }
    }

    public void rabbitSelection() throws IOException { //в конце дня добавляем новых кролей, только для пар у кого еды не 0

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

//        for (int i = 0; i < (rabitSize - ((rabitSize % 2))/ 2); i++) {
//            this.addRabbitFirst();
//        }


    }

    public void tigraGo(int i) throws IOException { //условие: каждые 2 дня если у тигра 0 еды он уходит

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
            this.tigers.remove((int) killTiger.get(a));
        }
    }
    public int foodSearchAround(CTiger tiger) throws IOException {

        return 0;
    }

    public int foodSearchAround(int tigerNumberInLeast) throws IOException { //(!) //Least IS NOT List
        CTiger tiger = this.tigers.get(tigerNumberInLeast);//(!)
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
                if (j < 0 || i > oY) //(!)
                    continue;

                log2("\n v cikle  x=" + i + ", y =" + j);

                //для лог2 печать всех кроликов на карте
                int[][] tempRabOnMap = new int[oX][oY];
                log2("\n Rabbits on map before tiger around food Search\n");
                for (int count = 0; count < this.rabbits.size(); count++) { //(!) for(CRabbit rabbit : this.rabbits)
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
                if (tiger.rFood() > 0)
                    break;
            }
            if (tiger.rFood() > 0)
                break;
        }
        return tiger.rFood();
    }

    //условие: каждый день если у кролика 0 еды он умирает
    public void rabbitGo() {
        LinkedList<Integer> killRabbit = new LinkedList<>();
        for (int count = 0; count < this.rabbits.size(); count++) {
            CRabbit rabbit = rabbits.get(count);
            if (rabbit.rFood() > 0)
                continue;

            killRabbit.add(count);
        }
        for (int i = killRabbit.size() - 1; i >= 0; i--) {//удалить кролика из леса
            //int b = killRabbit.get(i);
            //rabbits.remove(b);
            rabbits.remove((int) killRabbit.get(i));
        }
    }

    public int getAvailableFood(CCoord coord) { //(!)
        //
        return 0;
    }

    public int getAvailableFood(int i, int j) {
        int rMaxFood = 15; //(!)
        if (this.food[i][j] > rMaxFood)
            return rMaxFood;
        else
            return this.food[i][j];
    }

    // ************************************************Разное по мелочи************************************************
    public CTiger addTgr(String name) throws IOException {
        this.tigers.add(new CTiger(name));
        return this.tigers.getLast();
    }

    public void addTigra(String name) throws IOException {
        this.tigers.add(new CTiger(name));
    }

    private void addRabbit() throws IOException {
        this.rabbits.add(new CRabbit("rand"));
    }

    private CRabbit addRbt(String color) throws IOException {
        this.rabbits.add(new CRabbit(color));
        return this.rabbits.getLast();
    }

    private void addRabbit(String name) throws IOException {
        this.rabbits.add(new CRabbit(name));
    }

    public void addRabbitFirst() throws IOException {
        this.rabbits.addFirst(new CRabbit("rand"));
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
        log1(" \n \n Day " + i + ". Food in forest = " + Integer.toString(getForestFood()));
        log1(";  Food in tigers : ");
        PrintTigerFood();
        log1("**************");
        log1(" Food generated for rabbits:  ");
        FoodShowInfovStroky();
        log1("\n");
        //напечатать в конце этапа список кроликов
        //печатать каждый этап сколько  сьел какой тигр
    }

    public void lesShowInfoNight(int i) throws IOException {
        log1(" \n \n Night " + i + ". Food in forest = " + Integer.toString(getForestFood()));
        log1(";  Food in tigers : ");
        PrintTigerFood();
        //log("\n");
        log1("**************");
        log1(" Food of the end of the day:  ");
        FoodShowInfovStroky();
        //напечатать в конце этапа список кроликов
        //печатать каждый этап сколько  сьел какой тигр
    }

    public void PrintTigerFood() throws IOException {
        for (CTiger tiger : tigers) {
            log1(tiger.rname() + " - " + tiger.rFood() + ", ");
        }
    }

    public void FoodShowInfovStroky() throws IOException {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                log1(" " + this.food[i][j] + " ");
            }
        }
        log1("\n");
    }

    public void FoodShowInfo() throws IOException {
        //log("\n Food generated for rabbits:");
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                log1(" " + this.food[i][j] + " ");
            }
            log1("\n");
        }
    }

    public void RabbitShowInfo() throws IOException {

        log1("| Rabbit name |");
        for (CRabbit rabbit : this.rabbits) {
            log1(rabbit.rColor() + "|"); //для красивой печати вместо этого печатать pColor();
            log1("|");
        }
        log1("\n");
        log1("| Rabbit food |");
        for (CRabbit rabbit : this.rabbits) {
            log1(Integer.toString(rabbit.rFood()) + "|"); //для красивой печати вместо этого печатать pFood();

        }
        //log("\n" + "\n");
    }

    public void lesShowInfof() throws IOException { //просто как пример записи
        log1("\nInitial food in animals:");
        for (CTiger tiger : tigers) {    //for (int i=0; i<this.tiger.size();i++){
            tiger.tInfo();  //this.tiger.get(i).tInfo();
        }
    }

    public void log(String msg) throws IOException { // версия 1 печати информации для леса(тут инфа по ходам тигров, кроликов и еды в лесу)
        System.out.print(msg);
    }

    public void log1(String msg) throws IOException { // версия 1 печати информации для леса(тут инфа по ходам тигров, кроликов и еды в лесу)
        fileLog.WriteFile("E:\\Git\\Forest\\log\\log1_Old.txt", msg);
        //System.out.print(msg);
    }

    public void log2(String msg) throws IOException { // версия 2 печати информации для леса, тут проверка доп хода тигров
        fileLog1.WriteFile("E:\\Git\\Forest\\log\\log2_Old.txt", msg);

        //System.out.print(msg);
    }

    public void log3(String msg) throws IOException { // печать генерируемых фруктов
        fileLog2.WriteFile("E:\\Git\\Forest\\log\\log3_Old.txt", msg);
        //System.out.print("\t" + msg);
    }

}
