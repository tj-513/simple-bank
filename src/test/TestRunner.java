package test;

public class TestRunner {
    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        CoreServiceTest coreServiceTest = new CoreServiceTest();
        coreServiceTest.runTests();
    }
}
