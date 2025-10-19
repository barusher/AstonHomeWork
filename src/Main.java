import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        NewHashMap<String, Integer> map = new NewHashMap<>();
        map.put("один", 1);
        map.put("два", 2);
        map.put("три", 3);
        System.out.println("Получить 'один': " + map.get("один"));
        System.out.println("Получить 'два': " + map.get("два"));
        System.out.println("Размер: " + map.size());
        map.put("два", 22);
        System.out.println("После обновления - Получить 'два': " + map.get("два"));
        map.remove("два");
        System.out.println("После удаления - Получить 'два': " + map.get("два"));
        System.out.println("Содержит ключ 'два': " + map.containsKey("два"));
        map.clear();
        System.out.println("После очистки - Размер: " + map.size());
        System.out.println("Пустой: " + map.isEmpty());
    }
}