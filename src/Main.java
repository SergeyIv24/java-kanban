import java.util.HashMap;
public class Main {

    public static void main(String[] args) {
        TaskManager test = new TaskManager();

        HashMap<Integer, Integer> testMap = new HashMap<>();

        test.deleteAllEpics();
        System.out.println(test.printAllEpics());


        //test.createEpic("Приготовить ужин", "Готовим ужин");
        System.out.println(test.printAllEpics());



/*        testMap.put(1 , 0);
        testMap.put(2,0);
        System.out.println(testMap);

        System.out.println(testMap.get(1));*/




    }
}
