public class Main {
    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50000);

        Employee anna = new Employee("Anna", 8, 7000);
        Employee piotr = new Employee("Piotr", 6, 6500);

        company.hire(anna);
        company.hire(piotr);

        Project project = new Project("Mobile App", 30);
        project.addEmployee(anna);
        project.addEmployee(piotr);

        company.startProject(project);

        System.out.println("Stan poczatkowy:");
        company.showStatus();

        System.out.println("\n--- Tura 1 ---");
        project.workOneTurn();
        company.showStatus();

        System.out.println("\n--- Tura 2 ---");
        project.workOneTurn();
        company.showStatus();
    }
}