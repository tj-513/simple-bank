package test;

public class TestRunner {
    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        CoreServiceTest coreServiceTest = new CoreServiceTest();
        ValidationsTest validationsTest = new ValidationsTest();
        UtilsTest utilsTest = new UtilsTest();

        coreServiceTest.runTests();
        validationsTest.runAllTests();
        utilsTest.runAllTests();
    }
}
