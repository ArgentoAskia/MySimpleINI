public class Test<T> {
    private T t;

    public Test(T data){
        t = data;
    }
    public Class<?> getT() {
        return t.getClass();
    }

    public static void main(String[] args) {
        Test<Integer> test = new Test<>(123456);
        System.out.println(test.getT());
    }
}
