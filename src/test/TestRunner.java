package test;

public class TestRunner {
    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        CoreServiceTest coreServiceTest = new CoreServiceTest();
        ValidationsTest validationsTest = new ValidationsTest();

        coreServiceTest.runTests();
        validationsTest.runAllTests();
    }
}
