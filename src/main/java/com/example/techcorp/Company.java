import java.util.ArrayList;
import java.util.List;

public class Company {
    private String name;
    private double cash;
    private List<Employee> employees;
    private List<Project> projects;

    public Company(String name, double cash) {
        this.name = name;
        this.cash = cash;
        this.employees = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void hire(Employee employee) {
        employees.add(employee);
    }

    public void startProject(Project project) {
        projects.add(project);
    }

    public void showStatus() {
        System.out.println(" === COMPANY STATUS ===");
        System.out.println(" Name: " + name);
        System.out.println(" Cash: " + cash);
        System.out.println(" Employees: " + employees.size());
        System.out.println(" Projects: " + projects.size());

        for (Project project : projects) {
            System.out.println(
                " Project: " + project.getName() +
                ", progress: " + project.getProgress() + "/" +
                project.getRequiredWork() +
                ", finished: " + project.isFinished()
            );
        }
    }

    public String getName() {
        return name;
    }

    public double getCash() {
        return cash;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Project> getProjects() {
        return projects;
    }
}